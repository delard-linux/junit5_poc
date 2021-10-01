package org.delard.pocmockito.ejemplos.repositories;

import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
