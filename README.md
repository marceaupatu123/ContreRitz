# Début du projet!

J'ai (Marceau) commencé à setup le projet, voici l'architecture du projet :

```
contre-ritz/
├── pom.xml                  → dépendances et config Maven
├── mvnw, .mvn/              → Maven wrapper
├── .gitignore               → fichiers que Git doit ignorer
└── src/
    ├── main/
    │   ├── java/            → TOUT notre code Java
    │   │   └── com/contreritz/contre_ritz/
    │   │       └── ContreRitzApplication.java   → point d'entrée (le main)
    │   └── resources/       → fichiers non-Java
    │       ├── application.properties           → configuration (BDD, port, etc.)
    │       └── static/      → fichiers statiques (images, CSS)
    └── test/
        └── java/            → nos tests, même structure de packages que main
```

