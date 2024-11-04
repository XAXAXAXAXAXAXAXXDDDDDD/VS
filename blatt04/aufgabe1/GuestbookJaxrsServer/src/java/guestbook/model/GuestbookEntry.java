/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guestbook.model;

import java.util.Date;

/**
 *
 * @author dieter
 */
public class GuestbookEntry {

  static final private String CRLF = System.getProperty("line.separator");

  private int id;
  private String author;
  private String msg;
  private Date date;

  public int getId() {
    return id;
  }

  public void setId(int val) {
    id = val;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String val) {
    author = val;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String val) {
    msg = val;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date val) {
    date = val;
  }

  public String toXml() {
    String res = "<entry>";
    res = res + "</entry";
    return res;
  }

  public String XMLEncodeEntry() {
    String res;
    res = "<guestbookentry>";
    res = res + "<id>";
    res = res + getId();
    res = res + "</id>";
    res = res + "<author>";
    res = res + getAuthor();
    res = res + "</author>";
    res = res + "<msg>";
    res = res + getMsg();
    res = res + "</msg>";
    res = res + "<date>";
    res = res + getDate();
    res = res + "</date>";
    res = res + "</guestbookentry>";
    return res;
  }

  public String plainEncodeEntry() {
    String res;
    res = "ID:" + getId();
    res = res + " Author:";
    res = res + getAuthor();
    res = res + " Text:";
    res = res + getMsg();
    res = res + " Date:";
    res = res + getDate();
    res = res + CRLF;
    return res;
  }

} // class
