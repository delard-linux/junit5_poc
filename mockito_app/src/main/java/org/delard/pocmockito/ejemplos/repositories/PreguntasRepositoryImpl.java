package org.delard.pocmockito.ejemplos.repositories;

import org.delard.pocmockito.ejemplos.DatosExamenes;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntasRepositoryImpl implements PreguntasRepository{
    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        System.out.println("   *** Llamada a metodo real findPreguntasByExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        // TODO document why this method is mock
        return DatosExamenes.PREGUNTAS_GENERICAS;
    }

    @Override
    public void savePreguntas(List<String> preguntas) {
        // TODO document why this method is empty
    }
}
