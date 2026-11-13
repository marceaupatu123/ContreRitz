#!/usr/bin/env python3
"""Résumé lisible du rapport Qodana + export GitLab Code Quality."""
import hashlib
import json
import pathlib
import sys

SARIF = pathlib.Path(".qodana/results/qodana.sarif.json")
OUT = pathlib.Path("gl-code-quality-report.json")
MAX_LINES = 40

RED, YEL, CYA, BLD, RST = "\033[31;1m", "\033[33;1m", "\033[36m", "\033[1m", "\033[0m"
LEVELS = {  # niveau SARIF -> (libellé, couleur, sévérité GitLab, ordre)
    "error": ("ERREUR", RED, "major", 0),
    "warning": ("WARNING", YEL, "minor", 1),
    "note": ("INFO", CYA, "info", 2),
}

if not SARIF.exists():
    print("Aucun rapport SARIF trouvé : Qodana n'a pas produit de résultats.")
    OUT.write_text("[]")
    sys.exit(0)

data = json.loads(SARIF.read_text(encoding="utf-8"))
issues = []
for run in data.get("runs", []):
    for r in run.get("results", []):
        if r.get("suppressions"):
            continue
        level = r.get("level", "warning")
        loc = (r.get("locations") or [{}])[0].get("physicalLocation", {})
        path = loc.get("artifactLocation", {}).get("uri", "?")
        line = loc.get("region", {}).get("startLine", 1)
        msg = r.get("message", {}).get("text", "").replace("\n", " ")
        issues.append((level, path, line, r.get("ruleId", "?"), msg))

issues.sort(key=lambda i: (LEVELS.get(i[0], LEVELS["warning"])[3], i[1], i[2]))

print(f"\n{BLD}===== RÉSUMÉ QODANA ====={RST}")
if not issues:
    print("Aucun problème détecté.")
else:
    for lvl, (label, color, _, _) in LEVELS.items():
        n = sum(1 for i in issues if i[0] == lvl)
        if n:
            print(f"  {color}{label:<8}{RST} {n}")
    print(f"  {BLD}TOTAL    {len(issues)}{RST}\n")
    for level, path, line, rule, msg in issues[:MAX_LINES]:
        label, color, _, _ = LEVELS.get(level, LEVELS["warning"])
        print(f"{color}[{label}]{RST} {path}:{line}")
        print(f"    {rule} - {msg[:160]}")
    if len(issues) > MAX_LINES:
        print(f"\n... et {len(issues) - MAX_LINES} autres (voir le rapport complet dans les artefacts)")

report = []
for level, path, line, rule, msg in issues:
    sev = LEVELS.get(level, LEVELS["warning"])[2]
    fp = hashlib.md5(f"{rule}|{path}|{line}|{msg}".encode()).hexdigest()
    report.append({
        "description": msg or rule,
        "check_name": rule,
        "fingerprint": fp,
        "severity": sev,
        "location": {"path": path, "lines": {"begin": line}},
    })
OUT.write_text(json.dumps(report), encoding="utf-8")