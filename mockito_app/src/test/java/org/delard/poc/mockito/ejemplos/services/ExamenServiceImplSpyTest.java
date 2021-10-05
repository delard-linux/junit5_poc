package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.delard.pocmockito.ejemplos.repositories.PreguntasRepositoryImpl;
import org.delard.pocmockito.ejemplos.services.ExamenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl repository;

    @Spy
    PreguntasRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Test
    void testSpy() {
        // test spy pero con anotaciones
        List<String> preguntas = Arrays.asList("aritmética", "trigonometría");
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas").orElseThrow();
        assertEquals(1, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(2, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }
}