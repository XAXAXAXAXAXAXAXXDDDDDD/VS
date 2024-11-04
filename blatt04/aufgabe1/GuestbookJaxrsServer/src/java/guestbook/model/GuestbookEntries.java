/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guestbook.model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dieter
 */
public class GuestbookEntries {

  public static final String DATA_STORE = "guestbookdatastore.txt";
  private static final  String CRLF = System.getProperty("line.separator");
  private String path_data_store = null;
  private List<GuestbookEntry> list = new ArrayList<GuestbookEntry>();
  private int idcounter = 0;

  public GuestbookEntries(String path) {
    path_data_store = path;
    char c = path_data_store.charAt(path_data_store.length() - 1);
    if (c != '/') {
      path_data_store += "/";
    }
  }

  public GuestbookEntries() { // Construktor without parameters. Needed for XMLEncoder.

  }

  public int getIdcounter() {
    return idcounter;
  }

  public void setIdcounter(int val) {
    idcounter = val;
  }

  public void addEntry(GuestbookEntry entry) {
    list.add(entry);
  }

  public List<GuestbookEntry> getEntryList() {
    return list;
  }

  public void setEntryList(List<GuestbookEntry> val) {
    list = val;
  }

  public void clearEntryList() {
    list.clear();
  }

  public void deleteEntry(GuestbookEntry obj) {
    list.remove(obj);
  }

  /* ------------------------------------------------------------------------ */
 /* 
   Throws an exception if loading from file DATA_STORE fails
   */
  private void loadFromFile() throws IOException {
    try {
      FileInputStream os = new FileInputStream(path_data_store + DATA_STORE);
      XMLDecoder decoder = new XMLDecoder(os);
      GuestbookEntries tmp = (GuestbookEntries) decoder.readObject();
      decoder.close();
      list = tmp.getEntryList();
      idcounter = tmp.getIdcounter();

    } catch (IOException e) {
      // "Cannot find or open File for reading: " + path_data_store + DATA_STORE;
      throw e;
    }
  }

  /* 
   Throws an exception if saving to file DATA_STORE fails
   */
  private void saveToFile() throws IOException {
    try {
//      FileInputStream os = new FileInputStream(DATA_STORE); // Bad, since file would be in the bin direcotry of the Tomcat Server.
      FileOutputStream os = new FileOutputStream(path_data_store + DATA_STORE);
      XMLEncoder encoder = new XMLEncoder(os);
      encoder.writeObject(this);
      encoder.close();
    } catch (IOException e) {
      // "Cannot find or open File for writing: " + path_data_store + DATA_STORE;
      throw e;
    }
  }

  /* 
  Assumes guestbook was loaded previously
   */
  private GuestbookEntry getEntryById(int id) {
    GuestbookEntry res = null;
    // search for entry with id
    int size = list.size();
    for (int i = 0; i < size; i++) {
      res = list.get(i);
      if (res.getId() == id) {
        // found
        break;
      }
      res = null;  // entry not found
    } // for
    return res;
  }

  /* ------------------------------------------------------------------------ */
 /* 
  Returns a list of the entries of an author which may be an empty list
  Depending on producesTyps the list is in xml representation or plain text
  Throws an exception if loading from file fails
   */
  public String searchEntries(String author, String producesType) throws IOException {
    String res = "Requested MIME-Type " + producesType + " not supported";
    List<GuestbookEntry> resultlist = new ArrayList<GuestbookEntry>();

    GuestbookEntry entry;
    loadFromFile();

    if (author == null) {
      author = "";
    }
    // search for all entries of the author in book
    int size = list.size();
    for (int i = 0; i < size; i++) {
      entry = list.get(i);
      if (entry.getAuthor().equals(author)) {
        resultlist.add(entry);
      }
    } // for

    if (producesType.equals("text/xml")) {
      // build the xml-string to be returned
      res = "<?xml version=\"1.0\" ?>";
      res = res + "<guestbooklist>";
      size = resultlist.size();
      if (size > 0) {
        for (int i = 0; i < size; i++) {
          entry = resultlist.get(i);
          res = res + entry.XMLEncodeEntry();
        } // for

        res = res + "</guestbooklist>";

      } else {
        // construct an empty list
        res = "<?xml version=\"1.0\" ?>";
        res = res + "<guestbooklist>";
        res = res + "</guestbooklist>";
      }
    }
    if (producesType.equals("text/plain")) {
      // build the xml-string to be returned
      res = "";
      size = resultlist.size();
      if (size > 0) {
        for (int i = 0; i < size; i++) {
          entry = resultlist.get(i);
          res = res + entry.plainEncodeEntry();
        } // for
      } else {
        // construct an empty list
        res = "No entries found";
      }
    }
    return res;
  }

  /* 
  Returns a list of the entries as xml representation which may be an empty list
  Throws an exception if loading from file fails
   */
  public String getEntriesAsXml() throws IOException {
    String res;

    loadFromFile();
    // build the xml-string to be returned
    res = "<?xml version=\"1.0\" ?>";
    res = res + "<guestbooklist>";
    List<GuestbookEntry> list = getEntryList();
    int size = list.size();
    for (int i = 0; i < size; i++) {
      GuestbookEntry entry = list.get(i);
      res = res + entry.XMLEncodeEntry();
    } // for
    res = res + "</guestbooklist>";
    return res;
  }

  public String getEntriesAsTextPlain() throws IOException {
    String res;

    loadFromFile();
    // build the xml-string to be returned
    res = "";
    res = res + "guestbooklist:" + CRLF;
    List<GuestbookEntry> list = getEntryList();
    int size = list.size();
    for (int i = 0; i < size; i++) {
      GuestbookEntry entry = list.get(i);
      res = res + entry.plainEncodeEntry();
    } // for
    res = res + "end of guestbooklist.";
    return res;
  }

  /* 
  Returns plain text representation of the entry if it exists
  Returns null if entry with id is not found
  Throws an exception if loading from file fails
   */
  public String getEntryPlain(int id) throws IOException {
    String res = null;
    GuestbookEntry entry;

    loadFromFile();
    // search for id
    entry = getEntryById(id);
    // build the plain string to be returned
    if (entry != null) {
      res = res + entry.plainEncodeEntry();
    }
    return res;
  }

  /* 
  Returns xml representation of the entry if it exists
  Returns null if entry with id is not found
  Throws an exception if loading from file fails
   */
  public String getEntryXML(int id) throws IOException {
    String res = null;

    GuestbookEntry entry;
    loadFromFile();
    // search for id
    entry = getEntryById(id);
    // build the xml-string to be returned
    if (entry != null) {
      // build first part of xml documentto be returned
      res = "<?xml version=\"1.0\" ?>";
      res = res + entry.XMLEncodeEntry();
      // build last part of xml document to be returned
    }
    return res;
  }

  /* 
  Returns xml representation of the entry to be modified if it exists
  Returns null if no entry with idval is found
  Throws an exception if loading from file or saving to file fails
   */
  public String putEntry(int idval, String author, String msg) throws IOException {
    String res = null;
    GuestbookEntry entry;
    loadFromFile();
    // search for id
    entry = getEntryById(idval);
    // build the xml-string to be returned
    if (entry != null) {
      res = "<?xml version=\"1.0\" ?>";
      entry.setAuthor(author);
      entry.setDate(new Date());
      entry.setMsg(msg);
      saveToFile();
      res = res + entry.XMLEncodeEntry();
    }
    return res;
  }

  /* 
  Returns xml representation of the entry to be deleted if it exists
  Returns null if no entry with idval is found
  Throws an exception if loading from file or saving to file fails
   */
  public String deleteEntry(int idval) throws IOException {
    String res;
    GuestbookEntry entry;

    loadFromFile();
    // search for id
    entry = getEntryById(idval);
    // build the xml-string to be returned
    if (entry != null) {
      res = "<?xml version=\"1.0\" ?>";
      res = res + entry.XMLEncodeEntry();
      /* Eintrag wirklich loeschen */
      deleteEntry(entry);
      /* neuen Stand speichern */
      saveToFile();
      return res;
    } else {
      return null;
    }
  }

  /* 
  Returns xml representation of new entry
  Throws an exception if saving to file fails
   */
  public GuestbookEntry create(String author, String msg) throws IOException {
    String res;

    if (msg == null || msg.equals("")) {
      msg = "Message undefinied";
    }
    if (author == null || author.equals("")) {
      author = "anonymous";
    }

    GuestbookEntry entry = new GuestbookEntry();
    // if loading failed, go on with empty content of object book
    // else object book contains the values previously saved, also go on  
    try {
      loadFromFile();
    } catch (IOException ioe) {
      // go on
    }

    int idcounter = getIdcounter();
    idcounter++;
    setIdcounter(idcounter);

    entry.setAuthor(author);
    entry.setId(idcounter);
    entry.setDate(new Date());
    entry.setMsg(msg);

    addEntry(entry);
    saveToFile();
    // construct result xml document
    res = "<?xml version=\"1.0\" ?>";
    res = res + entry.XMLEncodeEntry();
    return entry;
  }

} // class
