package com.ramiro.proyectoMutantes;

import com.ramiro.proyectoMutantes.controller.DetectorMutantesRest;
import com.ramiro.proyectoMutantes.modelo.DetectorDeMutantes;
import com.ramiro.proyectoMutantes.modelo.Estadisticas;
import com.ramiro.proyectoMutantes.persitence.Genes;
import com.ramiro.proyectoMutantes.persitence.GenesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rx.Single;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

@SpringBootTest
class ProyectoMutantesApplicationTests {

	@Autowired
	GenesRepository repository;
	@Autowired
	DetectorDeMutantes detector;
	@Autowired
	Genes genesInvalidos;
	@Autowired
	Genes genesHumanos;
	@Autowired
	Genes genesDiagonales;
	@Autowired
	Genes  genesLineales;
	@Autowired
	Genes  genesHorizontales;
	@Autowired
	Genes  genePruebaDao;
	@Autowired
	DetectorMutantesRest restService;


	@BeforeEach
	public void setUp(){
		genePruebaDao.setEsMutante(false);
		genePruebaDao.setDna(new ArrayList<>());
	}


	@Test
	void personaConPocosGenesNoEsMutante(){
		ArrayList<String> personaConPocosGenes = new ArrayList<>();
		personaConPocosGenes.add("AVS");
		personaConPocosGenes.add("SDS");
		personaConPocosGenes.add("LLS");
		genesInvalidos.setDna(personaConPocosGenes);
		assert !detector.esMutante(genesInvalidos).isEsMutante();
	}

	@Test
	void genesDesconocidosFallan(){
		ArrayList<String> personaConGenesDesconocidos = new ArrayList<>();
		personaConGenesDesconocidos.add("ATGA");
		personaConGenesDesconocidos.add("CATG");
		personaConGenesDesconocidos.add("GGAC");
		personaConGenesDesconocidos.add("TTAS");
		genesInvalidos.setDna(personaConGenesDesconocidos);
		assert !detector.esMutante(genesInvalidos).isEsMutante();
	}

	@Test
	void humanoNoCumple(){
		ArrayList<String> humano = new ArrayList<>();
		humano.add("ATGA");
		humano.add("CATG");
		humano.add("GGAC");
		humano.add("TTAG");
		genesHumanos.setDna(humano);
		Genes resultado = detector.esMutante(genesHumanos);
		assert !resultado.isEsMutante();
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
		Genes resultado = detector.esMutante(genesDiagonales);
		assert resultado.isEsMutante();
	}

	@Test
	void repeticionesLinenalesCumplen(){
		ArrayList<String> personaConGenesLineales = new ArrayList<>();
		personaConGenesLineales.add("ATGA");
		personaConGenesLineales.add("CATG");
		personaConGenesLineales.add("GGGG");
		personaConGenesLineales.add("TTAA");
		genesLineales.setDna(personaConGenesLineales);
		Genes resultado = detector.esMutante(genesLineales);
		assert resultado.isEsMutante();
	}

	@Test
	void repeticionesHorizontalesCumplen(){
		ArrayList<String> personaConGenesHorizontales = new ArrayList<>();
		personaConGenesHorizontales.add("ATGT");
		personaConGenesHorizontales.add("CATT");
		personaConGenesHorizontales.add("GGGT");
		personaConGenesHorizontales.add("TTAT");
		genesHorizontales.setDna(personaConGenesHorizontales);
		Genes resultado = detector.esMutante(genesHorizontales);
		assert resultado.isEsMutante();
	}

	@Test
	void fullTestRepository(){
		Genes genesGuardados = repository.save(genePruebaDao);
		assert genesGuardados != null;
		Genes genesEncontrados = repository.findByDna(genesGuardados.getDna());
		assert genesEncontrados != null && genesEncontrados.getDna().equals(genesGuardados.getDna());
		repository.delete(genesGuardados);
		genesEncontrados = repository.findByDna(genesGuardados.getDna());
		assert genesEncontrados == null;
	}

	@Test
	void metodoMutantResponseOKTest(){
		ArrayList<String> personaConGenesHorizontales = new ArrayList<>();
		personaConGenesHorizontales.add("ATGT");
		personaConGenesHorizontales.add("CATT");
		personaConGenesHorizontales.add("GGGT");
		personaConGenesHorizontales.add("TTAT");
		genesHorizontales.setDna(personaConGenesHorizontales);
		Single<ResponseEntity> response = restService.esMutante(genesHorizontales);
		assert response.toBlocking().value().getStatusCode().equals(HttpStatus.OK);
	}

	@Test
	void metodoMutantResponseForbiddenTest(){
		ArrayList<String> humano = new ArrayList<>();
		humano.add("ATGA");
		humano.add("CATG");
		humano.add("GGAC");
		humano.add("TTAG");
		genesHumanos.setDna(humano);
		Single<ResponseEntity> response = restService.esMutante(genesHumanos);
		assert response.toBlocking().value().getStatusCode().equals(HttpStatus.FORBIDDEN);
	}

	@Test
	void metodoEstadisticas(){
		Single<Estadisticas> response = restService.obtenerEstadisticas();
		Estadisticas stats = response.toBlocking().value();
		assert stats.getTotalMuestas() >= 0 && stats.getMuestrasMutantes() >= 0 && (stats.getRatio() >= 0.0 );
	}


}
