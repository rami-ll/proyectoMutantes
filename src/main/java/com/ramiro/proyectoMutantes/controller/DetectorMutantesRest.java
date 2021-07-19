package com.ramiro.proyectoMutantes.controller;


import com.ramiro.proyectoMutantes.modelo.DetectorDeMutantes;
import com.ramiro.proyectoMutantes.modelo.Estadisticas;
import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DetectorMutantesRest {

    @Autowired
    private DetectorDeMutantes detector;
    @Autowired
    private GenesRepository repository;
    @Autowired
    Estadisticas estadisticas;

    @RequestMapping(value = "/mutant", method = RequestMethod.POST)
    public ResponseEntity esMutante(@RequestBody Genes genes){
        boolean esMutante = this.detector.esMutante(genes);
        if (esMutante){
           return new ResponseEntity(HttpStatus.OK);
        } else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Estadisticas obtenerEstadisticas(){
        long muestras = repository.count();
        long muestrasMutantes = repository.findByEsMutante(true).size();
        estadisticas.setTotalMuestas(muestras);
        estadisticas.setMuestrasMutantes(muestrasMutantes);
        estadisticas.setRatio((double)muestrasMutantes/muestras);
        return estadisticas;
    }

}
