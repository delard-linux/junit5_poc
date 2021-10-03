package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepository;
import org.delard.pocmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.delard.pocmockito.ejemplos.repositories.PreguntasRepository;
import org.delard.pocmockito.ejemplos.services.ExamenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntasRepository preguntasRepository;

    @InjectMocks
    ExamenServiceImpl examenService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindExamenByNombre(){
        examenRepository = new ExamenRepositoryImpl();
        examenService = new ExamenServiceImpl(examenRepository,preguntasRepository);
        var examen = examenService.findExamenByNombre("Matematicas");
        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void testFindExamenByNombreListaVaciaMock(){
        List<Examen> datos = Collections.emptyList();

        when(examenRepository.findAll()).thenReturn(datos);

        var examen = examenService.findExamenByNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testFindExamenByNombreMock (){

        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);

        var examen = examenService.findExamenByNombre("Matematicas");

        assertNotNull(examen);
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void testFindExamenByNombreConPreguntasMock (){

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
    void testFindExamenByNombreConPreguntasGenericasMock (){

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
    void testFindExamenByNombreConPreguntasGenericasVerifyMock (){

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

    @Test
    void testGuardarMock () {
        // Se verifica que se llama a los servicios del repository por debajo unicamente
        Examen examenPrueba = DatosExamenes.EXAMEN;
        examenPrueba.setPreguntas(DatosExamenes.PREGUNTAS_GENERICAS);

        when(examenRepository.save(any(Examen.class))).thenReturn(8L);

        var examenId = examenService.saveExamen(examenPrueba);

        assertNotNull(examenId);
        assertEquals(8L, examenId);
        verify(examenRepository).save(any(Examen.class));
        verify(preguntasRepository).savePreguntas(anyList());

    }

    @Test
    void testGuardarAutoincrementalMock () {
        // Se verifica que se llama a los servicios del repository por debajo unicamente
        Examen examenPrueba = DatosExamenes.EXAMEN;
        examenPrueba.setPreguntas(DatosExamenes.PREGUNTAS_GENERICAS);

        when(examenRepository.save(any(Examen.class))).then(
                new Answer<Long>() {
                    Long secuencia = 7L;
                    @Override
                    public Long answer(InvocationOnMock invocationOnMock) {
                        return ++secuencia;
                    }
                }
        );

        var examenId = examenService.saveExamen(DatosExamenes.EXAMEN);

        assertNotNull(examenId);
        assertEquals(8L, examenId);
        verify(examenRepository).save(any(Examen.class));
        verify(preguntasRepository).savePreguntas(anyList());

    }
    @Test
    void testManejoException() {
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES_ID_NULL);
        when(preguntasRepository.findPreguntasByExamenId(isNull())).thenThrow(new IllegalArgumentException());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            examenService.findExamenByNombreWithPreguntas("Matematicas"));
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(isNull());

    }


}
