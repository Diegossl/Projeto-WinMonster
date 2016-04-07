package br.uefs.ecomp.winmonster.controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import br.uefs.ecomp.winmonster.exceptions.ArquivoCorrompidoException;
import br.uefs.ecomp.winmonster.exceptions.ArquivoVazioException;
import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.util.AlgoritmoHuffman;
import br.uefs.ecomp.winmonster.util.Fila;
import br.uefs.ecomp.winmonster.util.No;
/**
 * 
 * @author Victor Munduruca
 * @author Diego Lourenço 
 * Essa classe realiza a intermediação entre a interface e o algortimo de Huffman em si, seguindo também 
 * a design pattern Singleton
 *
 */
/******************************************************************************* 
Autor: Victor Munduruca e Diego Lourenço
Componente Curricular: MI Algoritmos II
Concluido em: 07/04/2016
Declaro que este código foi elaborado por mim de forma individual e não contém nenhum 
trecho de código de outro colega ou de outro autor, tais como provindos de livros e  
apostilas, e páginas ou documentos eletrônicos da Internet. Qualquer trecho de código 
de outra autoria que não a minha está destacado com uma citação para o autor e a fonte 
do código, e estou ciente que estes trechos não serão considerados para fins de avaliação. 
******************************************************************************************/ 

public class AdministradorController {


	private static AdministradorController instanciaAdm; // Esse atributo serve como controle para o padrão Singleton
	private AlgoritmoHuffman algoritmoHuffman; // 

	private AdministradorController(){
		AlgoritmoHuffman.zerarSingleton(); // Zera o singleton
		algoritmoHuffman = AlgoritmoHuffman.getInstance(); // Recupera a única instância da classe AlgoritmoHuffman
	}
	/**
	 * Metodo que instancia um objeto do tipo AdministradorController apenas uma vez.
	 * @return AdministradorController
	 */
	public static AdministradorController getInstance(){
		if(instanciaAdm == null)//Se for a primeira vez a ser usada, então é criada a uma instancia.
			instanciaAdm = new AdministradorController();
		return instanciaAdm;//Retorna a referência do mesmo objeto AdministradorController.
	}
	/**
	 * Método que retorna a intância de AlgoritmoHuffman
	 * @return Instância AlgoritmoHuffman
	 */
	public AlgoritmoHuffman getHuff(){
		return algoritmoHuffman;
	}
	/**
	 * Método que reseta a referência "instanciaAdm" permitindo criar uma instância da classe AdministradorController.
	 * @return void
	 */
	public static void zerarSingleton() {
		instanciaAdm = null;
	}
	/**
	 * Método que retorna uma fila de prioridade, de nós 
	 * @param Local onde o arquivo se encontra 
	 * @return Fila ordenada de nós de árvore, com base nas suas frequências
	 * @throws IOException
	 * @throws ArquivoVazioException 
	 */
	public Fila filaDeFrequencias(File arquivo) throws IOException, ArquivoVazioException{
		Fila filaOrdenada = algoritmoHuffman.contaFrequencias(arquivo);
		return filaOrdenada;
	}
	/**
	 * Método que realiza a construção da árvore 
	 * @param Fila de prioridade de nós, tendo a frequência de seus caracteres como base para as comparações
	 * @return Uma árvore feita com base no algoritmo de huffman
	 * @throws FilaNulaException
	 */
	public No construirArvore(Fila filaOrdenada) throws ArquivoVazioException{
		if(filaOrdenada == null) // Verifica se a fila é nula
			throw new ArquivoVazioException();
		No raiz = algoritmoHuffman.arvore(filaOrdenada); // Constroe e árvore com base na fila de prioridade
		return raiz;
	}
	/**
	 * 
	 * @param texto
	 * @return
	 */
	public Long funcaoHash(String texto){
		int valor = 0, posicao = 0;
		Long soma = (long) 0;
		for(int i=0; i<texto.length(); i++){
			valor = texto.charAt(i);
			posicao = texto.indexOf(texto.charAt(i));
			soma = soma +  (valor * posicao);
		}
		return soma;
	}
	/**
	 * Método que realiza a compactação de um arquivo, ou seja, a escrita de seu texto codificado e objetos necessários para
	 * a descompactação 
	 * @param Raiz de uma árvore de Huffman
	 * @param Texto Codificado
	 * @param Caminho onde o arquivo será compactado
	 * @param Nome do arquivo 
	 * @param Hash do Texto Original 
	 * @throws IOException
	 */
	public void compactar(No raiz, String txtCodificado, String caminho, String nomeArq, Long hash) throws IOException {
		
		//nomeArq = trocaNome(nomeArq);
		nomeArq = nomeArq + ".monster"; // É adicionado a extensão proprietária para a descompactação 
		caminho = caminho + nomeArq; // O caminho é atualizafo adicionando o nome do arquivo
		
		/* Métodos para a escrita do arquivo*/
		File escritaArquivo = new File(caminho);
		FileOutputStream fos = new FileOutputStream(escritaArquivo);
	    ObjectOutputStream escrever = new ObjectOutputStream(fos);
	   
	    
	    escrever.writeLong(hash); // Escreve um Long referente ao número retornado pelo hashing do txtOriginal
	    escrever.writeObject(raiz); // Escreve o objeto raiz
	    BitSet bits = new BitSet();
	    escreverBitSet(bits, txtCodificado); // COnverte a string binária txtCodificado em BitSet
	    escrever.writeObject(bits); // Escreve esse bitSet
	    escrever.close(); // Fecha o arquivo
	    fos.close();
	}
	/**
	 * Converte uma String binária em bitSet
	 * @param BitSet a ser atualizado
	 * @param Texto Binário 
	 */
	public void escreverBitSet(BitSet bits, String texto) {
		for(int i = 0; i < texto.length() ; i++) { // Itera a string
			if(texto.charAt(i) == '1') { // Como o bitSet é inicialiado com zero, adiciona true quando o caractere da 
				//String é um
				bits.set(i);
			}
		}
		bits.set(texto.length()); // Um último true é setado no bitSet, para tratar o erro em textos codificados que terminam com zero
	}
	/**
	 * Realiza a leitura do hash
	 * @param file
	 * @return O hash lido
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Long lerHash(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    Long hash = (Long) entrada.readLong();
	    entrada.close();
	    return hash;
	}
	/**
	 * Realiza a leitura da árvore
	 * @param file
	 * @return A árvore lida
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public No lerArvore(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    entrada.readLong();
	    No mapa = (No) entrada.readObject();
	    entrada.close();
	    return mapa;
	}
	/**
	 * Realiza a leitura do texto codificado
	 * @param file
	 * @return BitSet que representa o texto codificado
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public BitSet lerTexto(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    entrada.readLong();
	    entrada.readObject();
	    BitSet bits = (BitSet) entrada.readObject();
	    entrada.close();
	    return bits;		
	}
	/**
	 * Método que realiza a descompactação do arquivo
	 * @param file
	 * @param nomeArq
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ArvoreNulaException
	 * @throws ArquivoCorrompidoException
	 */
	public void descompactar(File file, String nomeArq) throws ClassNotFoundException, IOException, ArvoreNulaException, ArquivoCorrompidoException {
	   /* Realiza a leitura dos objetos necessários para a descompactação*/
		Long hashOriginal = lerHash(file);
		
	    No arvore = lerArvore(file);
	    BitSet bits = lerTexto(file);
	   
		String txtDecod = algoritmoHuffman.decodificarTexto(arvore, bits); // Decodifica o texto, a partir da árvore e da sequência de bits
		
		Long hashNova = funcaoHash(txtDecod); // É feito um hashing no texto decodificado
		verificarIntegridade(hashOriginal, hashNova); // verificar se o arquivo não foi corrompido
		
		/* Métodos para a escrita no arquivo*/
		File arquivo = new File(file.getPath().replace(".monster", "")); // É retirado a extensão .monster do arquivo
		FileWriter fw = new FileWriter(arquivo);  
		BufferedWriter bw = new BufferedWriter(fw);  
		
		String txtAtualizado = txtDecod.replaceAll("\n", System.lineSeparator()); // Transforma os '\n' em lineSeparators, para preservar a identação 
		//ao utilizar a classe BufferedWriter
		txtAtualizado = txtAtualizado.substring(0,txtAtualizado.length()-1); // retira-se o último caractere, que é um '\n' a mais
		bw.write(txtAtualizado); // escreve o texto decodificado 
		bw.close();
	}
	/**
	 * Método que realiza a vericação da integridade 
	 * @param hashOriginal
	 * @param hashNova
	 * @throws ArquivoCorrompidoException
	 */
	public void verificarIntegridade(Long hashOriginal, Long hashNova) throws ArquivoCorrompidoException {
		if(!hashOriginal.equals(hashNova)) { // Se o hash orinal e a nova não forem iguais, é lançada a seguinte exceção 
			throw new ArquivoCorrompidoException();
		}
	}
}
