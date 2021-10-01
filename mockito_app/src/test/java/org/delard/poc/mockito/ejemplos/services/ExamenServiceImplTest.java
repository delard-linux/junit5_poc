package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepository;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.delard.pocmockito.ejemplos.repositories.PreguntasRepository;
import org.delard.pocmockito.ejemplos.services.ExamenService;
import org.delard.pocmockito.ejemplos.services.ExamenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    ExamenRepository examenRepository;
    PreguntasRepository preguntasRepository;
    ExamenService examenService;


    @BeforeEach
    void setUp() {
        examenRepository = mock(ExamenRepository.class);
        preguntasRepository = mock(PreguntasRepository.class);
        examenService = new ExamenServiceImpl(examenRepository,preguntasRepository);
    }

    @Test
    void findExamenByNombre(){
        examenRepository = new ExamenRepositoryImpl();
        examenService = new ExamenServiceImpl(examenRepository,preguntasRepository);
        var examen = examenService.findExamenByNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenByNombreListaVaciaMock(){
        List<Examen> datos = Collections.emptyList();

        when(examenRepository.findAll()).thenReturn(datos);

        var examen = examenService.findExamenByNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    void findExamenByNombreMock (){

        var datos = Arrays.asList(
                new Examen(1L, "Matematicas"),
                new Examen(2L, "Lengua"),
                new Examen(3L, "Historia"));

        when(examenRepository.findAll()).thenReturn(datos);

        var examen = examenService.findExamenByNombre("Matematicas");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

}
