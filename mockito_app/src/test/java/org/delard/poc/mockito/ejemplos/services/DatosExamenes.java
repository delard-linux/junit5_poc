package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class DatosExamenes {

    public static final List<Examen> EXAMENES = Arrays.asList(
            new Examen(1L, "Matematicas"),
            new Examen(2L, "Lengua"),
            new Examen(3L, "Historia"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matematicas"),
            new Examen(null, "Lengua"),
            new Examen(null, "Historia"));

    public static final List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(
            new Examen(-1L, "Matematicas"),
            new Examen(-2L, "Lengua"),
            new Examen(-3L, "Historia"));

    public static final List<String> PREGUNTAS_MATEMATICAS = Arrays.asList(
            "aritmetica", "integrales", "trigonometria", "derivadas");

    public static final List<String> PREGUNTAS_GENERICAS = Arrays.asList(
            "pregunta 1", "pregunta 2", "pregunta 3", "pregunta 4");

    public static final Examen EXAMEN =  new Examen(8L, "Fisica");

}
