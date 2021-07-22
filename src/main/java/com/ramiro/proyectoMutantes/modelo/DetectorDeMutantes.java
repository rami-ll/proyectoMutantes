package com.ramiro.proyectoMutantes.modelo;


import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rx.Single;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

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

    public Single<ResponseEntity> esMutanteService(Genes genes){
        return Single.create(
            singleSubscriber -> {
                Genes respuesta = this.esMutante(genes);
                singleSubscriber.onSuccess(setResponse(respuesta));
            }
        );
    }

    private ResponseEntity setResponse(Genes genes){
        if(genes.isEsMutante()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public Genes esMutante(Genes genes){
        char[][] matrizGenes;
        ArrayList<String> muestra = genes.getDna();
        if (inputValido(muestra)){
            Genes guardados = repository.findByDna(muestra);
            if (guardados == null) {
                matrizGenes = generarMatriz(muestra);
                genes.setEsMutante(validacionDiagonal(matrizGenes) || validacionFilas(matrizGenes) || validacionLienas(muestra));
                repository.save(genes);
            } else {
                return guardados;
            }
        }
        else{
           genes.setEsMutante(false);
        }
        return genes;
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
        String genesDiagonalesSup = "";
        String genesDiagonalesInf = "";
        int cantidadMinima = 4;
        int largo = genes.length;
        //Diagonal principal
        for (int i = 0; i < largo; i++){
            genesDiagonales = genesDiagonales + genes[i][i];
        }
        cadenasDiagonales.add(genesDiagonales);
        genesDiagonales = "";
        //Diagonales laterales
        for (int i = 1; largo - i >= cantidadMinima; i++){
            for (int j = 0; j + i < largo; j++){
                genesDiagonalesSup = genesDiagonalesSup + genes[j][j+i];
                genesDiagonalesInf = genesDiagonalesInf + genes[j+i][j];
            }
            cadenasDiagonales.add(genesDiagonalesSup);
            cadenasDiagonales.add(genesDiagonalesInf);
            genesDiagonalesSup = "";
            genesDiagonalesInf = "";
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


}
