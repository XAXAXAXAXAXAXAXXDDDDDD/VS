/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package guestbook.resources;

import guestbook.model.GuestbookEntries;
import guestbook.model.GuestbookEntry;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author kai bechmann
 */
@Path("webresources/guestbook")
public class GuestbookBaseResource {

    private static final String CRLF = System.lineSeparator();

    private static final String RESOURCE_PATH = "/home/vmuser/NetBeansProjects/GuestbookJaxrsServer/web/WEB-INF/";

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GuestbookResource
     */
    public GuestbookBaseResource() {
    }

    /**
     * Retrieves representation of an instance of
     * guestbook.resources.GuestbookResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getResources() {
        Response response;

        try {
            String responseText = this.getServiceDescription();

            response = Response.status(Response.Status.OK).entity(responseText).type(MediaType.APPLICATION_XML).build();

            return response;
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
        }
    }

    private String getServiceDescription() {
        String res;
        res = "<?xml version=\"1.0\" ?>" + CRLF;
        res += "<guestbook>" + CRLF;
        res += "<link rel=\"entries\" type=\"application/xml\" href=\"entries\" />" + CRLF;
        res += "<link rel=\"entries\" type=\"text/plain\" href=\"entries\" />" + CRLF;
        res += "</guestbook>" + CRLF;

        return res;
    }

    @GET
    @Path("entries")
    @Produces(MediaType.APPLICATION_XML)
    public Response getCurrentEntriesXML() {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String xmltext = book.getEntriesAsXml();
            var entryList = book.getEntryList();

            if (entryList.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_XML).build();
            }

            return Response.status(Response.Status.OK).entity(xmltext).type(MediaType.APPLICATION_XML).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<Error>Could not fetch resource guestbook.</Error>").type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
        }
    }

    @GET
    @Path("entries")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCurrentEntriesText() {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String plainText = book.getEntriesAsTextPlain();
            var entryList = book.getEntryList();

            if (entryList.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).type(MediaType.TEXT_PLAIN).build();
            }

            return Response.status(Response.Status.OK).entity(plainText).type(MediaType.TEXT_PLAIN).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not fetch resource guestbook.").type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @POST
    @Path("entries")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_XML)
    public Response postNewEntry(@FormParam("name") String name, @FormParam("msg") String msg) throws IOException {
        try {

            String author = name;
            if (author != null && msg != null) {
                GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);

                // author and name do not have to be unique, else you could handle it this way:
//                var result = book.searchEntries(author, "text/plain");
//                if(result != null && result.contains(msg)){
//                    return Response.status(409).entity("<Error>Resource is already existing</Error>").type(MediaType.APPLICATION_XML).build();
//                }
                GuestbookEntry entry = book.create(author, msg);

                // prepare response
                String uriStr = context.getAbsolutePath().toString();
                int id = entry.getId();
                uriStr = uriStr + "/" + id;
                URI uri;
                try {
                    uri = new URI(uriStr);
                } catch (Exception e) {
                    uri = null;
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<Error>Error while creating URI for response.</Error>").type(MediaType.APPLICATION_XML).build();
                }

                String representation = book.getEntryXML(entry.getId());
                Response response;
                response = Response.created(uri).entity(representation).build();
                return response;
            } else {
                return Response.status(400).entity("<Error>Empty Parameter name or msg.</Error>").type(MediaType.APPLICATION_XML).build();
            }
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ioe.toString()).type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).type(MediaType.APPLICATION_XML).build();
        }
        // TODO: Check for MIME Type
    }

    @GET
    @Path("entries/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEntryByIdXML(@PathParam("id") int id) throws IOException {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String entryText = book.getEntryXML(id);

            if (entryText == null) {
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
            }

            return Response.status(Response.Status.OK).entity(entryText).type(MediaType.APPLICATION_XML).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<Error>Could not fetch resource guestbook.</Error>").type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<Error>" + e.toString() + "</Error>").type(MediaType.APPLICATION_XML).build();
        }
    }

    @GET
    @Path("entries/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getEntryByIdText(@PathParam("id") int id) throws IOException {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String entryText = book.getEntryPlain(id);

            if (entryText == null) {
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).build();
            }

            return Response.status(Response.Status.OK).entity(entryText).type(MediaType.TEXT_PLAIN).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not fetch resource guestbook.").type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @DELETE
    @Path("entries/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteEntryById(@PathParam("id") int id) throws IOException {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String xmlTextOfDeletedEntry = book.deleteEntry(id);

            if (xmlTextOfDeletedEntry != null) {
                return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_XML).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Could not find entry with this ID.").type(MediaType.APPLICATION_XML).build();
            }

        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error.").type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
        }
    }

    @PUT
    @Path("entries/{id}")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_XML)
    public Response putModifyEntry(@PathParam("id") int id, @FormParam("name") String name, @FormParam("msg") String msg) throws IOException {
        try {
            String author = name;
            if (author != null && msg != null) {
                GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
                String xmltext = book.putEntry(id, name, msg);

                // resource does not exist
                if (xmltext == null) {
                    GuestbookEntry entry = book.create(author, msg);

                    String uriStr = context.getAbsolutePath().toString();
                    uriStr = uriStr.substring(0, uriStr.lastIndexOf('/'));
                    uriStr = uriStr + "/" + entry.getId();
                    URI uri;
                    try {
                        uri = new URI(uriStr);
                    } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("<Error>Error while creating URI for response.</Error>").type(MediaType.APPLICATION_XML).build();
                    }

                    String representation = book.getEntryXML(entry.getId());
                    Response response;
                    response = Response.created(uri).entity(representation).build();
                    return response;
                }

                // ok, rsc has been changed
                return Response.status(Response.Status.OK).entity(xmltext).type(MediaType.APPLICATION_XML).build();
            } else {
                return Response.status(400).entity("<Error>Empty Parameter name or msg.</Error>").type(MediaType.APPLICATION_XML).build();
            }
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error.").type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
        }
        // TODO: Check for MIME Type
    }

    @GET
    @Path("entries/search")
    @Produces(MediaType.TEXT_PLAIN)
    public Response searchEntriesText(@QueryParam("name") String n) {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String entryText = book.searchEntries(n, "text/plain");

            String resCheck = "No entries found";

            if (entryText.equals(resCheck)) {
                return Response.status(Response.Status.NO_CONTENT).entity(entryText).type(MediaType.TEXT_PLAIN).build();
            }

            return Response.status(Response.Status.OK).entity(entryText).type(MediaType.TEXT_PLAIN).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not fetch resource guestbook.").type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("entries/search")
    @Produces(MediaType.APPLICATION_XML)
    public Response searchEntriesXML(@QueryParam("name") String n) {
        try {
            GuestbookEntries book = new GuestbookEntries(RESOURCE_PATH);
            String entryText = book.searchEntries(n, "text/xml");

            String resCheck = "<?xml version=\"1.0\" ?>";
            resCheck = resCheck + "<guestbooklist>";
            resCheck = resCheck + "</guestbooklist>";

            if (entryText.equals(resCheck)) {
                return Response.status(Response.Status.NO_CONTENT).entity(entryText).type(MediaType.APPLICATION_XML).build();
            }

            return Response.status(Response.Status.OK).entity(entryText).type(MediaType.APPLICATION_XML).build();
        } catch (IOException ioe) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not fetch resource guestbook.").type(MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
        }
    }
}
