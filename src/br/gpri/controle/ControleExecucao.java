package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

import activerecord.Conjunto;
import activerecord.Elemento;
import activerecord.Regra;
import activerecord.Resultados;
import activerecord.Subregra;
import activerecord.TrechoEncontrado;
import br.gpri.janelas.JanelaExecucao;
import br.gpri.nlp.Tagger;

public class ControleExecucao extends Variaveis{

	private JanelaExecucao Janela; 
	private List<Elemento> elementos = new ArrayList<Elemento>();
	private List<Conjunto> conjuntos = new ArrayList<Conjunto>();
	private List<Regra> regras;
	private JCheckBox[] checkbox;
	
	public ControleExecucao(){
		
		Janela = new JanelaExecucao();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BOTAOVOLTAR.addActionListener(this.Voltar);
		Janela.BotaoTrocar.addActionListener(this.Trocar);
		Janela.setLocationRelativeTo(null);
		
		buscaDropDownElementos();
		buscaDropDownConjuntos();
		
		criaTabela();
	}
	
	private void criaTabela(){
		int indexConjunto = Janela.DropDownlistboxConjunto.getSelectedIndex();
		int indexElemento = Janela.DropDownlistboxElementos.getSelectedIndex();
		
		int idConjunto = 0;
		int idElemento = 0;
		if(indexConjunto != 0)
			idConjunto = conjuntos.get(indexConjunto).getId();
		if(indexElemento != 0)
			idElemento = elementos.get(indexElemento).getId();
		
		regras = BD.selectRegraExecucao(idUsuario,idConjunto,idElemento);
		DefaultTableModel tabela = new DefaultTableModel() {  
	    @Override  
	    public Class<?> getColumnClass(int column) {  
	        if (column == 0)  
	            return Boolean.class;  
	        return super.getColumnClass(column);  
	    }  };
	checkbox = new JCheckBox[regras.size()];
	for(int i=0; i<checkbox.length;i++){
		checkbox[i]=new JCheckBox();
		
	}
	
	tabela.addColumn("Sele��o");
	tabela.addColumn("Texto");
	tabela.addColumn("Regra");
	tabela.addColumn("Conjunto");
	tabela.addColumn("Elemento");
	 
	for(int i=0; i<regras.size();i++){
		Regra r = regras.get(i);
		String elemento = "";
		String conjunto = " ";
		for(int j=0; j<elementos.size(); j++)
			if(r.getElemento() == elementos.get(j).getId()){
				elemento = elementos.get(j).getNome();
				break;
			}
		for(int k=0; k<conjuntos.size(); k++)
			if(r.getConjunto() == conjuntos.get(k).getId()){
				conjunto = conjuntos.get(k).getNome();
				break;
			}
		
		Object[] o={true,r.getTexto(), r.getPrevia(), conjunto, elemento};
		tabela.addRow(o);
		
	}
	Janela.TabelaExecucao.setModel(tabela);
		
		
	}
	
	private void buscaDropDownElementos(){
		elementos = BD.selectElemento();
		Elemento todos = new Elemento();
		todos.setId(0);
		todos.setNome("TODOS");
		todos.setDescricao("Todos os elementos");
		elementos.add(0, todos);
		
		DefaultComboBoxModel lista = new DefaultComboBoxModel();
		for(int i=0; i<elementos.size(); i++){
			lista.addElement(elementos.get(i).getNome());
		}
		Janela.DropDownlistboxElementos.setModel(lista); 
		Janela.DropDownlistboxElementos.addActionListener(this.DropDownListBoxElem);
		if(!elementos.isEmpty())
			Janela.DropDownlistboxElementos.setSelectedIndex(0);
	}
	
	private void buscaDropDownConjuntos(){
		conjuntos = BD.selectConjunto();
		Conjunto todos = new Conjunto();
		todos.setId(0);
		todos.setNome("TODOS");
		conjuntos.add(0, todos);
		
		DefaultComboBoxModel lista = new DefaultComboBoxModel();
		for(int i=0; i<conjuntos.size(); i++){
			lista.addElement(conjuntos.get(i).getNome());
		}
		
		Janela.DropDownlistboxConjunto.setModel(lista); 
		Janela.DropDownlistboxConjunto.addActionListener(this.DropDownListBoxConj);
		if(!conjuntos.isEmpty())
			Janela.DropDownlistboxConjunto.setSelectedIndex(0);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	
	//if(checkbox[i].isSelected())
	
	private List<Integer> pegaSelecaoRegras(){
		List<Integer> indiceRegras = new ArrayList<Integer>();
		for(int i=0; i<Janela.TabelaExecucao.getModel().getRowCount(); i++){
			boolean a = (Boolean) Janela.TabelaExecucao.getModel().getValueAt(i,0);
			if(a)			
				indiceRegras.add(i);
		}
		return indiceRegras;
	}
	

	private List<Regra> buscaRegrasSelecionadas(List<Integer> indiceRegras){
		
		List<Regra> regrasSelecionadas = new ArrayList<Regra>();
		
		for(int i=0; i<indiceRegras.size(); i++){
			Regra r = regras.get(indiceRegras.get(i)); //Pega cada uma das regras
			r = BD.selectTermoRegra(r); //Busca os termos dessa regra
			
			List<Subregra> subregras = new ArrayList<Subregra>();  
			subregras = BD.selectSubRegras(r); //Busca suas subregras
			if(!(subregras.isEmpty()))
				for(int j=0; j<subregras.size(); j++){
					Subregra s = subregras.get(j);	//Busca os termos da subregra e atualiza na lista
					s = BD.selectTermoSubregra(s);
					subregras.set(j,s);			
				}
			r.setSubregras(subregras);
			regrasSelecionadas.add(r);
		}
		return regrasSelecionadas;		
	}
	
	 ActionListener Voltar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();
				JanelaArquivo.abreJanela();
				
			}
		};
	
		 ActionListener Ok = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Tagger Tagger = new Tagger(BD);
					
					
					List<Integer> indiceRegras = pegaSelecaoRegras(); //Verifica os checkbox
					List<Regra> regrasSelecionadas = buscaRegrasSelecionadas(indiceRegras); //Busca as regras selecionadas
					int numTextos = BD.getNumTextos(idUsuario, idArquivo);
					
					List<String> textos = new ArrayList<String>();
					
					List<Resultados> listaResultados = new ArrayList<Resultados>();
					//List<List<TrechoEncontrado>> listaEncontrados = new ArrayList<List<TrechoEncontrado>>();
					
					int idExecucao = BD.insertExecucao(idUsuario,idArquivo); //Cria a inst�ncia da Execucao
					BD.insertRegrasExecucao(idExecucao, regrasSelecionadas); //Armazena regras usadas na execucao
					
					for(int i=0; i<numTextos; i++){ //Pra cada texto, executa
						String texto = BD.selectTexto(idUsuario, idArquivo, i);
						boolean isEncontrado;
						
						List<TrechoEncontrado> encontrados = Tagger.executaRegra(texto, regrasSelecionadas);
						
						//Console
						System.out.println("Texto "+(i+1));
						System.out.println(texto);
						System.out.println();
						for(int j=0; j<encontrados.size(); j++){
							System.out.println("Trecho Encontrado: "+encontrados.get(j).getTrechoEncontrado());
							if(encontrados.get(j).isSubregra()){
								System.out.println("subRegra: "+encontrados.get(j).getSubregra().getPrevia());
							}
							else{
								System.out.println("Regra: "+encontrados.get(j).getRegra().getPrevia());								
							}
							
							System.out.println();
						}
						
						Resultados ResultadoTexto = new Resultados();
						
						//Caso n�o tenha encontrado nenhum trecho adiciona "Nada encontrado"
						if(encontrados.isEmpty()){
							TrechoEncontrado t = new TrechoEncontrado();
							Regra r = new Regra();
							r.setPrevia("Nada Encontrado");
							r.setTexto("Nada Encontrado");
							t.setRegra(r);
							t.setTrechoEncontrado("Nada Encontrado");
							t.setHasRegra(false); //Verificar pra tirar isso aqui
							ResultadoTexto.addTrecho(t);
							ResultadoTexto.setIsEncontrado(false);
						}
						
						else{
							ResultadoTexto.setTrechos(encontrados);
							ResultadoTexto.setIsEncontrado(true);
						}
						
						
						ResultadoTexto.setTexto(texto);
						
												
						listaResultados.add(ResultadoTexto);
						
						//textos.add(texto);
						//listaEncontrados.add(encontrados);
						
						//Insere no Banco de Dados - Insere regra seguido de suas subregras
						BD.insertResultados(ResultadoTexto, i, idExecucao);
					}
					
					
					fechaJanela();
					JanelaResultados = new ControleResultados(listaResultados);
					JanelaResultados.abreJanela();
					
				}
		 };
		
		 ActionListener Trocar = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			};
			
	
		ActionListener DropDownListBoxElem = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				int item = Janela.DropDownlistboxElementos.getSelectedIndex();
				String tooltip = elementos.get(item).getDescricao();
				Janela.DropDownlistboxElementos.setToolTipText(tooltip);
				
				criaTabela();
			}
		};

		ActionListener DropDownListBoxConj = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				criaTabela();
			}
		};
}
