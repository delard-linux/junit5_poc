package org.delard.pocjunit.ejemplos.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalPoCTest {


    @Nested
    @DisplayName("Test Sistema Operativo")
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows(){
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac(){
            fail();
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows(){

        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloJdk8(){

        }

    }



    @Test
    @EnabledOnJre(JRE.JAVA_16)
    void testSoloJdk16(){

    }

    @Test
    void imprimirSystemProperties(){
        System.getProperties().forEach( (k,v) -> System.out.println(k + " = " + v));
    }

    @Test
    @EnabledIfSystemProperty(named="java.version",matches = "16.[0-9]+.[0-9]+")
    void testSiJdkEsAlgunaVersion16(){
    }

    @Test
    void imprimirVariablesEntorno(){
        System.getenv().forEach( ( (k,v) -> System.out.println(k + " = " + v) ));
    }

    @Test
    @EnabledIfEnvironmentVariable(named="LANG",matches = "en_US.UTF-8")
    void testSiLanguageEnUS(){
    }

}