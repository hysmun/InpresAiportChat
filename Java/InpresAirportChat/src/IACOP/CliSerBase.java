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
public class CliSerBase {
    
    public DatagramSocket serSockUDP = null;
    public Socket cliSocketTCP = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public int portTCP = 50000;
    public int portUDP = 50001;
    public boolean run=false;
    
    
    public int writeLogin(String s)
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
    
    public String readTcp()
    {
        byte[] buf = null;
        boolean block = true;
        int taille;
        int type=0;
        try {
            while(block == true)
            {
                try {
                taille = dis.readInt();
                type = dis.readInt();
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
            return ""+type+"|"+new String(buf);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public DatagramPacket readUdp()
    {
        boolean block = true;
        byte[] buf = new byte[256];
        DatagramPacket p=new DatagramPacket(buf, 256);
        int taille;
        try {
            serSockUDP.receive(p);
            return p;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int write(IACOPmsg s)
    {
        byte[] buf = s.toByte();
        DatagramPacket paquet=null;
        try {
            dos.writeInt(s.msg.toString().getBytes().length);
            dos.writeInt(s.code);
            dos.write(s.msg.toString().getBytes());
            dos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    
    public int write(IACOPmsg s, InetAddress addr, int port)
    {
        byte[] buf = s.toByte();
        DatagramPacket paquet=null;
        try {
            if(s.code == IACOP.LOGIN_GROUP)
            {
                dos.writeInt(s.msg.toString().getBytes().length);
                dos.write(s.code);
                dos.write(s.msg.toString().getBytes());
                dos.flush();
            }
            else
            {
                paquet = new DatagramPacket(s.toByte(),s.toByte().length,addr, port);
                serSockUDP.send(paquet);
            }
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
}