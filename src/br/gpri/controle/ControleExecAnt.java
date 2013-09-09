package br.gpri.controle;

import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import activerecord.Execucao;
import br.gpri.janelas.JanelaExecAnt;

public class ControleExecAnt extends Variaveis{
	
	private JanelaExecAnt Janela;

	public ControleExecAnt(){
		Janela = new JanelaExecAnt();
		List<Execucao> execucoes = BD.selectExecucoes(idUsuario);
		geraListaExecucoes(execucoes);
		Janela.setLocationRelativeTo(null);

	}
	
	public ControleExecAnt(int idArquivo){
		Janela = new JanelaExecAnt();
		List<Execucao> execucoes = BD.selectExecucoes(idArquivo, idArquivo);
		geraListaExecucoes(execucoes);
		Janela.setLocationRelativeTo(null);

	}
	
	
	private void geraListaExecucoes(List<Execucao> execucoes){
		DefaultTableModel tabela = new DefaultTableModel();
		tabela.addColumn("Data / Hora");
		tabela.addColumn("Arquivo");
		
		if(execucoes.isEmpty()){
			Object[] o = {"Nenhuma Execução",""};
			tabela.addRow(o);
			}
		else{
			for(int i=0; i<execucoes.size(); i++){
				Execucao e = execucoes.get(i);
				Object[] o = {e.getData(),e.getArquivo()};
				tabela.addRow(o);
			}
		}
		
		Janela.tabelaExec.setModel(tabela);
		Janela.tabelaExec.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
}
