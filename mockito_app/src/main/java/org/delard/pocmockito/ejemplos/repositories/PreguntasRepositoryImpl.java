package org.delard.pocmockito.ejemplos.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntasRepositoryImpl implements PreguntasRepository{
    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        // TODO document why this method is mock
        return new ArrayList<>();
    }

    @Override
    public void savePreguntas(List<String> preguntas) {
        // TODO document why this method is empty
    }
}
