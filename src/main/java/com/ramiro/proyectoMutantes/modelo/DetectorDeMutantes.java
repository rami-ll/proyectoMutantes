package com.ramiro.proyectoMutantes.modelo;


import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
public class DetectorDeMutantes {

    private final String  componentesValidos = "[A,T,C,G]";
    private ArrayList<Pattern> genesMutantes;
    private Pattern pattern;
    @Autowired
    private GenesRepository repository;


    public DetectorDeMutantes(){
        this.pattern = Pattern.compile(this.componentesValidos);
        this.genesMutantes = new ArrayList<>();
        this.genesMutantes.add(Pattern.compile("[A]{4,}"));
        this.genesMutantes.add(Pattern.compile("[T]{4,}"));
        this.genesMutantes.add(Pattern.compile("[C]{4,}"));
        this.genesMutantes.add(Pattern.compile("[G]{4,}"));
    }

    public boolean esMutante(Genes genes){
        char[][] matrizGenes;
        ArrayList<String> muestra = genes.getDna();
        if (inputValido(muestra)){
            matrizGenes = generarMatriz(muestra);
            genes.setEsMutante(validacionDiagonal(matrizGenes) || validacionFilas(matrizGenes) || validacionLienas(muestra));
            this.guardarMuestra(genes);
            return genes.isEsMutante();
        }
        else{
            return false;
        }
    }

    private boolean inputValido(ArrayList<String> genes){
        boolean cumple;
        cumple = genes.get(0).length() > 3;
        cumple = cumple && (genes.size() == genes.get(0).length());
        cumple = (cumple && genes.stream().allMatch(componente -> pattern.matcher(componente).find()));
        return cumple;
    }

    private char[][] generarMatriz(ArrayList<String> genes){
        char[][] matriz = new char[genes.size()][genes.size()];
        int linea = 0;
        for (String componente : genes){
            for (int fila = 0; fila < componente.length(); fila++){
                matriz[linea][fila] = componente.charAt(fila);
            }
            linea++;
        }
        return matriz;
    }

    private boolean validacionDiagonal(char[][] genes){
        ArrayList<String> cadenasDiagonales =new ArrayList<>();
        String genesDiagonales = "";
        int cantidadMinima = 4;
        int largo = genes.length;
        //Diagonal principal
        for (int i = 0; i < largo; i++){
            genesDiagonales = genesDiagonales + genes[i][i];
        }
        cadenasDiagonales.add(genesDiagonales);
        genesDiagonales = "";
        //Diagonales superiores
        for (int i = 1; largo - i >= cantidadMinima; i++){
            for (int j = 0; j + i < largo; j++){
                genesDiagonales = genesDiagonales + genes[j][j+i];
            }
            cadenasDiagonales.add(genesDiagonales);
            genesDiagonales = "";
        }
        //Diagonales inferiores
        for (int i = 1; largo - i >= cantidadMinima; i++){
            for (int j = 0; j + i < largo; j++){
                genesDiagonales = genesDiagonales + genes[j+i][j];
            }
            cadenasDiagonales.add(genesDiagonales);
            genesDiagonales = "";
        }
        return cadenasDiagonales.stream().anyMatch(gen -> validarCadenaGenes(gen));
    }

    private boolean validacionFilas(char[][] genes){
        ArrayList<String> componetesHorizontales = new ArrayList<>();
        for (int i = 0; i < genes.length; i++){
            String componenteHorizontal = "";
            for(int j = 0; j < genes.length; j++){
                componenteHorizontal = componenteHorizontal + genes[j][i];
            }
            componetesHorizontales.add(componenteHorizontal);
        }
        return componetesHorizontales.stream().anyMatch(gen -> validarCadenaGenes(gen));
    }

    private boolean validacionLienas(ArrayList<String> genes){
        return genes.stream().anyMatch(gen -> validarCadenaGenes(gen));
    }

    private boolean validarCadenaGenes (String genes){
       return this.genesMutantes.stream().anyMatch(genMutante
               -> genMutante.matcher(genes).find());
    }

    private void guardarMuestra(Genes genes){
        if (repository.findByDna(genes.getDna()) == null){
            repository.save(genes);
        }
    }


}
