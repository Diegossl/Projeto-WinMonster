package br.uefs.ecomp.winmonster.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.uefs.ecomp.winmonster.controller.AdministradorController;
import br.uefs.ecomp.winmonster.exceptions.ArquivoCorrompidoException;
import br.uefs.ecomp.winmonster.exceptions.ArvoreNulaException;
import br.uefs.ecomp.winmonster.exceptions.FilaNulaException;
import br.uefs.ecomp.winmonster.util.Fila;
import br.uefs.ecomp.winmonster.util.No;

public class ManusearBotao implements ActionListener {

	private JButton compactar, descompactar;
	private AdministradorController controllerAdm;

	public ManusearBotao(JButton compactar, JButton descompactar) {
		AdministradorController.zerarSingleton();
		controllerAdm = AdministradorController.getInstance();
		this.compactar = compactar;
		this.descompactar = descompactar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == compactar){
			JFileChooser fc = new JFileChooser(); 
//			FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Arquivos de texto", "txt", "Arquivo de c++", "cpp", "Arquivos html", "html");
			FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Arquivos de texto", "txt");
			FileNameExtensionFilter filtroCpp = new FileNameExtensionFilter("Arquivo de c++", "cpp");
			FileNameExtensionFilter filtroHtml = new FileNameExtensionFilter("Arquivos html", "html");
			fc.setFileFilter(filtroTxt);
			fc.setFileFilter(filtroCpp);
			fc.setFileFilter(filtroHtml);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY); 
			fc.setDialogTitle("Selecionar Arquivo"); 
			
			int resposta = fc.showOpenDialog(null);
			if (resposta == JFileChooser.APPROVE_OPTION) { 
				File arquivo = fc.getSelectedFile();
				String nomeArquivo = fc.getSelectedFile().getName();
				String caminho = arquivo.getPath().replace(nomeArquivo, "");
				try{
					Fila fila = controllerAdm.filaDeFrequencias(arquivo);
					No raiz = controllerAdm.construirArvore(fila);
					controllerAdm.getHuff().mapeamento(raiz);
					String textoCod = controllerAdm.codificarTexto(controllerAdm.getHuff().getTextoOriginal());
					controllerAdm.compactar(raiz, textoCod, caminho, nomeArquivo, controllerAdm.getHuff().getTextoOriginal());
					JOptionPane.showMessageDialog(null, "Compactação realizada com sucesso!");

				}catch(IOException e2){
					e2.printStackTrace();
				} catch (FilaNulaException e3) {
					e3.printStackTrace();
				} catch (ArvoreNulaException e4) {
					e4.printStackTrace();
				}
			}
		}
		if(e.getSource() == descompactar){
			JFileChooser fc = new JFileChooser(); //cria um novo selecionador de arquivos
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY); //configura o selecionador para só receber arquivos
			fc.setDialogTitle(" Selecionar Arquivo "); //define o título da janela de seleção
			FileNameExtensionFilter filtroComp = new FileNameExtensionFilter("Arquivos comprimidos winMonster", "monster");
			fc.setFileFilter(filtroComp);
			int resposta = fc.showOpenDialog(null); //abre a janela de seleção e guarda a ação do usuário em resposta
			if (resposta == JFileChooser.APPROVE_OPTION) { 
				File arquivo = fc.getSelectedFile();
				String nomeArquivo = fc.getSelectedFile().getName();
				try {
					controllerAdm.descompactar(arquivo, nomeArquivo);
					JOptionPane.showMessageDialog(null, "Descompactação realizada com sucesso!");

				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ArvoreNulaException e1) {
					e1.printStackTrace();
				} catch (ArquivoCorrompidoException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Arquivo corrompido :(");
					e1.printStackTrace();
				}

			}
		}
	}
}
