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

public class AdministradorController {


	private static AdministradorController instanciaAdm;
	private AlgoritmoHuffman algoritmoHuffman;

	private AdministradorController(){
		AlgoritmoHuffman.zerarSingleton();
		algoritmoHuffman = AlgoritmoHuffman.getInstance();
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

	public AlgoritmoHuffman getHuff(){
		return algoritmoHuffman;
	}
	
	public Fila filaDeFrequencias(File arquivo) throws IOException{
		Fila filaOrdenada = algoritmoHuffman.contaFrequencias(arquivo);
		return filaOrdenada;
	}
	
	public No construirArvore(Fila filaOrdenada) throws FilaNulaException{
		if(filaOrdenada == null)
			throw new FilaNulaException();
		No raiz = algoritmoHuffman.arvore(filaOrdenada);
		return raiz;
	}
	
	/**
	 * Método que reseta a referência "instanciaAdm" permitindo criar uma instância da classe AdministradorController.
	 */
	public static void zerarSingleton() {
		instanciaAdm = null;
	}
	
	public String textoOriginal(File arquivo) throws IOException{
		FileReader file = new FileReader(arquivo);
		BufferedReader leitura = new BufferedReader(file);
		String texto = "";
		while(leitura.ready()){
			texto = texto + leitura.readLine() + "\n";
		}
		System.out.println("" +texto);
		leitura.close();
		file.close();
		return texto;
	}
	
	public int funcaoHash(String texto){
		int valor = 0, posicao = 0, soma = 0;
		for(int i=0; i<texto.length(); i++){
			valor = texto.charAt(i);
			posicao = texto.indexOf(texto.charAt(i));
			soma = soma +  (valor * posicao);
		}
		return soma;
	}
	
	public String decodificarTexto(No arvore, BitSet txtCod) {
		No aux = arvore;
		String txtDecod = "";
		for(int i = 0; i < txtCod.length() - 1; i++) {
			if(txtCod.get(i) == false) {
				aux = aux.getFilhoDaEsquerda();
			} else if(txtCod.get(i) == true) {
				aux = aux.getFilhoDaDireita();
			}
			if(aux.eFolha()) {
				txtDecod += aux.getSimbolo();
				aux = arvore;
			}
		}
		return txtDecod;
	}
	
	public String codificarTexto(Lista mapa , String texto) {
		StringBuffer textoCod = new StringBuffer();
		for(int i = 0; i < texto.length(); i++ ) {
			textoCod.append(instanciaAdm.getHuff().getMapa()[(int) texto.charAt(i)].getSequencia());
		}
		return textoCod.toString();
	}
	
//	public String buscar(Lista mapa , char simbolo) {
//		Iterador iteradorMapa = mapa.iterador ();
//		while(iteradorMapa .temProximo()) {
//			NoMapa noMapa = (NoMapa) iteradorMapa. obterProximo();
//			if(noMapa.getSimbolo() ==  simbolo) {
//				return noMapa.getSequencia();
//			}
//		}
//		return null ;
//	}
	
	public void compactar(No raiz, String txtCodificado, String caminho, String nomeArq) throws IOException {
		nomeArq = nomeArq + ".monster";
		caminho = caminho + nomeArq;		
		File escritaArquivo = new File(caminho);
		FileOutputStream fos = new FileOutputStream(escritaArquivo);
	    ObjectOutputStream escrever = new ObjectOutputStream(fos);
	    
	    escrever.writeObject(raiz);
	    escrever.writeInt(funcaoHash(txtCodificado));
	    BitSet bits = new BitSet();
	    escreverBitSet(bits, txtCodificado);
	    escrever.writeObject(bits);
	    escrever.close();
	    fos.close();
	}
	
	public void escreverBitSet(BitSet bits, String texto) {
		for(int i = 0; i < texto.length() ; i++) {
			if(texto.charAt(i) == '0') {
				bits.clear(i);
			} else if(texto.charAt(i) == '1') {
				bits.set(i);
			}
		}
		bits.set(texto.length());
	}
	public int lerHash(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    entrada.readObject();
	    int hash = (int) entrada.readInt();
	    entrada.close();
	    return hash;
	}
	public No lerMapa(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    
	    No mapa = (No) entrada.readObject();
	    entrada.close();
	    return mapa;
	}
	
	public BitSet lerTexto(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream entrada = new ObjectInputStream(fis);
	    entrada.readObject();
	    entrada.readInt();
	    BitSet bits = (BitSet) entrada.readObject();
	    entrada.close();
	    return bits;		
	}

//	public void descompactar(File file, String nomeArq) throws ClassNotFoundException, IOException, ArvoreNulaException, ArquivoCorrompidoException {
//		FileInputStream fis = new FileInputStream(file);
//	    ObjectInputStream entrada = new ObjectInputStream(fis);
//	    int hashOriginal = entrada.readInt();
//	    System.out.println("Hash Original: " +hashOriginal);
//	    No mapa = (No) entrada.readObject();
//	    BitSet bits = (BitSet) entrada.readObject();
//	    entrada.close();
//		String txtDecod = decodificarTexto(mapa, bits);
//		int hashNova = funcaoHash(txtDecod);
//		System.out.println("Hash nova: " +hashNova);
//		verificarIntegridade(hashOriginal, hashNova);
//		//System.out.println("" + txtDecod);
//		File arquivo = new File(file.getPath().replace(".monter", "(descompactado)")); 
//		FileWriter fw = new FileWriter(arquivo);  
//		BufferedWriter bw = new BufferedWriter(fw);  
//		String txtAtualizado = txtDecod.replaceAll("\n", System.lineSeparator());
//		txtAtualizado = txtAtualizado.substring(0,txtAtualizado.length()-1);
//		bw.write(txtAtualizado);
//		bw.close();
//	}
	public void descompactar(File file, String nomeArq) throws ClassNotFoundException, IOException, ArvoreNulaException, ArquivoCorrompidoException {
	    
		int hashOriginal = lerHash(file);
	    No mapa = lerMapa(file);
	    BitSet bits = lerTexto(file);
	   
		String txtDecod = decodificarTexto(mapa, bits);
		int hashNova = funcaoHash(txtDecod);
		System.out.println("Hash Original: " +hashOriginal);
		System.out.println("Hash nova: " +hashNova);
		verificarIntegridade(hashOriginal, hashNova);
		//System.out.println("" + txtDecod);
		
		File arquivo = new File(file.getPath().replace(".monster", "")); 
		FileWriter fw = new FileWriter(arquivo);  
		BufferedWriter bw = new BufferedWriter(fw);  
		String txtAtualizado = txtDecod.replaceAll("\n", System.lineSeparator());
		txtAtualizado = txtAtualizado.substring(0,txtAtualizado.length()-1);
		bw.write(txtAtualizado);
		bw.close();
	}
	public void verificarIntegridade(int hashOriginal, int hashNova) throws ArquivoCorrompidoException {
		if(hashOriginal != hashNova) {
			//throw new ArquivoCorrompidoException();
		}
	}
}
