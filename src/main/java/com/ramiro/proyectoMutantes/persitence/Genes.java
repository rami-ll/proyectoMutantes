package com.ramiro.proyectoMutantes.persitence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "genes")
public class Genes implements Serializable {

    @Id
    private String id;

    private ArrayList<String> dna;

    private boolean esMutante;

    public ArrayList<String> getDna() {
        return dna;
    }

    public void setDna(ArrayList<String> dna) {
        this.dna = dna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEsMutante() {
        return esMutante;
    }

    public void setEsMutante(boolean esMutante) {
        this.esMutante = esMutante;
    }
}
