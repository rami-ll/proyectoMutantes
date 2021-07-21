package com.ramiro.proyectoMutantes.modelo;

import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Single;

@Service
public class EstadisticasService {

    @Autowired
    private GenesRepository repository;
    @Autowired
    private Estadisticas estadisticas;

    public Single<Estadisticas> getEstadisitcasService(){
        return Single.create(
                singleSubscriber -> {
                    estadisticas.setTotalMuestas(repository.count());
                    estadisticas.setMuestrasMutantes(repository.findByEsMutante(true).size());
                    if (estadisticas.getTotalMuestas() > 0) {
                        estadisticas.setRatio((double) estadisticas.getMuestrasMutantes() / estadisticas.getTotalMuestas());
                    }
                    singleSubscriber.onSuccess(estadisticas);
                }
        );
    }
}
