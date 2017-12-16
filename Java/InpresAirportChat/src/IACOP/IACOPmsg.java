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
        setMsg(msg);
    }
    
    public IACOPmsg(String msg) {
        StringTokenizer st = new StringTokenizer(msg, "#");
        
        this.code = Integer.parseInt(st.nextToken());
        setMsg(st.nextToken());
        //System.out.println("Creat iacop msg |"+msg+"|");
    }
    
    public IACOPmsg(byte [] msg) {
        String tmp = new String(msg);
        StringTokenizer st = new StringTokenizer(tmp, "#");
        
        this.code = Integer.parseInt(st.nextToken());
        setMsg(st.nextToken());
    }
    
    public boolean setMsg(String msg)
    {
        this.msg = msg;
        return false;
    }

    @Override
    public String toString() {
        return "" + code + "#" + msg;
    }
    
    public String toShow()
    {
        String tmp = "";
        if(code == IACOP.LOGIN_GROUP)
            tmp+="LOGIN + "+msg;
        if(code == IACOP.ANSWER_QUESTION)
            tmp+="Answer + "+msg;
        if(code == IACOP.POST_QUESTION)
            tmp+="Question + "+msg;
        if(code == IACOP.POST_EVENT)
            tmp+=""+msg;
        return tmp;
    }
    
    public byte[] toByte()
    {
        return toString().getBytes();
    }
}
