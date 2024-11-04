/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guestbook.config;

import java.util.Set;

/**
 *
 * @author kai bechmann
 */
@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(guestbook.resources.EntryResource.class);
        resources.add(guestbook.resources.GuestbookBaseResource.class);
        resources.add(guestbook.resources.HelpResource.class);
    }
    
}
