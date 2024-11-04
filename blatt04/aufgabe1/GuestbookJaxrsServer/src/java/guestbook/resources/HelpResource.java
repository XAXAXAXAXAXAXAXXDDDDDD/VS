/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package guestbook.resources;

import guestbook.view.GuestbookDescription;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author kai bechmann
 */
@Path("webresources/help")
public class HelpResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of HelpResource
     */
    public HelpResource() {
    }

    /**
     * Retrieves representation of an instance of bmiservicejaxrspackage.HelpResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() {
        return GuestbookDescription.getSpecificationAsHtml("GueustbookJaxrsServer");
    }
}
