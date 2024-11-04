Autor: Kai Bechmann 
Verteilte Systeme WS 2023
Blatt 04 Arbeitsumgebung Linux, REST-basierte Webservices
Aufgabe 02
Vorbedingung:
	Projektverzeichnis GuestbookJaxrsClient mit Inhalt zum NetBeansProjekt von GuestbookJaxrsClient ist im Ordner im NetBeans-Projektordner $HOME/NetBeansProjects vorhanden.

Generieranleitung:
	1. NetBeans IDE starten und in NetBeans das Projekt GuestbookJaxrsClient öffnen.
	2. Clean & Build ausführen
	
Installationsanleitung:
	1. Im Verzeichnis $HOME/NetBeansProjects/ GuestbookJaxrsClient/dist
	liegt die Datei: GuestbookJaxrsClient.war
	2.Die Datei GuestbookJaxrsServer.war in den webapps-Ordner von Tomcat kopieren, d.h. nach <tomcat-install-dir>/webapps kopieren.
	<tomcat-install-dir> ist zielsystemabhängig und auf der Praktikum-VM gilt:
	tomcat-install-dir> = /home/vmuser/apache-tomcat-9.0.41.

Bedienungsanleitung:
Um die Startseite der Webanwendung aufzurufen, ist folgendermaßen vorzugehen:
	1. Browser starten
	2. Im Browser als URL eingeben: http://localhost:8080/GuestbookJaxrsClient
