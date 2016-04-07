package br.uefs.ecomp.winmonster.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI {
	
	private JButton botaoCompactar, botaoDescompactar;
	private ManusearMenu manusearMenu;
	private ManusearBotao manusearBotao;
	private JMenuBar menu;
	private JMenu opcao;
	private JMenuItem sobre, sair;
	private Splash splash;
	
	public GUI(){
		
	}
	
	public void InterfaceGrafica(){
		botaoCompactar = new JButton("Compactar");//Crio um botao para descompactar
		botaoDescompactar = new JButton("Descompactar");// Crio para o menu a op��o Sobre
		sobre = new JMenuItem("Sobre");// Crio para o menu a op��o Sair
		sair = new JMenuItem("Sair");//Crio um menu de op��es
		opcao = new JMenu("Op��es");//Adiciono a op��o Sobre ao novo menu
		opcao.add(sobre);//Adiciono a op��o Sobre ao menu novo menu
		opcao.add(sair);//Adiciono a op��o Sair ao menu novo menu
		menu = new JMenuBar();//Crio o Menu
		menu.add(opcao);//Adiciono as op��es ao Menu
		splash = new Splash();//Instancio uma tela de Splash
		/*Dimensiono os bot�es*/
		botaoCompactar.setBounds(80, 40, 120, 50);
		botaoDescompactar.setBounds(80, 100, 120, 50);
		
		//Crio um painel e fa�o as configura��es
		JPanel painel = new JPanel();
		painel.setLayout(null);
		painel.setBackground(Color.white);
		painel.add(botaoCompactar);
		painel.add(botaoDescompactar);
		
		//Crio a janela e fa�o as configura��es
		JFrame janela = new JFrame("WinMonster");
		janela.setSize(300, 250);
		janela.setLocationRelativeTo(null);
		janela.setResizable(false);
		janela.setJMenuBar(menu);
		janela.add(painel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Crio uma instancia de ManusearBotao para passar as a��es do bot�es
		manusearBotao = new ManusearBotao(botaoCompactar, botaoDescompactar);
		//Passo o objeto ManusearBotao para o botao, para assim o metodo decidir o que fzer.
		botaoCompactar.addActionListener(manusearBotao);
		botaoDescompactar.addActionListener(manusearBotao);
		
		//Mesmo procedimento do Botao
		manusearMenu = new ManusearMenu(sobre, sair, janela);
		sobre.addActionListener(manusearMenu);
		sair.addActionListener(manusearMenu);
		
		splash.showSplash();//Exibo o splash screenn
		janela.setVisible(true);//Torno visivel a tela

	}
}
