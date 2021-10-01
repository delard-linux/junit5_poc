package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepository;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.delard.pocmockito.ejemplos.repositories.PreguntasRepository;
import org.delard.pocmockito.ejemplos.services.ExamenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntasRepository preguntasRepository;

    @InjectMocks
    ExamenServiceImpl examenService;


    @BeforeEach
    void setUp() {
        //inyeccion de dependencias en Mockito, es necesaria la implementacion del service en vez de su interface
        MockitoAnnotations.openMocks(this);
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

        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);

        var examen = examenService.findExamenByNombre("Matematicas");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenByNombreConPreguntasMock (){

        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(1L)).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);

        var examen = examenService.findExamenByNombreWithPreguntas("Matematicas");
        var examen2 = examenService.findExamenByNombreWithPreguntas("Historia");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
        assertNotNull(examen.orElseThrow().getPreguntas());
        assertEquals(4, examen.orElseThrow().getPreguntas().size());
        assertTrue(examen.orElseThrow().getPreguntas().contains("aritmetica"));

        assertNotNull(examen2);
        assertEquals(3L, examen2.orElseThrow().getId());
        assertEquals("Historia", examen2.orElseThrow().getNombre());
        assertTrue(examen2.orElseThrow().getPreguntas().isEmpty());

    }

    @Test
    void findExamenByNombreConPreguntasGenericasMock (){

        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_GENERICAS);

        var examen = examenService.findExamenByNombreWithPreguntas("Matematicas");
        var examen2 = examenService.findExamenByNombreWithPreguntas("Historia");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
        assertNotNull(examen.orElseThrow().getPreguntas());
        assertEquals(4, examen.orElseThrow().getPreguntas().size());
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 1"));
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 3"));

        assertNotNull(examen2);
        assertEquals(3L, examen2.orElseThrow().getId());
        assertEquals("Historia", examen2.orElseThrow().getNombre());
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 1"));
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 3"));

    }

    @Test
    void findExamenByNombreConPreguntasGenericasVerifyMock (){

        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_GENERICAS);

        var examen = examenService.findExamenByNombreWithPreguntas("Matematicas");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
        assertNotNull(examen.orElseThrow().getPreguntas());
        assertEquals(4, examen.orElseThrow().getPreguntas().size());
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 1"));
        assertTrue(examen.orElseThrow().getPreguntas().contains("pregunta 3"));

        // verificar llamada a metodos del mock
        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(1L);

    }




}
