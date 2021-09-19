package org.delard.pocjunit.ejemplos.model;

import static org.junit.jupiter.api.Assertions.*;

import org.delard.pocjunit.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CuentaTest {

    @Test
    void testNombreCuenta() {

        Cuenta cuenta = new Cuenta("David",new BigDecimal("2154.124"));
        String expected = "David";
        String real = cuenta.getPersona();

        assertNotNull(real);
        assertEquals(expected, real);

    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("David",new BigDecimal("2154.124"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(2154.124,cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    void testReferenciaCuenta(){
        Cuenta cuenta = new Cuenta("John Doe",new BigDecimal("2154.124"));
        Cuenta cuenta2 = new Cuenta("John Doe",new BigDecimal("2154.124"));
        assertEquals(cuenta,cuenta2);
    }

    @Test
    void testDebitoCuenta(){
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));

        cuenta.debito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.1234", cuenta.getSaldo().toPlainString());

        BigDecimal cantidadPorEncimaDeSaldo = new  BigDecimal("1100");
        Exception ex = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(cantidadPorEncimaDeSaldo));
        assertEquals(DineroInsuficienteException.DINERO_INSUFICIENTE_MSG,ex.getMessage());

    }

    @Test
    void testCredito(){
        Cuenta cuenta = new Cuenta("John Doe",new BigDecimal("1000.1234"));

        cuenta.credito(new BigDecimal("100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.1234", cuenta.getSaldo().toPlainString());

        cuenta.credito(new BigDecimal("-100"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.1234", cuenta.getSaldo().toPlainString());


    }

    @Test
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












}