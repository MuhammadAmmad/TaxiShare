package Testes;

import TS.FrameWork.TO.*;
import TS.FrameWork.DAO.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Bruno
 */
public class Teste extends javax.swing.JDialog {

    /**
     * Creates new form Teste
     */
    public Teste(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        entityManager2 = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("HibernateJPAPU").createEntityManager();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nometxt = new javax.swing.JTextField();
        sexotxt = new javax.swing.JTextField();
        dddtxt = new javax.swing.JTextField();
        emailtxt = new javax.swing.JTextField();
        celulartxt = new javax.swing.JTextField();
        enviarTeste = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Nome");

        jLabel2.setText("Sexo");

        jLabel3.setText("Email");

        jLabel4.setText("dddTelefone");

        jLabel5.setText("celular");

        nometxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nometxtActionPerformed(evt);
            }
        });

        enviarTeste.setText("Enviar");
        enviarTeste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarTesteActionPerformed(evt);
            }
        });

        jLabel6.setText("Cadastrar Pessoa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(enviarTeste)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(celulartxt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dddtxt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(nometxt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(emailtxt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(sexotxt)))
                        .addGap(1, 1, 1)))
                .addGap(154, 154, 154))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nometxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sexotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(emailtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(dddtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(celulartxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(enviarTeste)
                .addGap(41, 41, 41))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nometxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nometxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nometxtActionPerformed

    private void enviarTesteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarTesteActionPerformed
//
//        Endereco e1 = new Endereco("rua FW","Sao miguel2", 444,"são paulo", "sao paulo", "sp", "brasil", "049876542", 'O');
//        Endereco e2 = new Endereco("rua FW","itaquera2", 444,"são paulo", "sao paulo", "sp", "brasil", "098763838", 'D');
//        Endereco e3 = new Endereco("rua FW","patriarca2", 444,"são paulo", "sao paulo", "sp", "brasil", "098763566", 'D');
//        
//        List<Endereco> lstEnd  = new ArrayList();
//        lstEnd.add(e1);
//        lstEnd.add(e2);
//        lstEnd.add(e3);
//        
//        Rota rota = new Rota(new Date(), true, Short.parseShort("1"), lstEnd);
        RotaJpaController rotaDAO = new RotaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
        Rota rotaw = rotaDAO.findRota(1);
//        
        
//        PerguntaJpaController perguntaDAO = new PerguntaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
//        PessoaJpaController pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
//        UsuarioJpaController usuarioDAO = new UsuarioJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
//        
                
//        Pessoa pessoa = new Pessoa(new Date(), "alan", "11", "97873829", "feminino", "alan@alan.com");
//        Pergunta pergunta = perguntaDAO.findPergunta(1) ;
//        Usuario u1 = new Usuario("aln2", "@lan", "seila", pessoa, pergunta, null);
//        
//        pessoaDAO.create(pessoa);
//        usuarioDAO.create(u1);
//        rota.getUsuarios().add(u1);
                
//        rotaDAO.create(rota);
        
        System.out.println("FOI");

    }//GEN-LAST:event_enviarTesteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
              try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Teste.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Teste.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Teste.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Teste.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
            java.util.logging.Logger.getLogger(Teste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Teste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Teste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Teste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Teste dialog = new Teste(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField celulartxt;
    private javax.swing.JTextField dddtxt;
    private javax.swing.JTextField emailtxt;
    private javax.persistence.EntityManager entityManager2;
    private javax.swing.JButton enviarTeste;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField nometxt;
    private javax.swing.JTextField sexotxt;
    // End of variables declaration//GEN-END:variables
}
