## Début du projet !

J'ai (Marceau) terminé le setup initial (Spring Boot + Vaadin + H2).
Voici l'architecture actuelle du projet et où ranger notre code :
```
ContreRitz/
├── pom.xml                                  → dépendances (Spring, Vaadin, H2...) et config Maven
├── mvnw, mvnw.cmd, .mvn/                    → Maven wrapper (permet de lancer le projet sans installer Maven)
├── .gitignore                               → fichiers locaux ignorés par Git (target/, data/, generated/...)
└── src/
    ├── main/
    │   ├── java/                            → TOUT notre code Java
    │   │   └── com/contreritz/contre_ritz/
    │   │       ├── ContreRitzApplication.java → point d'entrée de l'app (le main à lancer)
    │   │       └── view/                    → pages et composants Vaadin (le frontend)
    │   │           └── MainView.java        → page d'accueil actuelle (Hello World)
    │   ├── resources/                       → fichiers de config et ressources non-Java
    │   │   ├── application.properties       → config BDD H2 (jdbc:h2:file:./data/contreritzdb) et console web
    │   │   └── static/                      → fichiers statiques (logo, images, favicons...)
    │   └── frontend/generated/              → généré automatiquement par Vaadin (ne pas toucher !)
    └── test/
        └── java/                            → nos futurs tests unitaires (même structure de packages que main)
```


## Comment on travaille

Toute l'organisation se passe sur [GitLab](https://gitlabense.imtbs-tsp.eu/marceau.patureaux/ContreRitz). Le suivi est individuel (PRO3600, §8.3) : chacun doit toujours avoir au moins une issue ouverte à son nom.

### Les issues

- Une issue = une fonctionnalité complète (backend **et** front), avec sa checklist et ses dépendances (« Dépend de : #… »).
- **Jalons** : Jalon 1 (socle et authentification), 2 (portefeuille et repas), 3 (réservation et modération), 4 (finitions et démo), Bonus, et « Livrables et soutenance ». Un jalon se ferme à la main quand toutes ses issues sont terminées.
- **Labels** : `domaine::…` (le sujet), `priorité::haute/moyenne/basse` (l'ordre à suivre), `bonus` (optionnel), `livrable`. Le statut se suit avec `En cours`, `En attente de MR`, `Besoin d'aide !`.
- Les issues `[Équipe]` (réunions, autoformation) sont là pour tracer le temps hors code.

### Du début à la fin d'une tâche

1. **Choisis** une issue du jalon en cours, par ordre de priorité, vérifie ses dépendances, assigne-la à toi et mets le label `En cours`.
2. **Crée la branche** depuis l'issue avec le bouton « Create merge request ». La branche sera du type `16-auth-securite-connexion-inscription` et la MR contiendra `Closes #16`. Une branche = une seule personne.
3. **Code** back et front dans la même branche, avec la Javadoc et les tests de ta fonctionnalité.
4. **Ouvre la MR** avec le modèle : remplis « Ce que fait cette MR » et « Comment tester ». Garde `Draft:` tant que ce n'est pas fini, puis passe l'issue en `En attente de MR`.
5. **Pipeline** : les tests Maven et Qodana doivent être verts.
6. **Relecture** : quelqu'un d'autre relit, clique sur **Approve**, puis relance le job `approval-check` (bouton ▶). Sans ça, le pipeline reste rouge.
7. **Merge** : on coche *Squash commits* (si la branche n'a qu'un auteur) et *Delete source branch*, on édite le message du commit, puis on merge. L'issue se ferme toute seule.

### Règles

- Tout usage de l'IA doit être signalé précisemment en commentaires !

### Suivre son temps

Le temps se note à la main, à la fin de chaque séance, dans un commentaire de l'issue sur laquelle tu as travaillé. Chacun le fait avec **son propre compte** :

```
/spend 1h30m
Fini l'entité User et le service d'inscription
```

La première ligne enregistre le temps, la seconde est le commentaire d'une ligne qui résume ton apport. Les estimations de départ se posent avec `/estimate 8h`.

Les réunions et l'apprentissage (Vaadin, Spring, JPA) se notent dans les issues `[Équipe]`. Toute activité doit apparaître dans une issue.

### Réunions

Un compte-rendu par réunion, déposé dans `docs/comptes-rendus/CR-AAAA-MM-JJ.md`. La personne qui le rédige prévient les autres de son ajout.

### Dates à retenir

| Échéance | Date |
|---|---|
| Jalon 1 – Socle et authentification | 4 décembre 2026 |
| Jalon 2 – Portefeuille et repas | 18 décembre 2026 |
| Livrable 1 : pré-rapport | avant le 26 février 2027 |
| Livrable 2 : prototype | avant le 2 avril 2027 |
| Livrable 3 : logiciel, tests, rapport | avant le 21 mai 2027 |
| Soutenances | 10 juin (après-midi) et 11 juin 2027 |


## Comment on travaille

Toute l'organisation se passe sur [GitLab](https://gitlabense.imtbs-tsp.eu/marceau.patureaux/ContreRitz). Le suivi est individuel (PRO3600, §8.3) : chacun doit toujours avoir au moins une issue ouverte à son nom.

### Les issues

- Une issue = une fonctionnalité complète (backend **et** front), avec sa checklist et ses dépendances (« Dépend de : #… »).
- **Jalons** : Jalon 1 (socle et authentification), 2 (portefeuille et repas), 3 (réservation et modération), 4 (finitions et démo), Bonus, et « Livrables et soutenance ». Un jalon se ferme à la main quand toutes ses issues sont terminées.
- **Labels** : `domaine::…` (le sujet), `priorité::haute/moyenne/basse` (l'ordre à suivre), `bonus` (optionnel), `livrable`. Le statut se suit avec `En cours`, `En attente de MR`, `Besoin d'aide !`.
- Les issues `[Équipe]` (réunions, autoformation) sont là pour tracer le temps hors code.

### Du début à la fin d'une tâche

1. **Choisis** une issue du jalon en cours, par ordre de priorité, vérifie ses dépendances, assigne-la à toi et mets le label `En cours`.
2. **Crée la branche** depuis l'issue avec le bouton « Create merge request ». La branche sera du type `16-auth-securite-connexion-inscription` et la MR contiendra `Closes #16`. Une branche = une seule personne.
3. **Code** back et front dans la même branche, avec la Javadoc et les tests de ta fonctionnalité.
4. **Ouvre la MR** avec le modèle : remplis « Ce que fait cette MR » et « Comment tester ». Garde `Draft:` tant que ce n'est pas fini, puis passe l'issue en `En attente de MR`.
5. **Pipeline** : les tests Maven et Qodana doivent être verts.
6. **Relecture** : quelqu'un d'autre relit, clique sur **Approve**, puis relance le job `approval-check` (bouton ▶). Sans ça, le pipeline reste rouge.
7. **Merge** : on coche *Squash commits* (si la branche n'a qu'un auteur) et *Delete source branch*, on édite le message du commit, puis on merge. L'issue se ferme toute seule.

### Règles

- Tout usage de l'IA doit être signalé précisemment en commentaires !

### Suivre son temps

Le temps se note à la main, à la fin de chaque séance, dans un commentaire de l'issue sur laquelle tu as travaillé. Chacun le fait avec **son propre compte** :

```
/spend 1h30m
Fini l'entité User et le service d'inscription
```

La première ligne enregistre le temps, la seconde est le commentaire d'une ligne qui résume ton apport. Les estimations de départ se posent avec `/estimate 8h`. 

Les réunions et l'apprentissage (Vaadin, Spring, JPA) se notent dans les issues `[Équipe]`. Toute activité doit apparaître dans une issue.

### Réunions

Un compte-rendu par réunion, déposé dans `docs/comptes-rendus/CR-AAAA-MM-JJ.md`. La personne qui le rédige prévient les autres de son ajout.

### Dates à retenir

| Échéance | Date |
|---|---|
| Jalon 1 – Socle et authentification | 4 décembre 2026 |
| Jalon 2 – Portefeuille et repas | 18 décembre 2026 |
| Livrable 1 : pré-rapport | avant le 26 février 2027 |
| Livrable 2 : prototype | avant le 2 avril 2027 |
| Livrable 3 : logiciel, tests, rapport | avant le 21 mai 2027 |
| Soutenances | 10 juin (après-midi) et 11 juin 2027 |
