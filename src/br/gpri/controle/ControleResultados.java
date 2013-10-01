package br.gpri.controle;

import activerecord.Elemento;
import activerecord.Regra;
import activerecord.Subregra;
import activerecord.TrechoEncontrado;
import br.gpri.janelas.JanelaCadastroRegra;
import br.gpri.janelas.JanelaResultados;
import br.gpri.nlp.Tagger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControleResultados extends Variaveis{
	
	Integer linha;
	int idResult;
	private JanelaResultados Janela;
	private List<List<TrechoEncontrado>> listaEncontrados;
	private List<Elemento> elementos;
	private List<Regra> regras;
	List<Subregra> subregras;
	private List<TrechoEncontrado> trechosTextoSelecionadoRegras = new ArrayList<TrechoEncontrado>();
	private List<TrechoEncontrado> trechosTextoSelecionadoSubregras = new ArrayList<TrechoEncontrado>();
	
	public ControleResultados(List<String> textos, List<List<TrechoEncontrado>> listaEncontrados){
		linha = 0;
		Janela = new JanelaResultados();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BotaoRegra.addActionListener(this.Cadastra);
		inicializaListas(textos);
		this.listaEncontrados = listaEncontrados;
		Janela.setLocationRelativeTo(null);
		Janela.TextoRegra.setEditable(false);
		Janela.TextoRegra.setLineWrap(true);
		Janela.TextoRegra.setWrapStyleWord(true);
		Janela.RegraTextoTrecho.setEditable(false);
		Janela.RegraTextoTrecho.setLineWrap(true);
		Janela.RegraTextoTrecho.setWrapStyleWord(true);
		Janela.ListaTextos.setSelectedIndex(linha);
		Janela.NumeroTexto.setText(linha.toString());
		Janela.AreaTexto.setEditable(false);
		Janela.AreaTexto.setLineWrap(true);
		Janela.AreaTexto.setWrapStyleWord(true);
		Janela.NumeroTexto.setEditable(false);
		Janela.DropDownTexto.addActionListener(this.DropDownListBox);
		Janela.BotaoComRegra.addActionListener(this.Comment);
		
	}
	
	private void inicializaListas(List<String> textos){
	
		Janela.ListaTextos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaTextos.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaTextos.addListSelectionListener(this.Textos);
		DefaultListModel listaTexto = new DefaultListModel();
		for(int i=0; i<textos.size(); i++){
				listaTexto.addElement(textos.get(i));
		}
		
		Janela.ListaTextos.setModel(listaTexto);
		
		

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

/*	protected void geraListaTexto(List<String> textos){
		int item=Janela.DropDownTexto.getSelectedIndex();
		DefaultListModel listaTexto = new DefaultListModel();
		Janela.DropDownTexto.setSelectedIndex(0);
		for(int i=0; i<textos.size(); i++){
				
				listaTexto.addElement(textos.get(i));
				
			}
		Janela.ListaTextos.setModel(listaTexto);
		Janela.ListaTextos.updateUI();
		Janela.ListaTextos.setSelectedIndex(0);
	}*/
	
	protected void geraListaRegras(){
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
		
		 ActionListener Cadastra = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JanelaCadRegra= new ControleCadastroRegra();
					JanelaCadRegra.abreJanela();
					
				}
			};
		

		ActionListener DropDownListBox = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				int item = Janela.DropDownTexto.getSelectedIndex();
				if (item==0){
				
				}
				else if (item==1){}
				else{}
				
			}
		};
		
		 ActionListener Comment = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JanelaComentario = new ControleComentario(idResult);
					JanelaComentario.abreJanela();
					
				}
			};
			
		ListSelectionListener Textos = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				limpaCaixasTexto();
				Janela.AreaTexto.setText((String) Janela.ListaTextos.getSelectedValue());
				int textoSelecionado=Janela.ListaTextos.getSelectedIndex();
				List<TrechoEncontrado> trechosTextoSelecionado = listaEncontrados.get(textoSelecionado);
				
				separaTrechos(trechosTextoSelecionado);
				linha = Janela.ListaTextos.getSelectedIndex();
				linha = linha+1;
				Janela.NumeroTexto.setText(linha.toString());
				geraListaRegras();
			}	
		};
		
		ListSelectionListener Regras = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent Regras) {
                    int regraSelecionada=Janela.ListaRegra.getSelectedIndex();      
                    if(regraSelecionada>=0){
                            limpaCaixasTexto();
                            TrechoEncontrado t = trechosTextoSelecionadoRegras.get(regraSelecionada);
                            idResult=t.getidResultado();
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
