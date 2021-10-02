package org.delard.pocmockito.ejemplos.repositories;

import java.util.List;

public interface PreguntasRepository {

    List<String> findPreguntasByExamenId(Long examenId);

    void savePreguntas(List<String> listaPreguntas);

}
