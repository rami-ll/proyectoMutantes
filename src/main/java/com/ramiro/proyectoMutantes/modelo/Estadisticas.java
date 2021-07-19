package com.ramiro.proyectoMutantes.modelo;

import org.springframework.stereotype.Service;

@Service
public class Estadisticas {

    private long totalMuestas;
    private long muestrasMutantes;
    private double ratio;


    public long getTotalMuestas() {
        return totalMuestas;
    }

    public void setTotalMuestas(long totalMuestas) {
        this.totalMuestas = totalMuestas;
    }

    public long getMuestrasMutantes() {
        return muestrasMutantes;
    }

    public void setMuestrasMutantes(long muestrasMutantes) {
        this.muestrasMutantes = muestrasMutantes;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio){
        this.ratio = ratio;
    }
}
