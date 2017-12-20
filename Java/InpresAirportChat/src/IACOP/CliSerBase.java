/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import com.sun.media.jfxmedia.AudioClip;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class CliSerBase {
    
    public MulticastSocket serSockUDP = null;
    public Socket cliSocketTCP = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public int portTCP = 50000;
    public int portUDP = 50001;
    public boolean run=false;
    InetAddress addrUdp;

    public CliSerBase() {
        try {
            this.addrUdp = InetAddress.getByName("227.0.0.10");
        } catch (UnknownHostException ex) {
            Logger.getLogger(CliSerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
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
                //System.out.println("Debut read TCP");
                try {
                taille = dis.readInt();
                //System.out.println(""+taille);
                System.out.println("1--"+taille);
                type = dis.readInt();
                //System.out.println(""+type);
                System.out.println("2--"+type);
                buf = new byte[taille];
                dis.readFully(buf);
                System.out.println("3--"+new String(buf));
                if(buf.length != 0)
                   block = false;
                }
                catch(EOFException e)
                {}
                catch(NullPointerException e)
                {}
            }
            System.out.println("fin read tcp");
            return ""+type+"#"+new String(buf);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public String readTcpUnix()
    {
        byte[] buf = null;
        boolean block = true;
        int taille;
        int type=0;
        try {
            while(block == true)
            {
                //System.out.println("Debut read TCP");
                try {
                taille = dis.readByte();
                //System.out.println(""+taille);
                System.out.println("1--"+taille);
                type = dis.readByte();
                //System.out.println(""+type);
                System.out.println("2--"+type);
                buf = new byte[taille];
                dis.readFully(buf);
                System.out.println("3--"+new String(buf));
                if(buf.length != 0)
                   block = false;
                }
                catch(EOFException e)
                {}
                catch(NullPointerException e)
                {}
            }
            System.out.println("fin read tcp");
            return ""+type+"#"+new String(buf);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int writeUnix(IACOPmsg s)
    {
        byte[] buf = s.toByte();
        DatagramPacket paquet=null;
        try {
            dos.writeByte(s.msg.getBytes().length);
            //System.out.println(""+s.msg.getBytes().length);
            dos.writeByte(s.code);
            //System.out.println(""+s.code);
            dos.write(s.msg.getBytes());
            //System.out.println(""+new String(s.msg.getBytes()));
            dos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
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
            dos.writeInt(s.msg.getBytes().length);
            //System.out.println(""+s.msg.getBytes().length);
            dos.writeInt(s.code);
            //System.out.println(""+s.code);
            dos.write(s.msg.getBytes());
            //System.out.println(""+new String(s.msg.getBytes()));
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
                dos.writeInt(s.code);
                dos.write(s.msg.toString().getBytes());
                dos.flush();
            }
            else
            {
                String msg = new String(s.toByte());
                //paquet = new DatagramPacket(s.toByte(),s.toByte().length,addr, port);
                paquet = new DatagramPacket(msg.getBytes(),msg.getBytes().length,addrUdp, port);
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
            if(dis != null)
                dis.close();
            if(dos != null)
                dos.close();
            if(cliSocketTCP != null)
                cliSocketTCP.close();
            
            serSockUDP.leaveGroup(InetAddress.getByName("227.0.0.10"));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
}
