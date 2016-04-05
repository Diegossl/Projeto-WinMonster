package br.uefs.ecomp.winmonster.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.exceptions.FilaNulaException;

public class AlgoritmoHuffman {

	private String arestas = "";

	private Lista folhas = new Lista();

	private static AlgoritmoHuffman instanciaAdm;

	private AlgoritmoHuffman() {

	}

	public static AlgoritmoHuffman getInstance(){
		if(instanciaAdm == null)
			instanciaAdm = new AlgoritmoHuffman();
		return instanciaAdm;
	}

	public static void zerarSingleton() {
		instanciaAdm = null;
	}
	
	public Lista getFolhas(){
		return folhas;
	}
	
	public Fila contaFrequencias(File arquivo) throws IOException{
		Fila fila = new Fila();
		FileReader file = new FileReader(arquivo);
		BufferedReader leitura = new BufferedReader(file);
		while(leitura.ready()){
			String texto = leitura.readLine() + "\n";
			for(int i = 0; i < texto.length(); i++) {
				char ch = texto.charAt(i);
				No no = recuperarNo(fila, ch);
				if(no == null){
					No novoNo = new No();
					novoNo.setSimbolo(ch);
					fila.inserirFinal(novoNo);
				} 
				else{
					no.setFrequencia(no.getFrequencia() + 1);
				}
			}
		}
		leitura.close();
		file.close();
		Fila filaOrdenada = new Fila();
		while(!fila.estaVazia()) {
			filaOrdenada.inserirPrioridade(fila.removerInicio());
		}
		return filaOrdenada;
	}
	
	public No recuperarNo(Fila fila, char simbolo) {
		Iterador iteradorFila = fila.iterador();
		while(iteradorFila.temProximo()) {
			No noAtual =  (No) iteradorFila.obterProximo();
			if(noAtual.getSimbolo() == simbolo) {
				return noAtual;
			}
		}
		return null;
	}

	public No arvore(Fila fila) throws FilaNulaException{
		if(fila.estaVazia())
			throw new FilaNulaException();
		if(fila.obterTamanho() == 1)
			return (No) fila.removerInicio();
		No noPai = null;
		while(true){
			No no = (No) fila.removerInicio();
			No no2 = (No) fila.removerInicio();
			noPai = new No(no, no2);
			if(fila.estaVazia())
				break;
			fila.inserirPrioridade(noPai);
		}
		return noPai;
	}

	public void mapeamento(No no) throws IOException, ArvoreNulaException{
		if(no == null)
			throw new ArvoreNulaException(); 
		if(no.getFilhoDaEsquerda() == null && no.getFilhoDaDireita() == null){
			NoMapa folha = new NoMapa(arestas, no.getSimbolo());
			folhas.inserirFinal(folha);
			return;
		}
		arestas = arestas + 0;
		mapeamento(no.getFilhoDaEsquerda());
		arestas = arestas.substring(0, arestas.length() - 1);

		arestas = arestas + 1;
		mapeamento(no.getFilhoDaDireita());
		arestas = arestas.substring(0, arestas.length() - 1);

	}
}
