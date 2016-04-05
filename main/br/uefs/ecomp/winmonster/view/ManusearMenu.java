package br.uefs.ecomp.winmonster.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ManusearMenu implements ActionListener {
	
	private JMenuItem sobre, sair;
	private JLabel texto;
	private JFrame janela;

	public ManusearMenu(JMenuItem sobre, JMenuItem sair, JFrame janela){
		this.sobre = sobre;
		this.sair = sair;
		this.janela = janela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sobre){
			JPanel painel = new JPanel();
			texto = new JLabel("<html> V. 1.0 <br>O WinMonster é um software desenvolvido com a finalidade de comprimir e descomprimir arquivos de tamanhos variados.</html>");
			texto.setLocation(5, 0);
			texto.setSize(275, 100);
			texto.setVisible(true);
			painel.add(texto);
			painel.setLayout(null);
			painel.setBackground(Color.WHITE);
			
			JFrame novaJanela = new JFrame("Sobre");
			novaJanela.setSize(300, 150);
			novaJanela.setLocationRelativeTo(null);
			novaJanela.setResizable(false);
			novaJanela.add(painel);
			novaJanela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			novaJanela.setVisible(true);
		}
		if(e.getSource() == sair){
			janela.dispose();
		}
	}

}
