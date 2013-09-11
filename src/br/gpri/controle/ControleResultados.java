package br.gpri.controle;

import activerecord.Regra;
import activerecord.TrechoEncontrado;
import br.gpri.janelas.JanelaResultados;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControleResultados extends Variaveis{
	
	private JanelaResultados Janela;
	private List<List<TrechoEncontrado>> listaEncontrados;
	
	private List<TrechoEncontrado> trechosTextoSelecionadoRegras = new ArrayList<TrechoEncontrado>();
	private List<TrechoEncontrado> trechosTextoSelecionadoSubregras = new ArrayList<TrechoEncontrado>();
	
	public ControleResultados(List<String> textos, List<List<TrechoEncontrado>> listaEncontrados){
		Janela = new JanelaResultados();
		Janela.BotaoOk.addActionListener(this.Ok);
		inicializaListas(textos);
		this.listaEncontrados = listaEncontrados;
		Janela.setLocationRelativeTo(null);
		Janela.TextoRegra.setEditable(false);
		Janela.TextoRegra.setLineWrap(true);
		Janela.TextoRegra.setWrapStyleWord(true);
		Janela.RegraTextoTrecho.setEditable(false);
		Janela.RegraTextoTrecho.setLineWrap(true);
		Janela.RegraTextoTrecho.setWrapStyleWord(true);
		
		
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
		
		Janela.ListaSubRegra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaSubRegra.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaSubRegra.addListSelectionListener(this.Subregras);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void limpaCaixasTexto(){
		Janela.TextoRegra.setText("");
		Janela.RegraTextoTrecho.setText("");
		Janela.TextoSubRegra.setText("");
		Janela.SubRegraTextoTrecho.setText("");
	}
	
	private void separaTrechos(List<TrechoEncontrado> trechosTextoSelecionado){
		trechosTextoSelecionadoRegras = new ArrayList<TrechoEncontrado>();
		trechosTextoSelecionadoSubregras = new ArrayList<TrechoEncontrado>();
		
		for(int i=0;i<trechosTextoSelecionado.size();i++){
			if(trechosTextoSelecionado.get(i).isSubregra())
				trechosTextoSelecionadoSubregras.add(trechosTextoSelecionado.get(i));
			else
				trechosTextoSelecionadoRegras.add(trechosTextoSelecionado.get(i));
		}
	}
	
	private void geraListaRegras(){
		DefaultListModel listaRegrasEncontrados = new DefaultListModel();
		for(int i=0;i<trechosTextoSelecionadoRegras.size();i++){
			listaRegrasEncontrados.addElement(trechosTextoSelecionadoRegras.get(i).getRegra().getPrevia());				
		}
		Janela.ListaRegra.setModel(listaRegrasEncontrados);
		Janela.ListaRegra.updateUI();
		Janela.ListaRegra.setSelectedIndex(0);
	}
	
	private void geraListaSubregras(int idRegra){
		DefaultListModel listaSubregrasEncontrados = new DefaultListModel();
		for(int i=0;i<trechosTextoSelecionadoSubregras.size();i++){
			if(trechosTextoSelecionadoSubregras.get(i).getSubregra().getIdRegra() == idRegra)
				listaSubregrasEncontrados.addElement(trechosTextoSelecionadoSubregras.get(i).getSubregra().getPrevia());				
		}
		Janela.ListaSubRegra.setModel(listaSubregrasEncontrados);
		Janela.ListaSubRegra.updateUI();
		Janela.ListaSubRegra.setSelectedIndex(0);
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
				limpaCaixasTexto();
					
				int textoSelecionado=Janela.ListaTexto.getSelectedIndex();
				List<TrechoEncontrado> trechosTextoSelecionado = listaEncontrados.get(textoSelecionado);
				separaTrechos(trechosTextoSelecionado);
				
				geraListaRegras();
			}	
		};
		
		ListSelectionListener Regras = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				int regraSelecionada=Janela.ListaRegra.getSelectedIndex();	
				if(regraSelecionada>=0){
					limpaCaixasTexto();
					TrechoEncontrado t = trechosTextoSelecionadoRegras.get(regraSelecionada);
					int idRegra = t.getRegra().getId();
					String textoTrecho = t.getRegra().getTexto();
					String textoRegra = t.getTrechoEncontrado();
					Janela.TextoRegra.setText(textoRegra);
					Janela.RegraTextoTrecho.setText(textoTrecho);
					
					geraListaSubregras(idRegra);
				}
			}
		};
		
		ListSelectionListener Subregras = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Subregras) {
				int subregraSelecionada=Janela.ListaSubRegra.getSelectedIndex();	
				if(subregraSelecionada>=0){
					TrechoEncontrado t = trechosTextoSelecionadoSubregras.get(subregraSelecionada);
					String textoTrecho= t.getSubregra().getTexto();
					String textoSubregra = t.getTrechoEncontrado();
					Janela.TextoSubRegra.setText(textoSubregra);
					Janela.SubRegraTextoTrecho.setText(textoTrecho);
				}
			}
		};
}
