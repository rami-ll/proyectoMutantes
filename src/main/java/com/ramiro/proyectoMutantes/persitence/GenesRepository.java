package com.ramiro.proyectoMutantes.persitence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface GenesRepository extends MongoRepository<Genes, Serializable> {

    public Genes findByDna(ArrayList<String> dna);
    public List<Genes> findByEsMutante(boolean esMutante);
}
