package br.uefs.ecomp.winmonster.view;

/******************************************************************************* 
Autor: Victor Munduruca e Diego Louren�o
Componente Curricular: MI Algoritmos II
Concluido em: 07/04/2016
Declaro que este c�digo foi elaborado por mim de forma individual e n�o cont�m nenhum 
trecho de c�digo de outro colega ou de outro autor, tais como provindos de livros e  
apostilas, e p�ginas ou documentos eletr�nicos da Internet. Qualquer trecho de c�digo 
de outra autoria que n�o a minha est� destacado com uma cita��o para o autor e a fonte 
do c�digo, e estou ciente que estes trechos n�o ser�o considerados para fins de avalia��o. 
******************************************************************************************/ 


public class Principal {
	
	public static void main(String [] args){
		GUI gui = new GUI();
		gui.InterfaceGrafica();		
		System.out.println(System.getProperty("file.encoding"));
	}
}
