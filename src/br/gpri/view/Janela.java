package br.gpri.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author sikeira
 */
public class Janela extends javax.swing.JFrame {

    /** Creates new form Janela */
    public Janela() {
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        TextoSumario = new javax.swing.JTextArea();
        TextoGeraRegra = new javax.swing.JTextField();
        TextoRegra = new javax.swing.JTextField();
        BotaoAnterior = new javax.swing.JButton();
        BotaoProximo = new javax.swing.JButton();
        BotaoGerarRegra = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TextoCaminhoArquivo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        BotaoAbrir = new javax.swing.JButton();
        DropDownListBox1 = new javax.swing.JComboBox();
        DropDownListBox2 = new javax.swing.JComboBox();
        AbreArquivo = new javax.swing.JFileChooser();
        
               
        AbreArquivo.setFileFilter(new FileNameExtensionFilter("Arquivo do Sumário/Laudos do Excel","xls"));
        AbreArquivo.setAcceptAllFileFilterUsed(false);
        
        //Quebra de Linha Caixa de texto Sumário
        TextoSumario.setLineWrap(true);
        TextoSumario.setWrapStyleWord(true);  
        
        //Scroll da Caixa de Texto do Sumário
        
        
        
        //Habilitando ActionListener Botões      
		BotaoAbrir.addActionListener(this.Abre);
        BotaoProximo.addActionListener(this.Proximo);
		BotaoAnterior.addActionListener(this.Anterior);
		BotaoGerarRegra.addActionListener(this.GeraRegra);
		//BotaoGerarRegra.addActionListener(this.GeraRegra);
		
		//Desabilitando edição
		TextoSumario.setEditable(false);
		TextoGeraRegra.setEditable(false);
		TextoRegra.setEditable(false);
		
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        BotaoAnterior.setText("Anterior");

        BotaoProximo.setText("Próximo");

        BotaoGerarRegra.setText("Gerar Regra");

        jLabel1.setText("Sumário");

        jLabel2.setText("Texto");

        jLabel3.setText("Regra");

        jLabel4.setText("Caminho para o Arquivo");

        BotaoAbrir.setText("Abrir");

        DropDownListBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        DropDownListBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel4)
                            .add(layout.createSequentialGroup()
                                .add(TextoCaminhoArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 470, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(BotaoAbrir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 220, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(TextoSumario)
                            .add(TextoGeraRegra)
                            .add(TextoRegra))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(DropDownListBox1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, BotaoProximo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, BotaoAnterior, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(BotaoGerarRegra, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .add(DropDownListBox2, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4)
                .add(2, 2, 2)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TextoCaminhoArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(BotaoAbrir))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(BotaoAnterior)
                        .add(18, 18, 18)
                        .add(BotaoProximo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 162, Short.MAX_VALUE)
                        .add(BotaoGerarRegra))
                    .add(TextoSumario))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(TextoGeraRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(DropDownListBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(DropDownListBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(TextoRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public void abrejanela() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Janela().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton BotaoAbrir;
    private javax.swing.JButton BotaoAnterior;
    private javax.swing.JButton BotaoGerarRegra;
    private javax.swing.JButton BotaoProximo;
    private javax.swing.JComboBox DropDownListBox1;
    private javax.swing.JComboBox DropDownListBox2;
    private javax.swing.JTextField TextoCaminhoArquivo;
    private javax.swing.JTextField TextoGeraRegra;
    private javax.swing.JTextField TextoRegra;
    private javax.swing.JTextArea TextoSumario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JFileChooser AbreArquivo;
    // End of variables declaration
    
    //Funcionalidade BotÃ£o Abrir
    ActionListener Abre = new ActionListener() {
        public void actionPerformed(ActionEvent Abre) {
        		int retorno = AbreArquivo.showOpenDialog(null);
        		if (retorno == JFileChooser.APPROVE_OPTION){
        			Interface.caminho = AbreArquivo.getSelectedFile().getAbsolutePath();
        			TextoCaminhoArquivo.setText(Interface.caminho);
        			System.out.println(Interface.caminho);
        			Interface.Excel();
        			TextoSumario.setText(Interface.celula.getContents());}
        		else if(retorno == JFileChooser.CANCEL_OPTION)
        			System.out.println("Usuário cancelou a operação");
        		else
        			System.out.println("Erro ao abrir o arquivo");
        }
	};
	
	//Funcionalidade BotÃ£o Proximo/Anterior
	ActionListener Proximo = new ActionListener() {
	    public void actionPerformed(ActionEvent Proximo) {
	        if(Interface.linha != Interface.Planilha.getRows()){
	    	  Interface.linha++;
	    	  System.out.println(Interface.linha);
	    	  Interface.celula = Interface.Planilha.getCell(0, Interface.linha);
	    	  TextoSumario.setText(Interface.celula.getContents());
	    	  
	    	  //Limpa Caixa de Texto Regras
	    	  TextoGeraRegra.setText(null);
	    	  TextoRegra.setText(null);
	      }
	    }
	};
	ActionListener Anterior = new ActionListener() {
        public void actionPerformed(ActionEvent Anterior) {
        	if(Interface.linha != 0){
  	    	  Interface.linha--;
  	    	  System.out.println(Interface.linha);
  	    	  Interface.celula = Interface.Planilha.getCell(0, Interface.linha);
  	    	  TextoSumario.setText(Interface.celula.getContents());
  	    	  
  	    	  //Limpa Caixa de Texto Regras
	    	  TextoGeraRegra.setText(null);
	    	  TextoRegra.setText(null);
  	      }       
        }
	};
	
	//Funcionalidade Botão Selecionar Texto
	ActionListener GeraRegra = new ActionListener() {
        public void actionPerformed(ActionEvent GeraRegra) {
        TextoGeraRegra.setText(TextoSumario.getSelectedText());
        TextoRegra.setText(Interface.Tagger.TaggerInterface(TextoSumario.getText(), TextoGeraRegra.getText(),false));
    	
    	//Inserts
    	Interface.windowinsert.ExecutaJanela();
    	Interface.Inserts();
        }
	};

	
	//ActionPerformed Genérico (Não faz nada)
	public void actionPerformed(ActionEvent e) {
	}
}