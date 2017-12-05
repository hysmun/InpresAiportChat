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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class Server {
    public ServerSocket serSockTCP = null;
    public Socket cliSocketTCP = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public int portTCP;
    public boolean run=false;
    
    public Server( int port)
    {
        try {
            portTCP=port;
            serSockTCP = new ServerSocket(portTCP);
            
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
    
    public int write(String s)
    {
        byte[] buf = s.getBytes();
        try {
            dos.writeInt(buf.length);
            dos.write(buf);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    
    public String read()
    {
        byte[] buf = null;
        boolean block = true;
        int taille;
        try {
            while(block == true)
            {
                try {
                taille = dis.readInt();
                buf = new byte[taille];
                dis.readFully(buf);
                if(buf.length != 0)
                   block = false;
                }
                catch(EOFException e)
                {}
                catch(NullPointerException e)
                {}
            }
            return new String(buf);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int write(IACOPmsg s)
    {
        byte[] buf = s.toByte();
        try {
            dos.writeInt(buf.length);
            dos.write(buf);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    
    public int close()
    {
        try {
            dis.close();
            dos.close();
            cliSocketTCP.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
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
