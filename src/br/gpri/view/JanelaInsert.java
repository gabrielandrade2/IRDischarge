package br.gpri.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sikeira
 */
public class JanelaInsert extends javax.swing.JFrame implements ActionListener {

    //Cria nova janela
    public JanelaInsert() {
        initComponents();
    }
    
    //Variáveis                     
    private javax.swing.JButton BotaoOK;
    private javax.swing.JTextArea TextoInserts;
    private javax.swing.JTextArea TextoRegras;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
     
    
    public void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TextoInserts = new javax.swing.JTextArea();
        BotaoOK = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TextoRegras = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

	//Parâmetros Caixa de Texto
        TextoInserts.setColumns(20);
        TextoInserts.setRows(5);
        TextoInserts.setEditable(false);
        jScrollPane1.setViewportView(TextoInserts);
	//Quebra de Linha Caixa de texto Sumário
        TextoInserts.setLineWrap(true);
        TextoInserts.setWrapStyleWord(true);  

        BotaoOK.setText("OK");

        TextoRegras.setColumns(20);
        TextoRegras.setRows(5);
        TextoRegras.setEditable(false);
        TextoRegras.setLineWrap(true);
        TextoRegras.setWrapStyleWord(true);
        jScrollPane2.setViewportView(TextoRegras);

	//Insere texto na caixa ao criar a Janela
        String termosregras = "";
    	for (int i = 0; i < Interface.termosregras.length; i++)
    		if(Interface.termosregras[i] != null)
    		termosregras += (Interface.termosregras[i] + "\n\n\n");
    	this.TextoInserts.setText(termosregras);

    	this.TextoRegras.setText(Interface.regras);
    	
    	BotaoOK.addActionListener(OK);
    	
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addComponent(BotaoOK, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BotaoOK, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }                      

    
    public static void ExecutaJanela() {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JanelaInsert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JanelaInsert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JanelaInsert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JanelaInsert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JanelaInsert().setVisible(true);
            }
        });
    }
                  
   
    ActionListener OK = new ActionListener() {
        public void actionPerformed(ActionEvent OK) {
        	setVisible(false);
        	dispose();
        	
        	}
        };
    
	//Não faz nada
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
