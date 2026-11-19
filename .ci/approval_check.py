#!/usr/bin/env python3
"""Échoue tant que la merge request n'a pas été approuvée par quelqu'un d'autre que son auteur.

GitLab Free ne permet pas d'exiger une approbation : ce job contourne la limite.
Il suffit de relancer le job `approval-check` après l'approbation.

Variable CI/CD requise : APPROVAL_TOKEN (Project Access Token, rôle Reporter, scope read_api).
"""
import json
import os
import sys
import urllib.request

token = os.environ.get("APPROVAL_TOKEN")
if not token:
    print("ATTENTION : variable APPROVAL_TOKEN absente, contrôle d'approbation ignoré.")
    sys.exit(0)

url = (f"{os.environ['CI_API_V4_URL']}/projects/{os.environ['CI_PROJECT_ID']}"
       f"/merge_requests/{os.environ['CI_MERGE_REQUEST_IID']}/approvals")
request = urllib.request.Request(url, headers={"PRIVATE-TOKEN": token})
with urllib.request.urlopen(request, timeout=30) as response:
    data = json.load(response)

author = data.get("author", {}).get("username") or os.environ.get("GITLAB_USER_LOGIN")
approvers = [a["user"]["username"] for a in data.get("approved_by", [])]
valid = [a for a in approvers if a != author]

if valid:
    print("MR approuvée par : " + ", ".join(valid))
    sys.exit(0)

print("Cette MR n'est pas encore approuvée par un autre membre de l'équipe.")
print("Demande une relecture, puis clique sur « Approve » et relance ce job.")
sys.exit(1)
