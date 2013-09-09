package br.gpri.controle;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import activerecord.Execucao;
import br.gpri.janelas.JanelaExecAnt;

public class ControleExecAnt extends Variaveis{
	
	private JanelaExecAnt Janela;

	public ControleExecAnt(){
		Janela = new JanelaExecAnt();
		List<Execucao> execucoes = BD.selectExecucoes(idUsuario);
		geraListaExecucoes(execucoes);
	}
	
	public ControleExecAnt(int idArquivo){
		Janela = new JanelaExecAnt();
		List<Execucao> execucoes = BD.selectExecucoes(idArquivo, idArquivo);
		geraListaExecucoes(execucoes);
	}
	
	
	private void geraListaExecucoes(List<Execucao> execucoes){
		DefaultTableModel tabela = new DefaultTableModel();
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
}
