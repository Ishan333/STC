/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StringCalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author ymichot
 */
public class StcGUI extends javax.swing.JFrame {

    private String loadedSet;
    private JFileChooser loadSetFileChooser = new JFileChooser(".");
    private StringCalc stringCalc = new StringCalc();

    /**
     * Creates new form StcGUI
     */
    public StcGUI() {
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

        verboseCheckBox = new javax.swing.JCheckBox();
        metricCheckBox = new javax.swing.JCheckBox();
        calculateButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stringsTextArea = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadSetMenuItem = new javax.swing.JMenuItem();
        saveSetMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("STC - String Tension Calculator v0.1.9.1");

        verboseCheckBox.setText("Verbose");

        metricCheckBox.setText("Metric");

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        stringsTextArea.setColumns(20);
        stringsTextArea.setRows(5);
        jScrollPane1.setViewportView(stringsTextArea);

        fileMenu.setText("File");

        loadSetMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadSetMenuItem.setText("Load set...");
        loadSetMenuItem.setBorderPainted(true);
        loadSetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSetMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadSetMenuItem);

        saveSetMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveSetMenuItem.setText("Save set...");
        saveSetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSetMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveSetMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        aboutMenu.setText("About");

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        aboutMenuItem.setText("About STC...");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        aboutMenu.add(aboutMenuItem);

        jMenuBar1.add(aboutMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(verboseCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(metricCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
                .addComponent(calculateButton)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verboseCheckBox)
                    .addComponent(metricCheckBox)
                    .addComponent(calculateButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-608)/2, (screenSize.height-544)/2, 608, 544);
    }// </editor-fold>//GEN-END:initComponents

    private void loadSetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadSetMenuItemActionPerformed
        // TODO add your handling code here:
        int retval = loadSetFileChooser.showOpenDialog(StcGUI.this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File inFile = loadSetFileChooser.getSelectedFile();
            try {
                FileReader reader = new FileReader(inFile);
                BufferedReader br = new BufferedReader(reader);
                stringsTextArea.read(br, null);
                br.close();
                stringsTextArea.requestFocus();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }

    }//GEN-LAST:event_loadSetMenuItemActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        // TODO add your handling code here:
        stringsTextArea.setText(
                stringCalc.doCalculate(stringsTextArea.getText(), verboseCheckBox.isSelected(), metricCheckBox.isSelected()));
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void saveSetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSetMenuItemActionPerformed
        int retval = loadSetFileChooser.showOpenDialog(StcGUI.this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File outFile = loadSetFileChooser.getSelectedFile();
            try {
                FileWriter outFileWriter = new FileWriter(outFile);
                stringsTextArea.write(outFileWriter);
                outFileWriter.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
    }//GEN-LAST:event_saveSetMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        dispose();
        System.exit(0);        
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "STC - String Tension Calculator v0.1.9.1\nYoann \"Ishan\" Michot & Dan McMullen", "About STC...", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    


    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StcGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StcGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StcGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StcGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new StcGUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton calculateButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem loadSetMenuItem;
    private javax.swing.JCheckBox metricCheckBox;
    private javax.swing.JMenuItem saveSetMenuItem;
    private javax.swing.JTextArea stringsTextArea;
    private javax.swing.JCheckBox verboseCheckBox;
    // End of variables declaration//GEN-END:variables
}
