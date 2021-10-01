package org.delard.pocmockito.ejemplos.repositories;

import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public List<Examen> findAll() {
        return Arrays.asList(
                new Examen(1L, "Matematicas"),
                new Examen(2L, "Lengua"),
                new Examen(3L, "Historia"),
                new Examen(4L, "Ingles"));
    }
}
