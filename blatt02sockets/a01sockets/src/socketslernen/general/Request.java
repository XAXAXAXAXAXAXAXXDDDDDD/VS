/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketslernen.general;

/**
 *
 * @author kai
 */
public class Request {

    private short methodId;
    private int textlen;
    private String text;
    private int simTime;
    
    public short getMethodId() {
        return this.methodId;
    }

    public void setMethodId(short v) {
        this.methodId = v;
    }

    public int getTextlen() {
        return this.textlen;
    }

    public void setTextlen(int v) {
        this.textlen = v;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String v) {
        this.text = v;
    }
    
    public int getSimTime() {
        return this.simTime;
    }

    public void setSimTime(int s) {
        this.simTime = s;
    }
}
