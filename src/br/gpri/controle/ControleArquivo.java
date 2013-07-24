package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.gpri.janelas.JanelaArquivo;


public class ControleArquivo extends Principal {

	boolean Cadastrar = false;
	boolean Executar = false;
	JanelaArquivo Janela;
	
	public ControleArquivo(){
		Janela = new JanelaArquivo();
		Janela.ABotaoOk.addActionListener(this.OK);
		Janela.ABotaoVoltar.addActionListener(this.Volta);
		Janela.ACadastrarRadio.addActionListener(this.Cad);
		Janela.AExecutarRadio.addActionListener(this.Exec);
	/*	Janela.BotaoAbrir.addActionListener(this.Abre);
		Janela.AbreArquivo.setFileFilter(new FileNameExtensionFilter("Arquivo do Sumário/Laudos do Excel","xls"));
        Janela.AbreArquivo.setAcceptAllFileFilterUsed(false);*/
		geraListaArquivos();
		
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	public void geraListaArquivos(){
		//Fazer conexão banco de dados
		Janela.AList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
	}
	
	ActionListener Volta = new ActionListener() {
        public void actionPerformed(ActionEvent Volta) {
		fechaJanela();
		JanelaLogin.abreJanela();
		}
    };
    
	ActionListener OK = new ActionListener() {
        public void actionPerformed(ActionEvent OK) {
		if(Executar){
        	fechaJanela();
        	JanelaExecucao.abreJanela();}
        else if(Cadastrar){
        	fechaJanela();
        	JanelaCadRegra.abreJanela();}
        else
        	System.out.println("Selecione uma opção");
		}
    };  
	
	ActionListener Cad = new ActionListener() {
        public void actionPerformed(ActionEvent Cad) {
        	Cadastrar = true;
        	Executar = false;
		}
    };  
    
	ActionListener Exec = new ActionListener() {
        public void actionPerformed(ActionEvent Exec) {
        	Cadastrar = false;
        	Executar = true;
		}
    }; 
    /*
    ActionListener Abre = new ActionListener() {
        public void actionPerformed(ActionEvent Abre) {
        		int retorno = Janela.AbreArquivo.showOpenDialog(null);
        		if (retorno == JFileChooser.APPROVE_OPTION){
        			ControleExcel = Janela.AbreArquivo.getSelectedFile().getAbsolutePath();
        			TextoCaminhoArquivo.setText(Interface.caminho);
        			System.out.println(Interface.caminho);
        			ControleExcel();
        			TextoSumario.setText(Interface.celula.getContents());
        			//Fazer conexão banco de dados
        			}
        		else if(retorno == JFileChooser.CANCEL_OPTION)
        			System.out.println("Usuário cancelou a operação");
        		else
        			System.out.println("Erro ao abrir o arquivo");
        }
	};*/
}
