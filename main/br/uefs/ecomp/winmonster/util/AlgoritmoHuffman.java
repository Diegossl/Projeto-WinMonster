package br.uefs.ecomp.winmonster.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.exceptions.FilaNulaException;

/**
 * Essa classe implementa o algoritmo de compacta��o de Huffman
 * @author Victor Munduruca e Diego Louren�o
 *
 */
public class AlgoritmoHuffman {

	private static String arestas = ""; // String utilizada pelo m�todo recursivo mapeamento
	private  String textoOriginal; // Texto lido na compacta��o
	private String[] mapa = new String[256]; // Mapa de Strings, onde o �ndice se refere ao caractere e a String, a sequ�ncia correspondente
	
	
	/**
	 * 
	 * @return vetor de Strings, correspondentes ao mapa
	 */
	public String[] getMapa() {
		return mapa;
	}

	
	private static AlgoritmoHuffman instanciaAdm;
	
	private AlgoritmoHuffman() {

	}
	/**
	 * Implementa��o do padr�o Singleton
	 * @return Refer�ncia do mesmo objeto AlgoritmoHuffman
	 */
	public static AlgoritmoHuffman getInstance(){
		if(instanciaAdm == null) //Se for a primeira vez a ser usada, ent�o � criada a uma instancia.
			instanciaAdm = new AlgoritmoHuffman();
		return instanciaAdm; 
	}
	/**
	 * Zera os atributos dessa classe, para, apesar de ser a mesma inst�ncia, os atributos zerem a cada uso
	 * @return void
	 */
	public static void zerarSingleton() {
		arestas = "";
		instanciaAdm = null;
	}
	/**
	 * Esse m�todo realiza a primeira etapa do algoritmo de Huffman. Essa etapa � feita contando as frequ�ncias de cada caractere. Em seguida, relacionando essas informa��es
	 * (caractere e frequ�ncia) a objetos n�s, que s�o inseridos em uma fila de prioridade que � retornada
	 * @param arquivo
	 * @return Fila de Prioridade de objetos N�s
	 * @throws IOException
	 */
	public Fila contaFrequencias(File arquivo) throws IOException{
		No[] vetor = new No[256]; // Vetor que armazena as frequ�ncias, onde os �ndices s�o os caracteres
		Fila filaNo = new Fila(); // fila normal pra armazenar os n�s 
		String texto = ""; // representa cada linha lida
		
		StringBuffer str = new StringBuffer(); // Realiza a leitura do texto original
		FileReader file = new FileReader(arquivo);
		BufferedReader leitura = new BufferedReader(file);
		
		while(leitura.ready()){ 
			texto = leitura.readLine()+ "\n";// L� a linha
			str.append(texto); // Adiciona a linha lida ao texto original
			for(int i = 0; i < texto.length(); i++) { // Percorre a linha
				char ch = texto.charAt(i);
				if(vetor[(int) ch] == null) { // verifica se existe um n� no �ndice do caractere lido
					vetor[(int) ch] = new No(); // Instancia um novo n�
					vetor[(int) ch].setSimbolo(ch); // Configura o s�mbolo desse n� como o caractere lido
					filaNo.inserirFinal(vetor[(int) ch]);
				}
				vetor[(int) ch].setFrequencia(vetor[(int) ch].getFrequencia() + 1); // Se o n� existe, somente � incrementada sua frequ�ncia
			}
		}
		leitura.close(); // fecha o arquivo
		file.close();
		
		Fila filaOrdenada = new Fila(); // Cria uma fila de prioridade para armazenar os n�s
		while(!filaNo.estaVazia()) {
			filaOrdenada.inserirPrioridade(filaNo.removerInicio()); // remove da lista desordenada para a lista de prioridade
		}
		textoOriginal = str.toString(); // Atualiza o valor do texto original
		return filaOrdenada; // Retorna a fila de prioridade
	}
	/**
	 * Cria a �rvore do algoritmo de Huffman
	 * @param Fila de prioridade de N�s com base na frequ�ncia
	 * @return �rvore criada a partir da fila de prioridade, seguindo o algoritmo de Huffman
	 * @throws FilaNulaException
	 */
	public No arvore(Fila fila) throws FilaNulaException{
		if(fila.estaVazia()) // veriifica se a fila est� vazia
			throw new FilaNulaException();
		if(fila.obterTamanho() == 1) // Se a fila s� conter um elemento, um n� com esse elemento � retornado
			return (No) fila.removerInicio();
		No noPai = null; // noPai � inicializado
		while(true){ 
			/*remove os dois primeiros items da fila, armazenando-os em n�s*/
			No no = (No) fila.removerInicio();
			No no2 = (No) fila.removerInicio();
			noPai = new No(no, no2); // noPai agora � pai dos dois n�s removidos da fila de prioridade, e sua frequ�ncia a soma das duas
			if(fila.estaVazia())
				break;
			fila.inserirPrioridade(noPai); // insere o n� na fila de prioridade e assim por diante, at� q a fila fique vaiza
		}
		return noPai; // retorna a �rvore
	}
	/**
	 * M�todo que decodifica o texto
	 * @param �rvore de Huffman
	 * @param Bitset do texto codificado
	 * @return Texto decodificado
	 */
	public String decodificarTexto(No arvore, BitSet txtCod) {
		No aux = arvore; // Auxiliar criado para iterar pela �rvore
		StringBuffer txtDecod = new StringBuffer(); 
		for(int i = 0; i < txtCod.length() - 1; i++) { // S� vai at� o tamanho do bitSet menos um, pois � escrito um a mais no m�todo escreverBitset
			//Isso � feito com o intuito de tratar o erro de bitSets terminando em zeer
			if(txtCod.get(i) == false) { // Se for zero vai para a esquerda
				aux = aux.getFilhoDaEsquerda();
			} else if(txtCod.get(i) == true) { // Se for um, para a direita
				aux = aux.getFilhoDaDireita();
			}
			if(aux.eFolha()) { // Se for folha, � porque a sequ�ncia bin�ria equivale ao caractere presente nela 
				txtDecod.append(aux.getSimbolo()); // Atualiza o buffer
				aux = arvore; // reseta a �rvore para a raiz
			}
		}
		return txtDecod.toString();
	}
	/**
	 * Esse m�todo realiza a cofica��o do texto
	 * @param Texto a ser codificado
	 * @return Texto Codificado
	 */
	public String codificarTexto(String texto) {
		StringBuffer textoCod = new StringBuffer(); // um string buffer � criado
		for(int i = 0; i < texto.length(); i++ ) { //a string a ser codificada � percorrida 
			textoCod.append(mapa[(int) texto.charAt(i)]); // � buscado a sequ�ncia passando o caractere como �ndice
		}
		return textoCod.toString();
	}
	/**
	 * Realiza o mapeamento, ou seja, a cria��o de um vetor que relacione as sequ�ncias obtidas na �rvore
	 * com os caracteres (�ndices)
	 * @param no
	 * @throws IOException
	 * @throws ArvoreNulaException
	 */
	public void mapeamento(No no) throws IOException, ArvoreNulaException{
		if(no == null) // Verifica se a �rvore � nula
			throw new ArvoreNulaException(); 
		if(no.eFolha()){ // Se for folha
			mapa[(char) no.getSimbolo()] = arestas; // Adiciona a sequ�ncia de acordo com o caractere
			return;
		}
		arestas = arestas + 0;
		mapeamento(no.getFilhoDaEsquerda());
		arestas = arestas.substring(0, arestas.length() - 1);

		arestas = arestas + 1;
		mapeamento(no.getFilhoDaDireita());
		arestas = arestas.substring(0, arestas.length() - 1);

	}

	/**
	 * @return the textoOriginal
	 */
	public String getTextoOriginal() {
		return textoOriginal;
	}

}
