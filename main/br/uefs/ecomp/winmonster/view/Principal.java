package br.uefs.ecomp.winmonster.view;

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


public class Principal {
	
	public static void main(String [] args){
		GUI gui = new GUI();
		gui.InterfaceGrafica();		
		System.out.println(System.getProperty("file.encoding"));
	}
}
