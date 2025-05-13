-- isy-webflow Datenbankschema
--
-- Der Inhalt dieses SQL-Skripts wird in das Tabellenerzeugungs-Skript der
-- konkreten Anwendung eingebunden, die isy-webflow benutzt.
--
-- Autor Capgemini sd&m AG, Christoph Brehm
-- Version $Id: tabellen-erzeugen.sql 72739 2012-01-18 10:06:15Z sdm_asubasi $


-- Tabelle zum Ablegen von temporären Web-Ressourcen. Diese Tabelle wird benötigt,
-- wenn der ResourceController oder eine seiner Subklassen von der Anwendung benutzt
-- wird.
    CREATE TABLE TEMP_WEB_RESOURCE (
    	TEMP_WEB_RESOURCE_ID NUMBER(19,0) NOT NULL,
    	BENUTZERKENNUNG VARCHAR2(100 CHAR) NOT NULL,
    	BHKNZ VARCHAR2(6 CHAR) NOT NULL,
    	KENNZEICHEN VARCHAR2(30 CHAR),
    	MIMETYPE VARCHAR2(255 CHAR),
    	DATEINAME VARCHAR2(255 CHAR),
    	INHALT BLOB NOT NULL,
    	ZEITPUNKT TIMESTAMP DEFAULT SYSTIMESTAMP,
    	PRIMARY KEY(TEMP_WEB_RESOURCE_ID)
    );
    CREATE SEQUENCE SEQ_TEMP_WEB_RESOURCE_ID;
