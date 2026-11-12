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
