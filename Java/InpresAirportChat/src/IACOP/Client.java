/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class Client extends CliSerBase{
    public String ipTCP;
    public int portTCP;
    public int portUDP;
    
    
    public Client(String ip, int port, int portUdp)
    {
        try {
            ipTCP = ip;
            portTCP=port;
            portUDP = portUdp;
            cliSocketTCP = new Socket(ipTCP, portTCP);
            //serSockUDP = new DatagramSocket(portUDP);
            dis = new DataInputStream(cliSocketTCP.getInputStream());
            dos = new DataOutputStream(cliSocketTCP.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connectUdp( int portUdp)
    {
        try {
            portUDP = portUdp;
            serSockUDP = new DatagramSocket(portUDP);
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
