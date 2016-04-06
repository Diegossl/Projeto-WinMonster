package br.uefs.ecomp.winmonster.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;

import br.uefs.ecomp.winmonster.exceptions.ArquivoCorrompidoException;
import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.exceptions.FilaNulaException;
import br.uefs.ecomp.winmonster.util.AlgoritmoHuffman;
import br.uefs.ecomp.winmonster.util.Fila;
import br.uefs.ecomp.winmonster.util.Iterador;
import br.uefs.ecomp.winmonster.util.Lista;
import br.uefs.ecomp.winmonster.util.No;
import br.uefs.ecomp.winmonster.util.NoMapa;
/**
 * 
 * @author Victor Munduruca e Diego Louren�o 
 * Essa classe realiza a intermedia��o entre a interface e o algortimo de Huffman em si, seguindo tamb�m 
 * a design pattern Singleton
 *
 */
public class AdministradorController {


	private static AdministradorController instanciaAdm; // Esse atributo serve como controle para o padr�o Singleton
	private AlgoritmoHuffman algoritmoHuffman; // 

	private AdministradorController(){
		AlgoritmoHuffman.zerarSingleton(); // Zera o singleton
		algoritmoHuffman = AlgoritmoHuffman.getInstance(); // Recupera a �nica inst�ncia da classe AlgoritmoHuffman
	}
	
	/**
	 * Metodo que instancia um objeto do tipo AdministradorController apenas uma vez.
	 * @return AdministradorController
	 */
	public static AdministradorController getInstance(){
		if(instanciaAdm == null)//Se for a primeira vez a ser usada, ent�o � criada a uma instancia.
			instanciaAdm = new AdministradorController();
		return instanciaAdm;//Retorna a refer�ncia do mesmo objeto AdministradorController.
	}
	/**
	 * M�todo que retorna a int�ncia de AlgoritmoHuffman
	 * @return Inst�ncia AlgoritmoHuffman
	 */
	public AlgoritmoHuffman getHuff(){
		return algoritmoHuffman;
	}
	
	/**
	 * M�todo que reseta a refer�ncia "instanciaAdm" permitindo criar uma inst�ncia da classe AdministradorController.
	 * @return void
	 */
	public static void zerarSingleton() {
		instanciaAdm = null;
	}
	
	/**
	 * M�todo que retorna uma fila de prioridade, de n�s 
	 * @param Local onde o arquivo se encontra 
	 * @return Fila ordenada de n�s de �rvore, com base nas suas frequ�ncias
	 * @throws IOException
	 */
	public Fila filaDeFrequencias(File arquivo) throws IOException{
		Fila filaOrdenada = algoritmoHuffman.contaFrequencias(arquivo);
		return filaOrdenada;
	}
	/**
	 * M�todo que realiza a constru��o da �rvore 
	 * @param Fila de prioridade de n�s, tendo a frequ�ncia de seus caracteres como base para as compara��es
	 * @return Uma �rvore feita com base no algoritmo de huffman
	 * @throws FilaNulaException
	 */
	public No construirArvore(Fila filaOrdenada) throws FilaNulaException{
		if(filaOrdenada == null) // Verifica se a fila � nula
			throw new FilaNulaException();
		No raiz = algoritmoHuffman.arvore(filaOrdenada);
		return raiz;
	}
	
	
	
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
	 * M�todo que realiza a compacta��o de um arquivo, ou seja, a escrita de seu texto codificado e objetos necess�rios para
	 * a descompacta��o 
	 * @param Raiz de uma �rvore de Huffman
	 * @param Texto Codificado
	 * @param Caminho onde o arquivo ser� compactado
	 * @param Nome do arquivo 
	 * @param Hash do Texto Original 
	 * @throws IOException
	 */
	public void compactar(No raiz, String txtCodificado, String caminho, String nomeArq, Long hash) throws IOException {
		
		nomeArq = nomeArq + ".monster"; // � adicionado a extens�o propriet�ria para a descompacta��o 
		caminho = caminho + nomeArq; // O caminho � atualizafo adicionando o nome do arquivo
		
		/* M�todos para a escrita do arquivo*/
		File escritaArquivo = new File(caminho);
		FileOutputStream fos = new FileOutputStream(escritaArquivo);
	    ObjectOutputStream escrever = new ObjectOutputStream(fos);
	   
	    
	    escrever.writeLong(hash); // Escreve um Long referente ao n�mero retornado pelo hashing do txtOriginal
	    escrever.writeObject(raiz); // Escreve o objeto raiz
	    BitSet bits = new BitSet();
	    escreverBitSet(bits, txtCodificado); // COnverte a string bin�ria txtCodificado em BitSet
	    escrever.writeObject(bits); // Escreve esse bitSet
	    escrever.close(); // Fecha o arquivo
	    fos.close();
	}
	/**
	 * Converte uma String bin�ria em bitSet
	 * @param BitSet a ser atualizado
	 * @param Texto Bin�rio 
	 */
	public void escreverBitSet(BitSet bits, String texto) {
		for(int i = 0; i < texto.length() ; i++) { // Itera a string
			if(texto.charAt(i) == '1') { // Como o bitSet � inicialiado com zero, adiciona true quando o caractere da 
				//String � um
				bits.set(i);
			}
		}
		bits.set(texto.length()); // Um �ltimo true � setado no bitSet, para tratar o erro em textos codificados que terminam com zero
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
	 * Realiza a leitura da �rvore
	 * @param file
	 * @return A �rvore lida
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
	 * M�todo que realiza a descompacta��o do arquivo
	 * @param file
	 * @param nomeArq
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ArvoreNulaException
	 * @throws ArquivoCorrompidoException
	 */
	public void descompactar(File file, String nomeArq) throws ClassNotFoundException, IOException, ArvoreNulaException, ArquivoCorrompidoException {
	   /* Realiza a leitura dos objetos necess�rios para a descompacta��o*/
		Long hashOriginal = lerHash(file);
	    No arvore = lerArvore(file);
	    BitSet bits = lerTexto(file);
	   
		String txtDecod = algoritmoHuffman.decodificarTexto(arvore, bits); // Decodifica o texto, a partir da �rvore e da sequ�ncia de bits
		Long hashNova = funcaoHash(txtDecod); // � feito um hashing no texto decodificado
		verificarIntegridade(hashOriginal, hashNova); // verificar se o arquivo n�o foi corrompido
		
		/* M�todos para a escrita no arquivo*/
//		File arquivo = new File(file.getPath().replace(".monster", "")); // � retirado a extens�o .monster do arquivo
		File arquivo = new File(file.getPath().replace(nomeArq, "novo.txt")); 
		FileWriter fw = new FileWriter(arquivo);  
		BufferedWriter bw = new BufferedWriter(fw);  
		
		String txtAtualizado = txtDecod.replaceAll("\n", System.lineSeparator()); // Transforma os '\n' em lineSeparators, para preservar a identa��o 
		//ao utilizar a classe BufferedWriter
		txtAtualizado = txtAtualizado.substring(0,txtAtualizado.length()-1); // retira-se o �ltimo caractere, que � um '\n' a mais
		bw.write(txtAtualizado); // escreve o texto decodificado 
		bw.close();
	}
	/**
	 * M�todo que realiza a verica��o da integridade 
	 * @param hashOriginal
	 * @param hashNova
	 * @throws ArquivoCorrompidoException
	 */
	public void verificarIntegridade(Long hashOriginal, Long hashNova) throws ArquivoCorrompidoException {
		if(!hashOriginal.equals(hashNova)) { // Se o hash orinal e a nova n�o forem iguais, � lan�ada a seguinte exce��o 
			throw new ArquivoCorrompidoException();
		}
	}
}
