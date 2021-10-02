package org.delard.pocmockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenByNombre(String nombre);

    Optional<Examen> findExamenByNombreWithPreguntas(String nombre);

    Long saveExamen(Examen examen);

}
