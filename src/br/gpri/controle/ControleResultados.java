package br.gpri.controle;

import activerecord.Regra;
import activerecord.TrechoEncontrado;
import br.gpri.janelas.JanelaResultados;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControleResultados extends Variaveis{
	
	private JanelaResultados Janela;
	private List<List<TrechoEncontrado>> listaEncontrados;
	public ControleResultados(List<String> textos, List<List<TrechoEncontrado>> listaEncontrados){
		Janela = new JanelaResultados();
		Janela.BotaoOk.addActionListener(this.Ok);
		inicializaListas(textos);
		this.listaEncontrados = listaEncontrados;
		
		
	}
	
	private void inicializaListas(List<String> textos){
		Janela.ListaTexto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaTexto.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaTexto.addListSelectionListener(this.Textos);
		DefaultListModel listaTexto = new DefaultListModel();
		for(int i=0; i<textos.size(); i++){
			listaTexto.addElement(textos.get(i));
			
		}
		Janela.ListaTexto.setModel(listaTexto);
		
		Janela.ListaRegra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaRegra.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaRegra.addListSelectionListener(this.Regras);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	 ActionListener Ok = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();
				JanelaArquivo = new ControleArquivo();
				JanelaArquivo.abreJanela();
				
			}
		};
		ListSelectionListener Textos = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				DefaultListModel listaRegrasEncontrados = new DefaultListModel();
				int textoselecionado=Janela.ListaTexto.getSelectedIndex();
				List<TrechoEncontrado> trechos = listaEncontrados.get(textoselecionado);
				for(int i=0;i<trechos.size();i++){
					listaRegrasEncontrados.addElement(trechos.get(i).getRegra().getPrevia());
										
				}
				Janela.ListaRegra.setModel(listaRegrasEncontrados);
				Janela.ListaRegra.updateUI();
			}	
		};
		
		ListSelectionListener Regras = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				
			}
		};
}
