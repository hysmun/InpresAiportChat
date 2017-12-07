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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class Server extends CliSerBase{
    public ServerSocket serSockTCP = null;
    
    public Server( int port)
    {
        try {
            portTCP=port;
            serSockTCP = new ServerSocket(portTCP);
            serSockUDP = new DatagramSocket(portUDP);
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Server( int portTcp, int portUdp)
    {
        try {
            portTCP=portTcp;
            portUDP = portUdp;
            serSockTCP = new ServerSocket(portTCP);
            serSockUDP = new DatagramSocket(portUDP);
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void accept()
    {
        try {
            cliSocketTCP = serSockTCP.accept();
            dis = new DataInputStream(cliSocketTCP.getInputStream());
            dos = new DataOutputStream(cliSocketTCP.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int Sclose()
    {
        try {
            close();
            serSockTCP.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
}
