/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IACOP;

import java.util.StringTokenizer;

/**
 *
 * @author 'Toine
 */
public class IACOPmsg {
    
    
    public int code;
    public String msg;

    public IACOPmsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public IACOPmsg(String msg) {
        StringTokenizer st = new StringTokenizer(msg, "|");
        
        this.code = Integer.parseInt(st.nextToken());
        this.msg = st.nextToken();
    }
    
    public IACOPmsg(byte [] msg) {
        String tmp = new String(msg);
        StringTokenizer st = new StringTokenizer(tmp, "|");
        
        this.code = Integer.parseInt(st.nextToken());
        this.msg = st.nextToken();
    }

    @Override
    public String toString() {
        return "" + code + "|" + msg;
    }
    
    public byte[] toByte()
    {
        return toString().getBytes();
    }
}
