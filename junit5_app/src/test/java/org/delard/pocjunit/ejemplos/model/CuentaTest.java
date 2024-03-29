package org.delard.pocjunit.ejemplos.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import org.delard.pocjunit.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

class CuentaTest extends ClasePadreTest{

    Cuenta cuenta;

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter){
        this.cuenta = new Cuenta("David",new BigDecimal("2154.1234"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        testReporter.publishEntry(" ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());

    }

    @AfterEach
    void tearDown() {

        System.out.println("Finalizando el metodo");

    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el TEST");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el TEST");
    }

    @SuppressWarnings("ExcessiveLambdaUsage")
    @Test
    @Tag("cuenta")
    @DisplayName("Test nombre de la cuenta")
    void testNombreCuenta() {
        String expected = "David";
        String real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(expected, real,() -> "Nombre de cuenta incorrecto");
    }

    @Test
//    @Disabled("Incluido fail hardcode")
    @Tag("cuenta")
    @DisplayName("Test saldo de la cuenta")
    void testSaldoCuenta(){
//        fail();
        assertNotNull(cuenta.getSaldo());
        assertEquals(2154.1234,cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    @Tag("cuenta")
    @DisplayName("Test de cuentas iguales en objetos distintos")
    void testReferenciaCuenta(){
        Cuenta cuenta2 = new Cuenta("David",new BigDecimal("2154.1234"));
        assertEquals(cuenta,cuenta2);
    }

    @RepeatedTest(value=5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
    @Tag("cuenta")
    @DisplayName("Test de debito sobre cuentas repetido")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition() == 3){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal("100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2054, cuenta.getSaldo().intValue());
        assertEquals("2054.1234", cuenta.getSaldo().toPlainString());
        BigDecimal cantidadPorEncimaDeSaldo = new  BigDecimal("2100");
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(cantidadPorEncimaDeSaldo));
        assertEquals(DineroInsuficienteException.DINERO_INSUFICIENTE_MSG,ex.getMessage());
    }

    @Test
    @Tag("cuenta")
    @DisplayName("Test de debito sobre cuentas")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal("100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2054, cuenta.getSaldo().intValue());
        assertEquals("2054.1234", cuenta.getSaldo().toPlainString());
        BigDecimal cantidadPorEncimaDeSaldo = new  BigDecimal("2100");
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(cantidadPorEncimaDeSaldo));
        assertEquals(DineroInsuficienteException.DINERO_INSUFICIENTE_MSG,ex.getMessage());
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @ValueSource(strings = {"100", "300", "500", "1000", "2154.1234"})
    @Tag("cuenta")
    @DisplayName("Test de debito sobre cuentas parametrizado")
    void testDebitoCuentaParametrizado(String cantidad) {
        cuenta.debito(new BigDecimal(cantidad));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>=0);
    }

    @Test
    @DisplayName("Test de credito sobre cuentas")
    @Tag("cuenta")
    void testCredito(){
        cuenta.credito(new BigDecimal("100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2254, cuenta.getSaldo().intValue());
        assertEquals("2254.1234", cuenta.getSaldo().toPlainString());
        cuenta.credito(new BigDecimal("-100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2054, cuenta.getSaldo().intValue());
        assertEquals("2054.1234", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test de transferencia entre cuentas")
    @Tag("cuenta")
    @Tag("banco")
    void testTransferirDineroCuentas(){
//        System.out.println("ENTRO EN TEST DE BANCO");
        Cuenta cuenta1 = new Cuenta("John Doe",new BigDecimal("2500.1234"));
        Cuenta cuenta2 = new Cuenta("David RD",new BigDecimal("1000.1234"));
        Banco banco = new Banco();
        banco.setNombre("Caja de Ingenieros");
        banco.transferir(cuenta2,cuenta1,new BigDecimal(500));
        assertEquals("500.1234", cuenta2.getSaldo().toPlainString());
        assertEquals("3000.1234", cuenta1.getSaldo().toPlainString());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Tag("cuenta")
    @Tag("banco")
    @DisplayName("Test de relaciones entre cuentas")
    void testRelacionBancoCuentas(){
//        System.out.println("ENTRO EN TEST DE BANCO");
        Cuenta cuenta1 = new Cuenta("John Doe",new BigDecimal("2500.1234"));
        Cuenta cuenta2 = new Cuenta("David RD",new BigDecimal("1000.1234"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Caja de Ingenieros");
        assertAll( () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Caja de Ingenieros", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Caja de Ingenieros", cuenta2.getBanco().getNombre()),
                () -> assertEquals("David RD",
                        banco.getCuentas().stream()
                                .filter(c -> c.getPersona().equals("David RD"))
                                .findFirst()
                                .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("John Doe")))
                 );
    }

    @Test
    @Tag("cuenta")
    @DisplayName("Test saldo de la cuenta, entorno dev")
    void testSaldoCuentaConAssumption(){
        assumingThat(System.getProperty("ENV") != null
                && System.getProperty("ENV").equals("dev"),
                    () -> {
                        assertNotNull(cuenta.getSaldo());
                        assertEquals(2154.1234,cuenta.getSaldo().doubleValue());
                    });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }
}