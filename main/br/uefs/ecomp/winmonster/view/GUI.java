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
		botaoCompactar = new JButton("Compactar");
		botaoDescompactar = new JButton("Descompactar");
		sobre = new JMenuItem("Sobre");
		sair = new JMenuItem("Sair");
		opcao = new JMenu("Opções");
		opcao.add(sobre);
		opcao.add(sair);
		menu = new JMenuBar();
		menu.add(opcao);
		splash = new Splash();
		botaoCompactar.setBounds(80, 40, 120, 50);
		botaoDescompactar.setBounds(80, 100, 120, 50);
		
		JPanel painel = new JPanel();
		painel.setLayout(null);
		painel.setBackground(Color.white);
		painel.add(botaoCompactar);
		painel.add(botaoDescompactar);
		
		JFrame janela = new JFrame("WinMonster");
		janela.setSize(300, 250);
		janela.setLocationRelativeTo(null);
		janela.setResizable(false);
		janela.setJMenuBar(menu);
		janela.add(painel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		manusearBotao = new ManusearBotao(botaoCompactar, botaoDescompactar);
		botaoCompactar.addActionListener(manusearBotao);
		botaoDescompactar.addActionListener(manusearBotao);
		manusearMenu = new ManusearMenu(sobre, sair, janela);
		sobre.addActionListener(manusearMenu);
		sair.addActionListener(manusearMenu);
		
		splash.showSplash();
		janela.setVisible(true);

	}
}
