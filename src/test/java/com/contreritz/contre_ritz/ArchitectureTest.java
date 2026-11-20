// Généré par IA, c'est juste des règles pour pas faire de mauvaises pratiques

package com.contreritz.contre_ritz;

import com.contreritz.contre_ritz.model.User;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.base.DescribedPredicate.and;
import static com.tngtech.archunit.core.domain.JavaCall.Predicates.target;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameStartingWith;
import static com.tngtech.archunit.core.domain.properties.HasOwner.Predicates.With.owner;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

/**
 * Vérifie les règles de rangement du code, pour qu'elles ne dépendent pas de la relecture.
 * <p>
 * Chaque test correspond à une règle distincte : si l'un échoue, le message indique quelle classe
 * enfreint la règle et pourquoi. Seul le code de l'application est analysé, pas celui des tests.
 * </p>
 */
class ArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void importerLesClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.contreritz.contre_ritz");
    }

    /**
     * Règle : les repositories ne sont utilisés que par les services.
     * <p>
     * Pourquoi : un repository lit et écrit en base sans aucun contrôle. Si une vue, une configuration
     * ou n'importe quelle autre classe l'appelle directement, elle contourne les règles métier
     * portées par le service (vérification d'unicité, hachage du mot de passe, droits d'accès).
     * Une même règle risque alors d'être écrite à deux endroits, et un oubli la rend incohérente.
     * </p>
     */
    @Test
    void seulsLesServicesUtilisentLesRepositories() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyHaveDependentClassesThat().resideInAnyPackage("..service..", "..repository..")
                .because("l'accès à la base passe par un service (UserService, ...), qui porte les règles métier "
                        + "(vérifications, droits, hachage du mot de passe...) : une vue ou une configuration "
                        + "ne doit pas utiliser un repository directement")
                .check(classes);
    }

    /**
     * Règle : une vue n'appelle aucune méthode {@code set...} de {@link User}.
     * <p>
     * Pourquoi : modifier un utilisateur est de la logique métier (valeurs autorisées, mot de passe
     * déjà haché, droits). Une vue doit demander l'opération à {@code UserService}, pas changer
     * elle-même les champs de l'entité.
     * </p>
     */
    @Test
    void lesVuesNeModifientPasLesUtilisateurs() {
        noClasses()
                .that().resideInAPackage("..view..")
                .should().callMethodWhere(and(target(nameStartingWith("set")), target(owner(assignableTo(User.class)))))
                .because("modifier un utilisateur est de la logique métier : une vue appelle une méthode "
                        + "de UserService, elle ne change pas elle-même les champs de User")
                .check(classes);
    }

    /**
     * Règle : {@code service}, {@code model}, {@code repository} et {@code dto} ne dépendent pas de Vaadin.
     * <p>
     * Pourquoi : la logique métier et les entités doivent rester utilisables et testables sans
     * interface graphique (par exemple avec les tests unitaires de {@code UserServiceTest}).
     * Un service qui affiche une {@code Notification} ou manipule l'{@code UI} ne peut plus
     * être réutilisé ni testé seul.
     * </p>
     */
    @Test
    void lesCouchesMetierNeConnaissentPasVaadin() {
        noClasses()
                .that().resideInAnyPackage("..service..", "..model..", "..repository..", "..dto..")
                .should().dependOnClassesThat().resideInAPackage("com.vaadin..")
                .because("services et entités doivent rester utilisables et testables sans interface")
                .check(classes);
    }

    /**
     * Règle : pas d'injection par attribut ({@code @Autowired}, {@code @Inject}, {@code @Resource},
     * {@code @Value} sur un champ).
     * <p>
     * Pourquoi : avec l'injection par le constructeur, les dépendances sont visibles et la classe se
     * teste en lui passant simplement des objets (ou des faux avec Mockito), sans démarrer Spring.
     * </p>
     */
    @Test
    void pasDInjectionParChamp() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classes);
    }
}
