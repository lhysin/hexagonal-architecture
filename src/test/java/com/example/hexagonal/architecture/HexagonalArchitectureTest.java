package com.example.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("헥사고날 아키텍처 규칙 테스트")
class HexagonalArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter().importPackages("com.example.hexagonal");
    }

    @Test
    @DisplayName("domain 계층은 spring 패키지에 의존하지 않아야 한다")
    void shouldNotDependOnSpringInDomainLayer() {
        noClasses()
                .that().resideInAnyPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
                .check(classes);
    }

    @Test
    @DisplayName("application 계층은 adapter 계층 구현에 의존하지 않아야 한다")
    void shouldNotDependOnAdapterInApplicationLayer() {
        noClasses()
                .that().resideInAnyPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
                .check(classes);
    }
}
