package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import activerecord.Execucao;
import activerecord.TrechoEncontrado;
import br.gpri.janelas.JanelaExecAnt;

public class ControleExecAnt extends Variaveis{
	
	private JanelaExecAnt Janela;
	List<Execucao> execucoes;

	public ControleExecAnt(){
		Janela = new JanelaExecAnt();
		execucoes = BD.selectExecucoes(idUsuario);
		geraListaExecucoes(execucoes);
		
		Janela.ABotaoOk.addActionListener(this.OK);
		Janela.ABotaoVoltar.addActionListener(this.Volta);
		Janela.setLocationRelativeTo(null);
	}
	
	public ControleExecAnt(int idArquivo){
		Janela = new JanelaExecAnt();
		List<Execucao> execucoes = BD.selectExecucoes(idArquivo, idArquivo);
		geraListaExecucoes(execucoes);
		
		Janela.ABotaoOk.addActionListener(this.OK);
		Janela.ABotaoVoltar.addActionListener(this.Volta);
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
	
	ActionListener Volta = new ActionListener() {
        public void actionPerformed(ActionEvent Volta) {
        	fechaJanela();
        	JanelaArquivo = new ControleArquivo();
        	JanelaArquivo.abreJanela();
		}
    };
    
	ActionListener OK = new ActionListener() {
        public void actionPerformed(ActionEvent OK) {
        	int index = Janela.tabelaExec.getSelectedRow();
    		if(index < 0)
    			System.out.println("Selecione uma execução");
    		else{
    			Execucao execucao = execucoes.get(index);
    			int idExecucao = execucao.getId();
    			int idArquivo = execucao.getIdArquivo();
    			List<String> textos = BD.selectTextos(idUsuario, idArquivo);
    			List<List<TrechoEncontrado>> listaResultados = BD.selectResultados(idExecucao, idArquivo, idUsuario);
    			
    			fechaJanela();
    			JanelaResultados = new ControleResultados(textos, listaResultados);
    			JanelaResultados.abreJanela();
    		}
        	
        	
        	
        }
	};  
	
	
}
