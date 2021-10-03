package org.delard.pocmockito.ejemplos.repositories;

import org.delard.pocmockito.ejemplos.DatosExamenes;
import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImplOtro implements ExamenRepository{

    @Override
    public Long save(Examen examen) {
        // TODO metodo mock
        return DatosExamenes.EXAMEN.getId();
    }

    @Override
    public List<Examen> findAll(){
        System.out.println("   *** Llamada a metodo real findAll");
        // TODO se ha incluido un sleep para la prueba
        try{
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        // TODO metodo mock
        return DatosExamenes.EXAMENES;
    }
}
