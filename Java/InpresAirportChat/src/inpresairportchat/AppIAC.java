/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inpresairportchat;

import IACOP.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class AppIAC extends javax.swing.JFrame {

    Client cli=null;
    int PORTTCP = 50000;
    String ipTCP = "127.0.0.1";
    int PORTUDP = 50001;
    String ipUDP = "127.0.0.1";
    Inet4Address IPUDP;
    Thread th;
    boolean run = false;
    /**
     * Creates new form AppIAC
     */
    public AppIAC() {
        initComponents();
        th = new Thread()
        {
            public void run()
            {
                //to do here
                byte[] buf = new byte[256];
                DatagramPacket msg = new DatagramPacket(buf, 256);
                IACOPmsg chatmsg=null;
                while(run == true)
                {
                    String tmp;
                    msg = cli.readUdp();
                    chatmsg = new IACOPmsg(new String(msg.getData()));
                    write(chatmsg.toShow());
                }
            }
        };
    }
    
    public AppIAC(int portTcp, int portUdp) {
        initComponents();
        PORTTCP = portTcp;
        PORTUDP = portUdp;
        portSpinner.setValue(portUdp);
        th = new Thread()
        {
            public void run()
            {
                //to do here
                byte[] buf = new byte[256];
                DatagramPacket msg = new DatagramPacket(buf, 256);
                IACOPmsg chatmsg=null;
                while(run == true)
                {
                    String tmp;
                    msg = cli.readUdp();
                    chatmsg = new IACOPmsg(new String(msg.getData()).substring(0, msg.getLength()));
                    write(chatmsg.toShow());
                }
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatTA = new javax.swing.JTextArea();
        msgTF = new javax.swing.JTextField();
        senButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        connectCB = new javax.swing.JCheckBox();
        decoButton = new javax.swing.JButton();
        loginTF = new javax.swing.JTextField();
        passTF = new javax.swing.JTextField();
        portSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatTA.setEditable(false);
        chatTA.setColumns(20);
        chatTA.setRows(5);
        jScrollPane1.setViewportView(chatTA);

        senButton.setText("Send");
        senButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                senButtonMouseClicked(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                connectButtonMouseClicked(evt);
            }
        });

        connectCB.setText("Connecté ?");

        decoButton.setText("Deco");
        decoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                decoButtonMouseClicked(evt);
            }
        });

        loginTF.setText("user");

        passTF.setText("user");

        portSpinner.setModel(new javax.swing.SpinnerNumberModel(50100, 50100, 60000, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addComponent(msgTF))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(senButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(decoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(connectCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(portSpinner, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(loginTF, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(passTF, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(connectButton)
                        .addGap(18, 18, 18)
                        .addComponent(decoButton)
                        .addGap(26, 26, 26)
                        .addComponent(connectCB)
                        .addGap(124, 124, 124)
                        .addComponent(loginTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(passTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(portSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(senButton))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void senButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_senButtonMouseClicked
        // TODO add your handling code here:
        
        IACOPmsg msg = null;
        if(msgTF.getText().charAt(0) == '?')
        {
            msg = new IACOPmsg(IACOP.POST_QUESTION,msgTF.getText().substring(1, msgTF.getText().length()));
        }
        else
            msg = new IACOPmsg(IACOP.POST_EVENT,loginTF.getText()+": "+msgTF.getText());
        cli.write(msg, IPUDP, PORTUDP);
        //write("Cli sended : "+msg.toShow());
        //cli.write(msg);
        msgTF.setText("");
    }//GEN-LAST:event_senButtonMouseClicked

    private void connectButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_connectButtonMouseClicked
        // TODO add your handling code here:1
        int portTcp1=0;
        if(cli == null)
        {
            portTcp1 = (int) portSpinner.getValue();
            PORTUDP = portTcp1+1;
            cli = new Client(ipTCP, 50000, PORTUDP);
            write("client connecter");
        }
        String log = null;
        String mdp = null;
        long temps = (new Date()).getTime();
        double alea = Math.random();
        String rep=null;
        try {
            
            log = loginTF.getText();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);
            MessageDigest md = MessageDigest.getInstance("MD5");
            mdp = passTF.getText();
            md.update(mdp.getBytes());
            md.update(baos.toByteArray());
            rep = "" + log + "|" + temps + "|" + alea + "|" + new String(md.digest());
        } catch (IOException iOException) {
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        
        IACOPmsg msg = new IACOPmsg(IACOP.LOGIN_GROUP, rep);
        cli.write(msg);
        //write("message send : "+msg.toString());
        
        String readTcp = cli.readTcp();
        msg = new IACOPmsg(readTcp);
        if(msg.code != IACOP.LOGIN_NOK)
        {
            StringTokenizer st = new StringTokenizer(msg.msg, "|");
            try {
                IPUDP = (Inet4Address) Inet4Address.getByName(st.nextToken());
            } catch (UnknownHostException ex) {
                Logger.getLogger(AppIAC.class.getName()).log(Level.SEVERE, null, ex);
            }
            PORTUDP = Integer.parseInt(st.nextToken());
            //write("Client connecter par tcp");
            write("ip recu --  "+IPUDP.getHostAddress()+":"+PORTUDP);
            connectCB.setSelected(true);
            cli.connectUdp(50001);
            run = true;
            th.start();
        }
        else
        {
            write("ERREUR LOGIN veuillez reessaye");
            connectCB.setSelected(false);
            cli.close();
            cli = null;
        }
    }//GEN-LAST:event_connectButtonMouseClicked

    private void decoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_decoButtonMouseClicked
        // TODO add your handling code here:
        run = false;
        connectCB.setSelected(false);
        cli.close();
        cli = null;
    }//GEN-LAST:event_decoButtonMouseClicked

    public void write(String tmp)
    {
        Date d = new Date();
        String s = ""+d+" "+tmp+"\n";
        chatTA.append(s);
        System.out.print(s);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(AppIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppIAC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatTA;
    private javax.swing.JButton connectButton;
    private javax.swing.JCheckBox connectCB;
    private javax.swing.JButton decoButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField loginTF;
    private javax.swing.JTextField msgTF;
    private javax.swing.JTextField passTF;
    private javax.swing.JSpinner portSpinner;
    private javax.swing.JButton senButton;
    // End of variables declaration//GEN-END:variables
}
