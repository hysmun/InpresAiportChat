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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class SerIAC extends javax.swing.JFrame {

    Server ser;
    int PORTTCP = 50000;
    Thread thTcp;
    Thread thUdp;
    boolean run = false;
    
    static Map<String, String> hashLogin = new TreeMap<String, String>();
    static Map<String, String> hashQuestion = new TreeMap<String, String>();
    
    static
    {
        hashLogin.put("user", "user");
        hashLogin.put("root", "toor");
        hashLogin.put("toine", "aaaa");
        hashLogin.put("remy", "ggbrogg");
        
        hashQuestion.put("weather", "20 degre, pluie, tornade");
        hashQuestion.put("job", "Netoyeur centrale nucléaire de fukushima, peripateticienne, Souffleur de reponse, magicien pour aveugle");
        hashQuestion.put("bourse", "dollars 0,81   euros 1   yen 10000   chilling 423");
    }
    /**
     * Creates new form SerIAC
     */
    public SerIAC() {
        initComponents();
        ser = new Server(PORTTCP);
        write("serveur lancer");
        thTcp = new Thread(){public void run(){ readWhileTcp(); }};
        thUdp = new Thread(){public void run(){ readWhileUdp(); }};
        
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
        startButton = new javax.swing.JButton();
        StopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatTA.setEditable(false);
        chatTA.setColumns(20);
        chatTA.setRows(5);
        jScrollPane1.setViewportView(chatTA);

        startButton.setText("Start");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });

        StopButton.setText("Stop");
        StopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StopButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addGap(61, 61, 61)
                        .addComponent(StopButton)
                        .addGap(0, 656, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(StopButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        // TODO add your handling code here:
        run = true;
        thTcp.start();
        thUdp.start();
    }//GEN-LAST:event_startButtonMouseClicked

    private void StopButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StopButtonMouseClicked
        // TODO add your handling code here:
        run = false;
        ser.Sclose();
    }//GEN-LAST:event_StopButtonMouseClicked

    
    public void write(String tmp)
    {
        Date d = new Date();
        String s = ""+d+" "+tmp+"\n";
        chatTA.append(s);
        System.out.print(s);
    }
    public void readWhileTcp()
    {
        //to do here
        IACOPmsg msg = null;
        write("DEBUT read TCP");
        while(run == true)
        {
            write("serveur accept");
            ser.accept();
            write("accept OK");
            
            String tmp;
            tmp = ser.readTcp();
            write("message lu");
            msg = new IACOPmsg(tmp);
            if(msg.code == IACOP.LOGIN_GROUP){
                try {
                    StringTokenizer st = new StringTokenizer(msg.msg, "|");
                    String login = st.nextToken();

                    long temps = Long.parseLong(st.nextToken());
                    double alea = Double.parseDouble(st.nextToken());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream bdos = new DataOutputStream(baos);
                    bdos.writeLong(temps);
                    bdos.writeDouble(alea);
                    String mdp = (String) hashLogin.get(login);
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(mdp.getBytes());
                    md.update(baos.toByteArray());
                    String digest = new String(md.digest());
                
                    if (digest.equals(st.nextToken())) {
                        write(msg.toShow());
                        msg = new IACOPmsg(IACOP.LOGIN_GROUP, "127.0.0.1|50001");
                        ser.write(msg);
                    }
                    else
                    {
                        write(msg.toShow());
                        msg = new IACOPmsg(IACOP.LOGIN_NOK, "NON");
                        ser.write(msg);
                    }
                } catch (NullPointerException e) {
                    msg = new IACOPmsg(IACOP.LOGIN_NOK, "NON");
                    ser.write(msg);
                } catch (IOException ex) {
                    Logger.getLogger(SerIAC.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SerIAC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                write(msg.toShow());
                msg = new IACOPmsg(IACOP.LOGIN_NOK, "NON");
                ser.write(msg);
            }
        }
        write("Fin read TCP");
    }
    public void readWhileUdp()
    {
        byte[] buf = new byte[256];
        DatagramPacket msg = new DatagramPacket(buf, 256);
        IACOPmsg chatmsg=null;
        write("DEBUT read UDP");
        while(run == true)
        {
            String tmp;
            msg = ser.readUdp();
            //write("Recu : "+new String(msg.getData()));
            chatmsg = new IACOPmsg(new String(msg.getData()).substring(0, msg.getLength()));
            if(chatmsg.code == IACOP.POST_QUESTION)
            {
                write("POST_QUESTION:"+chatmsg.toShow());
                IACOPmsg chatrep = null;
                String rep;
                rep = hashQuestion.get(chatmsg.msg);
                //write("@"+chatmsg.msg+" -- ");
                if(rep != null)
                    chatrep = new IACOPmsg(IACOP.ANSWER_QUESTION, rep);
                else
                    chatrep = new IACOPmsg(IACOP.ANSWER_QUESTION, "ERREUR QUESTION");
                ser.write(chatrep, msg.getAddress(), msg.getPort());
            }
            if(chatmsg.code == IACOP.POST_EVENT)
            {
                write(chatmsg.toShow());
            }
        }
        write("Fin read UDP");
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
            java.util.logging.Logger.getLogger(SerIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SerIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SerIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SerIAC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SerIAC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton StopButton;
    private javax.swing.JTextArea chatTA;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
