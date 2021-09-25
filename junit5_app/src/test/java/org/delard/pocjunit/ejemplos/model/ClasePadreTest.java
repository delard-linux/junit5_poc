package org.delard.pocjunit.ejemplos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

public class ClasePadreTest{

    @BeforeEach
    void initMetodoTest(TestInfo testInfo){
        System.out.println("Iniciando el metodo: " + testInfo.getDisplayName() + " " + testInfo.getTags());
    }

}
