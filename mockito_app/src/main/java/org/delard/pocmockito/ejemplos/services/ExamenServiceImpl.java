package org.delard.pocmockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepository;
import org.delard.pocmockito.ejemplos.repositories.PreguntasRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private final ExamenRepository examenRepository;
    private final PreguntasRepository preguntasRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenByNombre(String nombre) {
        return examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Optional<Examen> findExamenByNombreWithPreguntas(String nombre) {

        var examenOptional = findExamenByNombre(nombre);
        var examen = examenOptional.orElseThrow();

        examen.setPreguntas(preguntasRepository.findPreguntasByExamenId(examen.getId()));

        return examenOptional;
    }
}
