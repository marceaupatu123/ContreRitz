#!/usr/bin/env python3
"""Affiche la couverture des lignes de la logique métier (service + repository).

Lit le rapport CSV de JaCoCo. GitLab récupère le pourcentage affiché grâce à
l'expression `coverage:` du job `maven-tests` dans .gitlab-ci.yml.
"""
import csv
import sys

CSV_PATH = "target/site/jacoco/jacoco.csv"

try:
    with open(CSV_PATH, encoding="utf-8", newline="") as f:
        rows = list(csv.DictReader(f))
except FileNotFoundError:
    print("Aucun rapport de couverture trouvé (les tests ont peut-être échoué).")
    sys.exit(0)

missed = sum(int(r["LINE_MISSED"]) for r in rows)
covered = sum(int(r["LINE_COVERED"]) for r in rows)
total = missed + covered

for r in rows:
    line_total = int(r["LINE_MISSED"]) + int(r["LINE_COVERED"])
    if line_total:
        pct = 100 * int(r["LINE_COVERED"]) / line_total
        print(f"  {r['PACKAGE']}.{r['CLASS']}: {pct:.1f}% ({r['LINE_COVERED']}/{line_total} lignes)")

if total == 0:
    print("Couverture des lignes (service + repository) : aucune ligne à mesurer")
else:
    print(f"Couverture des lignes (service + repository) : {100 * covered / total:.1f}%")
