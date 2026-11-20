#!/usr/bin/env python3
"""Génère SUIVI_TEMPS.md à partir du temps noté dans GitLab (/spend).

Un titre par personne, puis le temps passé sur chaque tâche (issue ou merge request).
Le fichier est entièrement régénéré : on ne le modifie pas à la main.

Ce script est prévu pour tourner dans le job `suivi-temps` de .gitlab-ci.yml.

Variable CI/CD requise : APPROVAL_TOKEN (Project Access Token, scope read_api).
"""
import json
import os
import pathlib
import sys
import urllib.error
import urllib.request
from collections import defaultdict
from datetime import date

OUT = pathlib.Path("SUIVI_TEMPS.md")

QUERY = """
query($path: ID!, $after: String) {
  project(fullPath: $path) {
    timelogs(first: 100, after: $after) {
      pageInfo { hasNextPage endCursor }
      nodes {
        timeSpent
        user { name username }
        issue { iid title }
        mergeRequest { iid title }
      }
    }
  }
}
"""


def config():
    """Lit l'instance, le projet et le jeton dans les variables du CI."""
    token = os.environ.get("APPROVAL_TOKEN")
    if not token:
        sys.exit("Variable APPROVAL_TOKEN absente : à ajouter dans Settings > CI/CD > Variables.")
    try:
        return os.environ["CI_SERVER_URL"].rstrip("/"), os.environ["CI_PROJECT_PATH"], token
    except KeyError as missing:
        sys.exit(f"Variable {missing} absente : ce script doit tourner dans un job GitLab CI.")


def fetch_timelogs(server, path, token):
    nodes, after = [], None
    while True:
        body = json.dumps({"query": QUERY, "variables": {"path": path, "after": after}}).encode("utf-8")
        request = urllib.request.Request(
            f"{server}/api/graphql", data=body,
            headers={"PRIVATE-TOKEN": token, "Content-Type": "application/json"})
        try:
            with urllib.request.urlopen(request, timeout=30) as response:
                data = json.loads(response.read())
        except urllib.error.HTTPError as error:
            sys.exit(f"Erreur HTTP {error.code} : le jeton est-il valide (scope read_api) ?")
        if data.get("errors"):
            sys.exit(f"Erreur GraphQL : {data['errors']}")
        project = (data.get("data") or {}).get("project")
        if project is None:
            sys.exit("Projet introuvable ou jeton sans droit de lecture sur ce projet.")
        page = project["timelogs"]
        nodes += page["nodes"]
        if not page["pageInfo"]["hasNextPage"]:
            return nodes
        after = page["pageInfo"]["endCursor"]


def fmt(seconds):
    sign = "-" if seconds < 0 else ""
    seconds = abs(seconds)
    return f"{sign}{seconds // 3600}h{(seconds % 3600) // 60:02d}"


def escape(text):
    return text.replace("|", "\\|")


def main():
    server, path, token = config()
    timelogs = fetch_timelogs(server, path, token)

    # personne -> tâche -> secondes
    per_user = defaultdict(lambda: defaultdict(int))
    names = {}
    for log in timelogs:
        user = log["user"]
        names[user["username"]] = user["name"]
        if log["issue"]:
            task = (0, log["issue"]["iid"], f"#{log['issue']['iid']} {log['issue']['title']}")
        elif log["mergeRequest"]:
            task = (1, log["mergeRequest"]["iid"], f"!{log['mergeRequest']['iid']} {log['mergeRequest']['title']}")
        else:
            task = (2, 0, "Autre")
        per_user[user["username"]][task] += log["timeSpent"]

    lines = [
        "# Suivi du temps",
        "",
        "> Fichier **généré automatiquement** à partir du temps noté dans GitLab (`/spend`).",
        "> Ne pas le modifier à la main : pour ajouter du temps, noter `/spend 1h30` dans un commentaire",
        "> de l'issue concernée, puis relancer le job `suivi-temps` du pipeline.",
        "",
        f"Dernière mise à jour : {date.today().isoformat()}",
        "",
    ]
    total_general = 0
    for username in sorted(per_user, key=lambda u: names[u].lower()):
        tasks = per_user[username]
        total = sum(tasks.values())
        total_general += total
        lines += [f"## {names[username]}", "", "| Tâche | Temps |", "|-------|-------|"]
        for key in sorted(tasks):
            lines.append(f"| {escape(key[2])} | {fmt(tasks[key])} |")
        lines += ["", f"**Total : {fmt(total)}**", ""]
    lines += ["## Total de l'équipe", "", f"**{fmt(total_general)}**", ""]

    OUT.write_text("\n".join(lines), encoding="utf-8")
    print(f"{OUT} écrit : {len(timelogs)} entrées de temps, {len(per_user)} personne(s), "
          f"total {fmt(total_general)}.")


if __name__ == "__main__":
    main()
