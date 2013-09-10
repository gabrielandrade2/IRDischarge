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
	private List<TrechoEncontrado> trechosTextoSelecionado;
	
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
		Janela.ListaSubRegra.addListSelectionListener(null);
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
				Janela.TextoRegra.setText("");
				Janela.RegraTextoTrecho.setText("");
				DefaultListModel listaRegrasEncontrados = new DefaultListModel();
				int textoSelecionado=Janela.ListaTexto.getSelectedIndex();
				trechosTextoSelecionado = listaEncontrados.get(textoSelecionado);
				for(int i=0;i<trechosTextoSelecionado.size();i++){
					if(!trechosTextoSelecionado.get(i).isSubregra())
						listaRegrasEncontrados.addElement(trechosTextoSelecionado.get(i).getRegra().getPrevia());				
				}
				Janela.ListaRegra.setModel(listaRegrasEncontrados);
				Janela.ListaRegra.updateUI();
				Janela.ListaRegra.setSelectedIndex(0);

			}	
		};
		
		ListSelectionListener Regras = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				int regraselecionada=Janela.ListaRegra.getSelectedIndex();	
				if(regraselecionada>=0){
					TrechoEncontrado t = trechosTextoSelecionado.get(regraselecionada);
					String textoregra = t.getRegra().getTexto();
					String textotrecho = t.getTrechoEncontrado();
					Janela.TextoRegra.setText(textoregra);
					Janela.RegraTextoTrecho.setText(textotrecho);
				}
			}
		};
}
