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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class Client{
    public Socket cliSockTCP = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public String ipTCP;
    public int portTCP;
    
    
    public Client(String ip, int port)
    {
        try {
            ipTCP = ip;
            portTCP=port;
            cliSockTCP = new Socket(ipTCP, portTCP);
            dis = new DataInputStream(cliSockTCP.getInputStream());
            dos = new DataOutputStream(cliSockTCP.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
        byte[] buf=null;
        int taille;
        boolean block = true;
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
                catch(EOFException | NullPointerException e)
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
            cliSockTCP.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
   
}
