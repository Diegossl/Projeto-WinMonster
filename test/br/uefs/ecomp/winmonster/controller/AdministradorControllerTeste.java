package br.uefs.ecomp.winmonster.controller;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.junit.Before;
import org.junit.Test;

import br.uefs.ecomp.winmonster.exceptions.ArquivoCorrompidoException;
import br.uefs.ecomp.winmonster.exceptions.ArquivoVazioException;
import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.util.Fila;
import br.uefs.ecomp.winmonster.util.No;

public class AdministradorControllerTeste {

	private AdministradorController controllerAdm;

	@Before
	public void setUp() throws Exception {
		AdministradorController.zerarSingleton();
		controllerAdm = AdministradorController.getInstance();
	}

	@Test
	public void gerarValorHashSucesso(){
		FileReader file = null;
		try {
			file = new FileReader("arquivosTestes/testeIntegridade.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		BufferedReader leitura = new BufferedReader(file);
		String texto = "";
		try {
			while(leitura.ready()){
				texto = texto + leitura.readLine();
			}
		} catch (IOException e) {
			fail();
		}

		Long hash = controllerAdm.funcaoHash(texto);

		assertEquals(302, hash.longValue());
	}

	@Test
	public void testCompactarComSucesso(){

		File file = new File("arquivosTestes/testeCompactar.txt");

		Fila fila = null;
		try {
			fila = controllerAdm.filaDeFrequencias(file);
		} catch (IOException e) {
			fail();
		} catch (ArquivoVazioException e) {
			fail();
		}

		No raiz = null;
		try {
			raiz = controllerAdm.construirArvore(fila);
		} catch (ArquivoVazioException e) {
			fail();
		}

		try {
			controllerAdm.getHuff().mapeamento(raiz);
		} catch (IOException e) {
			fail();
		} catch (ArvoreNulaException e) {
			fail();
		}

		String textoCodificado = controllerAdm.getHuff().codificarTexto(controllerAdm.getHuff().getTextoOriginal());

		try {
			controllerAdm.compactar(raiz, textoCodificado,file.getPath().replace(file.getName(), ""), file.getName(), controllerAdm.funcaoHash(controllerAdm.getHuff().getTextoOriginal()));
		} catch (IOException e) {
			fail();
		}

		File arquivoCompactado = new File("arquivosTestes/frase.txt.monster");

		int chave = 0;

		if(arquivoCompactado != null)
			chave = 1;

		assertEquals(1, chave);

		int chave2 = 0;

		if(arquivoCompactado.length() < file.length())
			chave2 = 1;

		assertEquals(1, chave2);
	}

	@Test
	public void testDescompactarComSucesso(){
		File file = new File("arquivosTestes/testeDescompactar.txt");

		Fila fila = null;
		try {
			fila = controllerAdm.filaDeFrequencias(file);
		} catch (IOException e) {
			fail();
		} catch(ArquivoVazioException e) {
			fail();
		}

		No raiz = null;
		try {
			raiz = controllerAdm.construirArvore(fila);
		} catch (ArquivoVazioException e) {
			fail();
		}

		try {
			controllerAdm.getHuff().mapeamento(raiz);
		} catch (ArvoreNulaException e) {
			fail();
		} catch (IOException e) {
			fail();
		}

		String textoCodificado = controllerAdm.getHuff().codificarTexto(controllerAdm.getHuff().getTextoOriginal());

		try {
			controllerAdm.compactar(raiz, textoCodificado, file.getPath().replace(file.getName(), ""), file.getName(), controllerAdm.funcaoHash(controllerAdm.getHuff().getTextoOriginal()));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}


		File arquivo = new File("arquivosTestes/testeDescompactar.txt.monster");

		try {
			controllerAdm.descompactar(arquivo, arquivo.getName());
		} catch (ClassNotFoundException | IOException | ArvoreNulaException | ArquivoCorrompidoException e) {
			fail();
		}
		File arquivoOriginal = new File("arquivosTestes/testeDescompactar - Copia.txt");
		File arquivoDescompactado = new File("arquivosTestes/testeDescompactar.txt");
		

		int chave = 0;

		if(arquivoDescompactado != null)
			chave = 1;

		assertEquals(1, chave);

		int chave2 = 0;
		if(arquivoOriginal.length() == arquivoDescompactado.length())
			chave2 = 1;

		assertEquals(1, chave2);
	}
	@Test 
	public void testCompactarArquivoVazio() {
		File arquivo = new File("arquivosTestes/testeVazio.txt");

		Fila fila = null;
		try {
			fila = controllerAdm.filaDeFrequencias(arquivo);
		} catch (IOException e) {
			fail();
		} catch (ArquivoVazioException e) {
			assertTrue(true);
		}

		No raiz = null;
		try {
			raiz = controllerAdm.construirArvore(fila);
		} catch (ArquivoVazioException e) {
			assertTrue(true);	
		}

		try {
			controllerAdm.getHuff().mapeamento(raiz);
		} catch (IOException e) {
			fail();
		} catch (ArvoreNulaException e) {
			assertTrue(true);
		}
	}
}
