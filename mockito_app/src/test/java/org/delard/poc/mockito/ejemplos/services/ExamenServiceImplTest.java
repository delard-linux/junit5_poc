package org.delard.poc.mockito.ejemplos.services;

import org.delard.pocmockito.ejemplos.DatosExamenes;
import org.delard.pocmockito.ejemplos.models.Examen;
import org.delard.pocmockito.ejemplos.repositories.*;
import org.delard.pocmockito.ejemplos.services.ExamenService;
import org.delard.pocmockito.ejemplos.services.ExamenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImplOtro examenRepository;
    @Mock
    PreguntasRepositoryImpl preguntasRepository;

    @InjectMocks
    ExamenServiceImpl examenService;

    @Captor
    ArgumentCaptor<Long> captorId;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindExamenByNombre(){
        examenRepository = new ExamenRepositoryImplOtro();
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
        // Se verifica que cuando el examen Matematicas tiene un Id nulo se devuelva una excepcion
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES_ID_NULL);
        when(preguntasRepository.findPreguntasByExamenId(isNull())).thenThrow(new IllegalArgumentException());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            examenService.findExamenByNombreWithPreguntas("Matematicas"));
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(isNull());

    }

    @Test
    void testArgumentMatchers() {
        // Se verifica que cuando se busca el examen de Matematicas con sus preguntas se le pasa el Id 1 con diferentes alternativas con ArgumentMatchers
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(examenRepository).findAll();

        verify(preguntasRepository).findPreguntasByExamenId(argThat(arg -> arg != null && arg.equals(1L)));
        verify(preguntasRepository).findPreguntasByExamenId(argThat(arg -> arg != null && arg >= 1L));
        //verify(preguntasRepository).findPreguntasByExamenId(eq(1L));

    }

    @Test
    void testArgumentMatchersPersonalizado() {
        // Se verifica que cuando se busca el examen de Matematicas con sus preguntas se le pasa el Id 1 recibido por Matematicas
        // y se verifica con un ArgumentMatcher personalizado, esta personalización lleva tb mensaje personalizado
        // si se le pasa el juego de datos EXAMENES_ID_NEGATIVOS falla
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(argThat(new MiArgsMatchers()));

    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long idExamen) {
            this.argument = idExamen;
            return idExamen != null && idExamen > 0;
        }

        @Override
        public String toString() {
            return "Mensaje personalizado de error " +
                    "que imprime mockito en caso de que falle el test con el argumento " +
                    this.argument + " por no ser un entero positivo";
        }
    }

    @Test
    void testArgumentMatchersPersonalizadoLambda() {
        // Se verifica que cuando se busca el examen de Matematicas con sus preguntas se le pasa el Id 1 recibido por Matematicas
        // y se verifica con un ArgumentMatcher personalizado con lambdas sin mensaje personalizado
        // si se le pasa el juego de datos EXAMENES_ID_NEGATIVOS falla
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(argThat(argumento -> argumento != null && argumento > 0 ));

    }

    @Test
    void testArgumentCaptor() {
        // Se verifica que cuando se busca el examen de Matematicas con sus preguntas se le pasa el Id 1 recibido por Matematicas
        // la verificación se hace capturando el argumento
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntasRepository).findPreguntasByExamenId(captor.capture());
        assertEquals(1L, captor.getValue());

    }

    @Test
    void testArgumentCaptorAnotacion() {
        // Se verifica que cuando se busca el examen de Matematicas con sus preguntas se le pasa el Id 1 recibido por Matematicas
        // la verificación se hace capturando el argumento
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        when(preguntasRepository.findPreguntasByExamenId(anyLong())).thenReturn(DatosExamenes.PREGUNTAS_MATEMATICAS);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(preguntasRepository).findPreguntasByExamenId(captorId.capture());
        assertEquals(1L, captorId.getValue());

    }

    @Test
    void testDoThrow() {
        // Comprobar que la llamada a un metodo mock que devuelve void está fallando
        // en vez de usar el when hay que usar la sintaxis doThrow ... when
        Examen examen = DatosExamenes.EXAMEN;
        examen.setPreguntas(DatosExamenes.PREGUNTAS_GENERICAS);
        doThrow(IllegalArgumentException.class).when(preguntasRepository).savePreguntas(anyList());
        assertThrows(IllegalArgumentException.class, () -> examenService.saveExamen(examen));

    }

    @Test
    void testDoAnswer() {
        // Test para verificar que dependiendo del argumento se genera un u otro mock de preguntas
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L? DatosExamenes.PREGUNTAS_MATEMATICAS: DatosExamenes.PREGUNTAS_GENERICAS;
        }).when(preguntasRepository).findPreguntasByExamenId(anyLong());

        Examen examen = examenService.findExamenByNombreWithPreguntas("Matematicas").orElseThrow();
        assertEquals(4, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        // la primera vez que se llama...
        verify(preguntasRepository).findPreguntasByExamenId(anyLong());

        Examen examen2 = examenService.findExamenByNombreWithPreguntas("Historia").orElseThrow();
        assertEquals(4, examen2.getPreguntas().size());
        assertTrue(examen2.getPreguntas().contains("pregunta 1"));

    }

    @Test
    void testDoAnswerGuardarExamen() {
        // Test para verificar que intercepta el argumento y devuelve el ID a traves de una secuencia que post incrementa
        // Given
        Examen newExamen = DatosExamenes.EXAMEN;
        newExamen.setPreguntas(DatosExamenes.PREGUNTAS_GENERICAS);

        doAnswer(new Answer<Long>(){
            Long secuencia = 8L;
            @Override
            public Long answer(InvocationOnMock invocation) {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen.getId();
            }
        }).when(examenRepository).save(any(Examen.class));

        // When
        Long idExamen = examenService.saveExamen(newExamen);

        // Then
        assertNotNull(idExamen);
        assertEquals(8L, idExamen);

        verify(examenRepository).save(any(Examen.class));
        verify(preguntasRepository).savePreguntas(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        // Se verifica que en el servicio uno de los dos metodos se usa mock y el segundo es real
        // El mock es el findAll de los examenes pero la lista de preguntas por examen es real
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES_TOPOGRAFIA);
        doCallRealMethod().when(preguntasRepository).findPreguntasByExamenId(anyLong());
        Examen examen = examenService.findExamenByNombreWithPreguntas("Teledeteccion").orElseThrow();
        assertEquals(5L, examen.getId());
        assertEquals("Teledeteccion", examen.getNombre());

    }

    @Test
    void testSpy() {
        // Con Spy se verifica algun metodo concreto pero el resto es real,
        // se tienen que usar implementaciones, no se puede usar el interfaz
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntasRepository preguntaRepository = spy(PreguntasRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("aritmetica 1", "algebra 2", "geometria 3");
        // este no lo usamos ya que implica llamar al preguntas repository, para ello usamos el siguiente
        //when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = examenService.findExamenByNombreWithPreguntas("Matematicas").orElseThrow();
        assertEquals(1, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(3, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica 1"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testOrdenDeInvocaciones() {
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);

        examenService.findExamenByNombreWithPreguntas("Matematicas");
        examenService.findExamenByNombreWithPreguntas("Lengua");

        InOrder inOrder = inOrder(preguntasRepository);
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(1L);
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(2L);

    }

    @Test
    void testOrdenDeInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);

        examenService.findExamenByNombreWithPreguntas("Matematicas");
        examenService.findExamenByNombreWithPreguntas("Lengua");

        InOrder inOrder = inOrder(examenRepository, preguntasRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(1L);

        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasByExamenId(2L);

    }

    @Test
    void testNumeroDeInvocaciones() {
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(preguntasRepository).findPreguntasByExamenId(1L);
        verify(preguntasRepository, times(1)).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atLeast(1)).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atLeastOnce()).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atMost(1)).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atMostOnce()).findPreguntasByExamenId(1L);
    }

    @Test
    void testNumeroDeInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(DatosExamenes.EXAMENES);
        examenService.findExamenByNombreWithPreguntas("Matematicas");

//        verify(preguntaRepository).findPreguntasPorExamenId(5L); falla
        verify(preguntasRepository, times(2)).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atLeast(2)).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atLeastOnce()).findPreguntasByExamenId(1L);
        verify(preguntasRepository, atMost(20)).findPreguntasByExamenId(1L);
//        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L); falla
    }

    @Test
    void testNumeroInvocaciones3() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        examenService.findExamenByNombreWithPreguntas("Matematicas");

        verify(preguntasRepository, never()).findPreguntasByExamenId(1L);
        verifyNoInteractions(preguntasRepository);

        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll();
        verify(examenRepository, atLeast(1)).findAll();
        verify(examenRepository, atLeastOnce()).findAll();
        verify(examenRepository, atMost(10)).findAll();
        verify(examenRepository, atMostOnce()).findAll();
    }

}
