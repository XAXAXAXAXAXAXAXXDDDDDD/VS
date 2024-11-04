/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guestbook.view;

/**
 *
 * @author dieter
 */
public class GuestbookDescription {
  
   static final private String CRLF = System.getProperty("line.separator");
   static public String getSpecificationAsHtml(String servicename) {
    String res;
    res = "<html><head><title>"+servicename+"</title>" + CRLF;
    res = res + "<style type='text/css'>" + CRLF;
    res = res + ".code {color:blue;font-family:monospace;} " + CRLF;

    res = res + "table {border-style: solid; }" + CRLF;
    res = res + "tr {border-style: solid; border-width: 1px;}" + CRLF;
    res = res + "td {border-style: solid; border-width: 1px;}" + CRLF;

    res = res + "</style>" + CRLF;
    res = res + "</head><body>" + CRLF;
    res = res + "<h3>"+servicename+": specification of resources and operations</h3>" + CRLF;

    // Vector<String[]> v = new Vector<String[]>();
    String tableheader[]
            = {"NB", "URI-Templates", "Verb", "Request Type", "Response Type", "Description"};
    String tableelements[][] = {
//      {"/", "GET", "&nbsp;", "text/html", "Entrypoint human readable"},
//      {"/", "GET", "&nbsp;", "text/xml", "Entrypoint maschine readable"},
      {"guestbook", "GET", "&nbsp;", "application/xml", "Resource description of guestbook"},
      {"guestbook/entries", "GET", "&nbsp;", "text/plain", "Read the list of current entries"},
      {"guestbook/entries", "GET", "&nbsp;", "application/xml", "Read the list of current entries"},
      {"guestbook/entries", "POST", "application/x-www-form-urlencoded<hr />name={val}&amp;msg={val}", "application/xml", "Create a new entry"},
      {"guestbook/entries/{id}", "GET", "&nbsp;", "text/plain", "Read entry with the Id=id"},
      {"guestbook/entries/{id}", "GET", "&nbsp;", "application/xml", "Read entry with the Id=id"},
      {"guestbook/entries/{id}", "DELETE", "&nbsp;", "application/xml", "Delete entry with the Id=id"},
      {"guestbook/entries/{id}", "PUT", "application/x-www-form-urlencoded<hr />name={val}&amp;msg={val}", "application/xml", "Modify entry with the Id=id"},
      {"guestbook/entries/search<br />?name={val}", "GET", "&nbsp;", "text/plain", "Read entries with the name=val"},
      {"guestbook/entries/search<br />?name={val}", "GET", "&nbsp;", "application/xml", "Read entries with the name=val"},
      {"help", "GET", "&nbsp;", "text/html", "Human readable specification of webservice GuestbookJaxrs"}
    };

    res = res + "<table>" + CRLF;
    // construct header line
    res = res + "<tr>";
    for (int i = 0; i < tableheader.length - 1; i++) {
      res = res + "<td>" + tableheader[i] + "</td>";
    }
    res = res + "<td style='width:300px'>" + tableheader[tableheader.length - 1] + "</td>";
    // construct normal lines
    res = res + "</tr>";
    for (int i = 0; i < tableelements.length; i++) {
      res = res + "<tr>";
      res = res + "<td>" + (i+1) + "</td>";

      for (int j = 0; j < tableelements[i].length; j++) {
        res = res + "<td>" + tableelements[i][j] + "</td>";
      }
      res = res + "</tr>";
    }

    res = res + "</table>";
    
    res = res + "<p>In the default configuration of the Webservice in Netbeans 16 the resources are available in Glassfish 4.1.1/Tomcat 8 at:<br />" + CRLF;
    res = res + "<span style='font-family:monospace; color:blue'>http://&lt;domain:port&gt;/" + servicename + "/webresources/&lt;URI-Template&gt;</span></p>" + CRLF;
    res = res + "</p>";
    res = res + "<p>";
    res = res + "The human readable entry point is <span style='font-family:monospace; color:blue'>http://&lt;domain:port&gt;/" + servicename + "/</span> with HTTP-Method GET and Content-Type text/html<br>";
    res = res + "The machine readable entry point is <span style='font-family:monospace; color:blue'>http://&lt;domain:port&gt;/" + servicename + "/</span> with HTTP-Method GET and Content-Type text/xml<br>";
    res = res + "</p>";
    
    res = res + "</body></html>";
    return res;

  }

}
