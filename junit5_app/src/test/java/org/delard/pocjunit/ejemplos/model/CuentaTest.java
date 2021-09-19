package org.delard.pocjunit.ejemplos.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import org.delard.pocjunit.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(){
        System.out.println("Iniciando el metodo");
        this.cuenta = new Cuenta("David",new BigDecimal("2154.1234"));
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
    @DisplayName("Test nombre de la cuenta")
    void testNombreCuenta() {
        String expected = "David";
        String real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(expected, real,() -> "Nombre de cuenta incorrecto");
    }

    @Test
//    @Disabled("Incluido fail hardcode")
    @DisplayName("Test saldo de la cuenta")
    void testSaldoCuenta(){
//        fail();
        assertNotNull(cuenta.getSaldo());
        assertEquals(2154.1234,cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    @DisplayName("Test de cuentas iguales en objetos distintos")
     void testReferenciaCuenta(){
        Cuenta cuenta2 = new Cuenta("David",new BigDecimal("2154.1234"));
        assertEquals(cuenta,cuenta2);
    }

    @Test
    @DisplayName("Test de debito sobre cuentas")
    void testDebitoCuenta(){
        cuenta.debito(new BigDecimal("100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2054, cuenta.getSaldo().intValue());
        assertEquals("2054.1234", cuenta.getSaldo().toPlainString());
        BigDecimal cantidadPorEncimaDeSaldo = new  BigDecimal("2100");
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(cantidadPorEncimaDeSaldo));
        assertEquals(DineroInsuficienteException.DINERO_INSUFICIENTE_MSG,ex.getMessage());
    }

    @Test
    @DisplayName("Test de credito sobre cuentas")
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
    void testTransferirDineroCuentas(){
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
    @DisplayName("Test de relaciones entre cuentas")
    void testRelacionBancoCuentas(){
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
    @DisplayName("Test saldo de la cuenta, entorno dev")
    void testSaldoCuentaConAssumption(){
        assumeTrue(System.getProperty("ENV") != null
                && System.getProperty("ENV").equals("dev"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2154.1234,cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }



}