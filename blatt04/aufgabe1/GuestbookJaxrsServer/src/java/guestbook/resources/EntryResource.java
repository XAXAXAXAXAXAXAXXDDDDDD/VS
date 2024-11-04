/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package guestbook.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author kai bechmann
 */
@Path("/")
public class EntryResource {

    @Context
    private UriInfo context;

    @Context
    HttpServletRequest request;

    /**
     * Creates a new instance of EntryResource
     */
    public EntryResource() {
    }

    /**
     * Retrieves representation of an instance of
     * guestbook.resources.EntryResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response getXml() {
        try {
            String responseText = "<servicedescription>"
                    + "<link rel=\"guestbook\" type=\"application/xml\" href=\"webresources/guestbook\" />"
                    + "<link rel=\"help\" type=\"text/html\" href=\"webresources/help\" />"
                    + "<link rel=\"wadl\" type=\"application/xml\" href=\"application.wadl\" />"
                    + "</servicedescription>";
            return Response.status(Response.Status.OK).entity(responseText).type(MediaType.TEXT_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_XML).build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getText() {
        try {
            String responseText = getIndexPageHTML();
            return Response.status(Response.Status.OK).entity(responseText).type(MediaType.TEXT_HTML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_HTML).build();
        }

    }

    private String getIndexPageHTML() {
        String pathToResource = request.getRequestURL() + "webresources";
        String contextpath = request.getContextPath();
        char first = contextpath.charAt(0);
        if (first == '/') {
            contextpath = contextpath.substring(1);
        }

        String requestUrl = contextpath;
        String s = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>" + contextpath + ": Rest-based Webservice, Index page</title>";
        s += "<style type=\"text/css\">.code {color:blue;font-family: monospace}table {border-style: solid;width: 1px; }</style>";
        s += "</head><body><h3>" + contextpath + ": a REST-based Webservice</h3>";
        s += "<p>" + contextpath + " is a REST-based Webservice which is implemented by the project also called " + contextpath + " developed using Netbeans 19.</p>"
                + "<p>The general URL of the webservice resources is<br />";
        s += "<span class=\"code\">" + contextpath + "/webresources/{servicepath}/{Resource-URI}</span></p>";
        s += "<p>The URL of the specification of the webservice is <br /><a href=\"" + pathToResource + "/help\"><span class=\"code\">" + contextpath + "/help</span></a></p>";
        s += "<p style=\"display:none\">";
        s += "The following URL shows how the specification of the REST-based webservice should be<br />";
        s += "<a href=\"" + contextpath + ">HelpDescription\"><span class=\"code\">" + contextpath + "/HelpDescription</span></a></p>";
        s += "<p>The WADL-File of the webservcie can be retrieved at<br />";
        String pathToWadl = request.getRequestURL() + "application.wadl";
        s += "<a href=\"" + pathToWadl + "\"><span class=\"code\">" + contextpath + "/application.wadl</span></a>  </p>";
        s += "<p> The URL of the resource of the resource <span style=\"font-weight:bold\">guestbook</span> is <br />";
        s += "<a href=\"" + pathToResource + "/guestbook\"><span class=\"code\">" + contextpath + "/guestbook</span></a></p>";

        s += "<p>The URL of the service description of the webservice is <br /><a href=\"" + request.getRequestURL() + "\"><span class=\"code\">" + request.getRequestURL() + "</a> ";
        s += "used in a request with the HTTP-Header Accept=&quot;text/xml&quot;.</span> <br> Use the below link to the client to test it.</p>";
        s += "<h4>A Web Application Client for " + contextpath + "is:</h4>";
        s += "<p><a href=\"/GuestbookJaxrsClient\">Guestbook</a></p></body></html>";

        return s;
    }
}
