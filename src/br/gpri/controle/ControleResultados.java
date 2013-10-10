package br.gpri.controle;

import activerecord.Elemento;
import activerecord.Regra;
import activerecord.Resultados;
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
	int idResultSR;
	private JanelaResultados Janela;
	private List<Resultados> listaResultados;
	private List<Resultados> listaResultadosSelecionados;
	private List<Elemento> elementos;
	private List<Regra> regras;
	List<Subregra> subregras;
	private List<TrechoEncontrado> trechosTextoSelecionadoRegras = new ArrayList<TrechoEncontrado>();
	private List<TrechoEncontrado> trechosTextoSelecionadoSubregras = new ArrayList<TrechoEncontrado>();
	
	public ControleResultados(List<Resultados> listaResultados){
		linha = 0;
		this.listaResultados = listaResultados;
		
		Janela = new JanelaResultados();
		Janela.setLocationRelativeTo(null);
		
		//ActionListener dos Botões
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BotaoRegra.addActionListener(this.Cadastra);
		
	
		
		//Caixa de Texto Regra
		Janela.TextoRegra.setEditable(false);
		Janela.TextoRegra.setLineWrap(true);
		Janela.TextoRegra.setWrapStyleWord(true);
		
		//Caixa de Texto Trecho Regra
		Janela.RegraTextoTrecho.setEditable(false);
		Janela.RegraTextoTrecho.setLineWrap(true);
		Janela.RegraTextoTrecho.setWrapStyleWord(true);
		
		//Caixa de Texto Subregra
		Janela.TextoSubRegra.setEditable(false);
		Janela.TextoSubRegra.setLineWrap(false);
		Janela.TextoSubRegra.setWrapStyleWord(true);
		
		//Caixa de Texto Trecho Subregra
		Janela.SubRegraTextoTrecho.setEditable(false);
		Janela.SubRegraTextoTrecho.setLineWrap(true);
		Janela.SubRegraTextoTrecho.setWrapStyleWord(true);
		
		//Referente aos Textos
		Janela.NumeroTexto.setText(linha.toString());
		Janela.AreaTexto.setEditable(false);
		Janela.AreaTexto.setLineWrap(true);
		Janela.AreaTexto.setWrapStyleWord(true);
		Janela.NumeroTexto.setEditable(false);
		
		//DropDownListBox filtro de textos
		Janela.DropDownTexto.setSelectedIndex(0);
		Janela.DropDownTexto.addActionListener(this.DropDownListBox);

		//Botões de Comentário
		Janela.BotaoComRegra.addActionListener(this.Comment);
		Janela.BotaoComSubRegra.addActionListener(this.CommentSubRegra);
		
		//Gera Lista dos textos
		inicializaListas();
		
	}
	
	private void inicializaListas(){
		
		Janela.ListaTextos.setSelectedIndex(linha);
		Janela.ListaTextos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaTextos.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaTextos.addListSelectionListener(this.Textos);
	
		geraListaResultados();

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
	
	private void geraListaResultados(){
		int filtro = Janela.DropDownTexto.getSelectedIndex(); //Pega a seleção do DropDown
		listaResultadosSelecionados = new ArrayList<Resultados>();
		List<String> textos = new ArrayList<String>();
		
		//Compara e gera a lista para cada caso
		if(filtro == 0){ //Todos
			listaResultadosSelecionados = listaResultados;
			for(int i=0; i<listaResultados.size(); i++){
				textos.add(listaResultados.get(i).getTexto());
			}
		}
		else if(filtro == 1){ //Encontrados
			for(int i=0; i<listaResultados.size(); i++){
				Resultados r = listaResultados.get(i);
				if(r.isEncontrado()){
					listaResultadosSelecionados.add(r);
					textos.add(r.getTexto());
				}
			}
		}
		else if(filtro == 2){ //Não encontrados
			for(int i=0; i<listaResultados.size(); i++){
				Resultados r = listaResultados.get(i);
				if(!r.isEncontrado()){
					listaResultadosSelecionados.add(r);
					textos.add(r.getTexto());
				}
			}
		}
		else
			System.out.println("Problema com o DropDownListBox do filtro de textos");
		
		//Atualiza lista na Janela
		DefaultListModel listaTexto = new DefaultListModel();
		for(int i=0; i<textos.size(); i++){
				listaTexto.addElement(textos.get(i));
		}
		
		Janela.ListaTextos.setModel(listaTexto);
		
		Janela.ListaTextos.updateUI();
		Janela.ListaTextos.setSelectedIndex(0);
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
					JanelaCadRegra= new ControleCadastroRegra(false, linha-1);
					JanelaCadRegra.abreJanela();
					
				}
			};
		

		ActionListener DropDownListBox = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				geraListaResultados();
			}
		};
		
		 ActionListener Comment = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JanelaComentario = new ControleComentario(idResult);
					JanelaComentario.abreJanela();
					
				}
			};
			 ActionListener CommentSubRegra = new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JanelaComentario = new ControleComentario(idResultSR);
						JanelaComentario.abreJanela();
						
					}
				};
				
		ListSelectionListener Textos = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent Regras) {
				limpaCaixasTexto();
				Janela.AreaTexto.setText((String) Janela.ListaTextos.getSelectedValue());
				int textoSelecionado=Janela.ListaTextos.getSelectedIndex();
				if(textoSelecionado == -1){
					textoSelecionado = 0;
				}	
				
				List<TrechoEncontrado> trechosTextoSelecionado = listaResultados.get(textoSelecionado).getTrechos();
				//List<TrechoEncontrado> trechosTextoSelecionado = listaEncontrados.get(textoSelecionado);
				
				separaTrechos(trechosTextoSelecionado);
				linha = Janela.ListaTextos.getSelectedIndex();
				Integer l = linha + 1;
				Janela.NumeroTexto.setText(l.toString());
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
                            idResultSR=t.getidResultado();
                            Janela.TextoSubRegra.setText(textoSubregra);
                            Janela.SubRegraTextoTrecho.setText(textoTrecho);
                    }
                    
            }
    };
		
	

}
