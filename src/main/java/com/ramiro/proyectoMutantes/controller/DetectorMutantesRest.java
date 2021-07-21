package com.ramiro.proyectoMutantes.controller;


import com.ramiro.proyectoMutantes.modelo.DetectorDeMutantes;
import com.ramiro.proyectoMutantes.modelo.Estadisticas;
import com.ramiro.proyectoMutantes.modelo.EstadisticasService;
import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rx.Single;
import rx.schedulers.Schedulers;

import java.net.URI;
import java.util.zip.DataFormatException;

@RestController
public class DetectorMutantesRest {

    @Autowired
    private DetectorDeMutantes detector;
    @Autowired
    EstadisticasService estadisticas;

    @RequestMapping(value = "/mutant",
            method = RequestMethod.POST)
    public Single<ResponseEntity> esMutante(@RequestBody Genes genes){
            return this.detector.esMutanteService(genes).subscribeOn(
                    Schedulers.io());

    }

    @RequestMapping(
            value = "/stats",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Single<Estadisticas> obtenerEstadisticas(){
        return this.estadisticas.getEstadisitcasService().subscribeOn(
                Schedulers.io());
    }

}
