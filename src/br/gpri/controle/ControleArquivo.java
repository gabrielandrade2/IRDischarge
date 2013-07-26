package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import activerecord.Arquivo;
import br.gpri.janelas.JanelaArquivo;

public class ControleArquivo extends Variaveis {

	private String caminhoArquivo = new String();
	private boolean Cadastrar = false;
	private boolean Executar = false;
	private JanelaArquivo Janela;
	private Stack<Arquivo> arquivosRecentes = new Stack();
	
	public ControleArquivo(){
		Janela = new JanelaArquivo();
		Janela.ABotaoOk.addActionListener(this.OK);
		Janela.ABotaoVoltar.addActionListener(this.Volta);
		Janela.ACadastrarRadio.addActionListener(this.Cad);
		Janela.AExecutarRadio.addActionListener(this.Exec);
		Janela.BotaoAbrir.addActionListener(this.Abre);
		Janela.AbreArquivo.setFileFilter(new FileNameExtensionFilter("Arquivo do Sumário/Laudos do Excel","xls"));
        Janela.AbreArquivo.setAcceptAllFileFilterUsed(false);
		geraListaArquivos();
		Janela.setLocationRelativeTo(null);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void geraListaArquivos(){
		arquivosRecentes = BD.selectArquivos(idUsuario);
		for(int i=0; i<arquivosRecentes.size(); i++)
			Janela.lista.addElement(arquivosRecentes.elementAt(i).getNome());
		
		Janela.AList.setModel(Janela.lista);
		Janela.AList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.AList.setLayoutOrientation(JList.VERTICAL);
		Janela.AList.setVisibleRowCount(10);
		Janela.AList.addMouseListener(this.Lista);
		}
	
		public void adicionaArquivoRecente(Arquivo a){
			testaArquivoRecenteExiste(a);
			
			if(arquivosRecentes.size() >= 10)
				arquivosRecentes.remove(10);
			arquivosRecentes.insertElementAt(a, 0);
			Janela.lista.insertElementAt(a.getNome(), 0);
			Janela.AList.setSelectedIndex(0);
			Janela.AList.ensureIndexIsVisible(0);
			caminhoArquivo = a.getCaminho();
		}
	
		public void testaArquivoRecenteExiste(Arquivo a){
			for(int i=0; i<arquivosRecentes.size(); i++){
				String x = a.getCaminho();
				String y = arquivosRecentes.elementAt(i).getCaminho();
				if(x.contentEquals(y))
					arquivosRecentes.remove(i);
				}
		}
		

	MouseListener Lista = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			int selecionado = Janela.AList.getSelectedIndex();
			Arquivo a = arquivosRecentes.elementAt(selecionado);
			testaArquivoRecenteExiste(a);
			if(arquivosRecentes.size() >= 10)
				arquivosRecentes.remove(10);
			arquivosRecentes.insertElementAt(a, 0);
			caminhoArquivo = a.getCaminho();
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};
		
		
	ActionListener Volta = new ActionListener() {
        public void actionPerformed(ActionEvent Volta) {
		fechaJanela();
		JanelaLogin = new ControleLogin();
		JanelaLogin.abreJanela();
		}
    };
    
	ActionListener OK = new ActionListener() {
        public void actionPerformed(ActionEvent OK) {
		if(Executar){
        	fechaJanela();
        	System.out.println("Erro:"+BD.insertArquivos(idUsuario, arquivosRecentes));
        	Excel.abreExcel(caminhoArquivo);
        	JanelaExecucao = new ControleExecucao();
        	JanelaExecucao.abreJanela();}
        else if(Cadastrar){
        	fechaJanela();
        	System.out.println("Erro:"+BD.insertArquivos(idUsuario, arquivosRecentes));
        	Excel.abreExcel(caminhoArquivo);
        	JanelaCadRegra = new ControleCadastroRegra();
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
    
    ActionListener Abre = new ActionListener() {
        public void actionPerformed(ActionEvent Abre) {
        		int retorno = Janela.AbreArquivo.showOpenDialog(null);
        		if (retorno == JFileChooser.APPROVE_OPTION){
        			Arquivo a = new Arquivo();
        			a.setNome(Janela.AbreArquivo.getSelectedFile().getName());
        			a.setCaminho(Janela.AbreArquivo.getSelectedFile().getAbsolutePath());
        			
        			adicionaArquivoRecente(a);
        			System.out.println("Erro:"+BD.insertArquivos(idUsuario, arquivosRecentes));
        			}
        		else if(retorno == JFileChooser.CANCEL_OPTION)
        			System.out.println("Usuário cancelou a operação");
        		else
        			System.out.println("Erro ao abrir o arquivo");
        }
	};
}
