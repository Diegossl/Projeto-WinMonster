package br.uefs.ecomp.winmonster.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.exceptions.FilaNulaException;

/**
 * Essa classe implementa o algoritmo de compactação de Huffman
 * @author Victor Munduruca e Diego Lourenço
 *
 */
public class AlgoritmoHuffman {

	private static String arestas = ""; // String utilizada pelo método recursivo mapeamento
	private  String textoOriginal; // Texto lido na compactação
	private String[] mapa = new String[256]; // Mapa de Strings, onde o índice se refere ao caractere e a String, a sequência correspondente
	
	
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
	 * Implementação do padrão Singleton
	 * @return Referência do mesmo objeto AlgoritmoHuffman
	 */
	public static AlgoritmoHuffman getInstance(){
		if(instanciaAdm == null) //Se for a primeira vez a ser usada, então é criada a uma instancia.
			instanciaAdm = new AlgoritmoHuffman();
		return instanciaAdm; 
	}
	/**
	 * Zera os atributos dessa classe, para, apesar de ser a mesma instância, os atributos zerem a cada uso
	 * @return void
	 */
	public static void zerarSingleton() {
		arestas = "";
		instanciaAdm = null;
	}
	/**
	 * Esse método realiza a primeira etapa do algoritmo de Huffman. Essa etapa é feita contando as frequências de cada caractere. Em seguida, relacionando essas informações
	 * (caractere e frequência) a objetos nós, que são inseridos em uma fila de prioridade que é retornada
	 * @param arquivo
	 * @return Fila de Prioridade de objetos Nós
	 * @throws IOException
	 */
	public Fila contaFrequencias(File arquivo) throws IOException{
		No[] vetor = new No[256]; // Vetor que armazena as frequências, onde os índices são os caracteres
		Fila filaNo = new Fila(); // fila normal pra armazenar os nós 
		String texto = ""; // representa cada linha lida
		
		StringBuffer str = new StringBuffer(); // Realiza a leitura do texto original
		FileReader file = new FileReader(arquivo);
		BufferedReader leitura = new BufferedReader(file);
		
		while(leitura.ready()){ 
			texto = leitura.readLine()+ "\n";// Lê a linha
			str.append(texto); // Adiciona a linha lida ao texto original
			for(int i = 0; i < texto.length(); i++) { // Percorre a linha
				char ch = texto.charAt(i);
				if(vetor[(int) ch] == null) { // verifica se existe um nó no índice do caractere lido
					vetor[(int) ch] = new No(); // Instancia um novo nó
					vetor[(int) ch].setSimbolo(ch); // Configura o símbolo desse nó como o caractere lido
					filaNo.inserirFinal(vetor[(int) ch]);
				}
				vetor[(int) ch].setFrequencia(vetor[(int) ch].getFrequencia() + 1); // Se o nó existe, somente é incrementada sua frequência
			}
		}
		leitura.close(); // fecha o arquivo
		file.close();
		
		Fila filaOrdenada = new Fila(); // Cria uma fila de prioridade para armazenar os nós
		while(!filaNo.estaVazia()) {
			filaOrdenada.inserirPrioridade(filaNo.removerInicio()); // remove da lista desordenada para a lista de prioridade
		}
		textoOriginal = str.toString(); // Atualiza o valor do texto original
		return filaOrdenada; // Retorna a fila de prioridade
	}
	/**
	 * Cria a árvore do algoritmo de Huffman
	 * @param Fila de prioridade de Nós com base na frequência
	 * @return Árvore criada a partir da fila de prioridade, seguindo o algoritmo de Huffman
	 * @throws FilaNulaException
	 */
	public No arvore(Fila fila) throws FilaNulaException{
		if(fila.estaVazia()) // veriifica se a fila está vazia
			throw new FilaNulaException();
		if(fila.obterTamanho() == 1) // Se a fila só conter um elemento, um nó com esse elemento é retornado
			return (No) fila.removerInicio();
		No noPai = null; // noPai é inicializado
		while(true){ 
			/*remove os dois primeiros items da fila, armazenando-os em nós*/
			No no = (No) fila.removerInicio();
			No no2 = (No) fila.removerInicio();
			noPai = new No(no, no2); // noPai agora é pai dos dois nós removidos da fila de prioridade, e sua frequência a soma das duas
			if(fila.estaVazia())
				break;
			fila.inserirPrioridade(noPai); // insere o nó na fila de prioridade e assim por diante, até q a fila fique vaiza
		}
		return noPai; // retorna a árvore
	}
	/**
	 * Método que decodifica o texto
	 * @param Árvore de Huffman
	 * @param Bitset do texto codificado
	 * @return Texto decodificado
	 */
	public String decodificarTexto(No arvore, BitSet txtCod) {
		No aux = arvore; // Auxiliar criado para iterar pela árvore
		StringBuffer txtDecod = new StringBuffer(); 
		for(int i = 0; i < txtCod.length() - 1; i++) { // Só vai até o tamanho do bitSet menos um, pois é escrito um a mais no método escreverBitset
			//Isso é feito com o intuito de tratar o erro de bitSets terminando em zeer
			if(txtCod.get(i) == false) { // Se for zero vai para a esquerda
				aux = aux.getFilhoDaEsquerda();
			} else if(txtCod.get(i) == true) { // Se for um, para a direita
				aux = aux.getFilhoDaDireita();
			}
			if(aux.eFolha()) { // Se for folha, é porque a sequência binária equivale ao caractere presente nela 
				txtDecod.append(aux.getSimbolo()); // Atualiza o buffer
				aux = arvore; // reseta a árvore para a raiz
			}
		}
		return txtDecod.toString();
	}
	/**
	 * Esse método realiza a coficação do texto
	 * @param Texto a ser codificado
	 * @return Texto Codificado
	 */
	public String codificarTexto(String texto) {
		StringBuffer textoCod = new StringBuffer(); // um string buffer é criado
		for(int i = 0; i < texto.length(); i++ ) { //a string a ser codificada é percorrida 
			textoCod.append(mapa[(int) texto.charAt(i)]); // é buscado a sequência passando o caractere como índice
		}
		return textoCod.toString();
	}
	/**
	 * Realiza o mapeamento, ou seja, a criação de um vetor que relacione as sequências obtidas na árvore
	 * com os caracteres (índices)
	 * @param no
	 * @throws IOException
	 * @throws ArvoreNulaException
	 */
	public void mapeamento(No no) throws IOException, ArvoreNulaException{
		if(no == null) // Verifica se a árvore é nula
			throw new ArvoreNulaException(); 
		if(no.eFolha()){ // Se for folha
			mapa[(char) no.getSimbolo()] = arestas; // Adiciona a sequência de acordo com o caractere
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
