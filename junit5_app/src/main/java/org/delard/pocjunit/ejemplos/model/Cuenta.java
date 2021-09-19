package org.delard.pocjunit.ejemplos.model;

import org.delard.pocjunit.ejemplos.exception.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public void debito(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO)<0)
            credito(cantidad.abs());
        if(this.saldo.subtract(cantidad).compareTo(BigDecimal.ZERO)<0)
            throw new DineroInsuficienteException(DineroInsuficienteException.DINERO_INSUFICIENTE_MSG);
        setSaldo(this.saldo.subtract(cantidad));
    }

    public void credito(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO)<0)
            debito(cantidad.abs());
        setSaldo(this.saldo.add(cantidad));
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cuenta)
                || getPersona()==null
                || getSaldo()==null)
            return false;
        Cuenta c = (Cuenta)obj;
        return (getPersona().equals(c.getPersona())
            && getSaldo().equals(c.getSaldo()));
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((getPersona() == null) ? 0 : getPersona().hashCode());
        result = prime * result + ((getSaldo() == null) ? 0 : getSaldo().hashCode());
        return result;
    }

}
