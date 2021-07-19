package com.ramiro.proyectoMutantes;

import com.ramiro.proyectoMutantes.modelo.DetectorDeMutantes;
import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;

@SpringBootTest
class ProyectoMutantesApplicationTests {

	@Autowired
	GenesRepository repository;
	@Autowired
	DetectorDeMutantes detector;
	Genes genesInvalidos, genesHumanos, genesDiagonales, genesLineales, genesHorizontales;


	@BeforeEach
	public void setUp(){
		genesInvalidos =new Genes();
		genesDiagonales =new Genes();
		genesLineales =new Genes();
		genesHorizontales =new Genes();
		genesHumanos =new Genes();
	}


	@Test
	void personaConPocosGenesNoEsMutante(){
		ArrayList<String> personaConPocosGenes = new ArrayList<>();
		personaConPocosGenes.add("AVS");
		personaConPocosGenes.add("SDS");
		personaConPocosGenes.add("LLS");
		genesInvalidos.setDna(personaConPocosGenes);
		assert !detector.esMutante(genesInvalidos);
	}

	@Test
	void genesDesconocidosFallan(){
		ArrayList<String> personaConGenesDesconocidos = new ArrayList<>();
		personaConGenesDesconocidos.add("ATGA");
		personaConGenesDesconocidos.add("CATG");
		personaConGenesDesconocidos.add("GGAC");
		personaConGenesDesconocidos.add("TTAS");
		genesInvalidos.setDna(personaConGenesDesconocidos);
		assert !detector.esMutante(genesInvalidos);
	}

	@Test
	void humanoNoCumple(){
		ArrayList<String> humano = new ArrayList<>();
		humano.add("ATGA");
		humano.add("CATG");
		humano.add("GGAC");
		humano.add("TTAG");
		genesHumanos.setDna(humano);
		detector.esMutante(genesHumanos);
		assert !genesHumanos.isEsMutante();
	}

	@Test
	void repeticionesDiagonalesCumplen(){
		ArrayList<String> personaConGenesDiagonales = new ArrayList<>();
		personaConGenesDiagonales.add("ATGAGT");
		personaConGenesDiagonales.add("CATGCC");
		personaConGenesDiagonales.add("GGACTA");
		personaConGenesDiagonales.add("TTAAGC");
		personaConGenesDiagonales.add("TTAAGG");
		personaConGenesDiagonales.add("TTAACA");
		genesDiagonales.setDna(personaConGenesDiagonales);
		detector.esMutante(genesDiagonales);
		assert genesDiagonales.isEsMutante();
	}

	@Test
	void repeticionesLinenalesCumplen(){
		ArrayList<String> personaConGenesLineales = new ArrayList<>();
		personaConGenesLineales.add("ATGA");
		personaConGenesLineales.add("CATG");
		personaConGenesLineales.add("GGGG");
		personaConGenesLineales.add("TTAA");
		genesLineales.setDna(personaConGenesLineales);
		detector.esMutante(genesLineales);
		assert genesLineales.isEsMutante();
	}

	@Test
	void repeticionesHorizontalesCumplen(){
		ArrayList<String> personaConGenesHorizontales = new ArrayList<>();
		personaConGenesHorizontales.add("ATGT");
		personaConGenesHorizontales.add("CATT");
		personaConGenesHorizontales.add("GGGT");
		personaConGenesHorizontales.add("TTAT");
		genesHorizontales.setDna(personaConGenesHorizontales);
		detector.esMutante(genesHorizontales);
		assert genesHorizontales.isEsMutante();
	}



}
