package org.delard.pocmockito.ejemplos.repositories;

import org.delard.pocmockito.ejemplos.models.Examen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImplOtro implements ExamenRepository{

    @Override
    public Long save(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> findAll(){
        try{
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        // TODO document why this method is mock
        return new ArrayList<>();
    }
}
