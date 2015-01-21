/*
 * Copyright (c) 2010, 2013, Fados-productions S.L. , omaonk and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of  Fados-productions S.L. or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * You may contact the author at [albertjuhe@gmail.com]
 * And the copyright holder at [albertjuhe@gmail.com] [Ramón turró 23 - 08005 Barcelona]
 */
package omaonk.xml;

import java.io.*;
import java.util.*;

import net.sf.saxon.s9api.*;
import org.apache.log4j.Logger;
import omaonk.exceptions.*;
import omaonk.params.*;

/**
 *
 * @author Albert
 *
 * Aquesta classe carrega el fitxer xml on hi ha totes les tasques de fusió a
 * realitzar
 */
public class profile {

    private File fitxerTasca;
    private static Logger logger = Logger.getLogger(profile.class.getName());
    private XPathCompiler xpath = null;
    private XdmNode booksDoc = null;
    private ArrayList<String> aLlistatContent = null; //Guarda els nodes content amb el nom de fitxer
    private HashMap hmMetas = null;
    private boolean content = false;
    private boolean metas = false;
    public String ID_TASK;
 
    public profile(File tascaXML) {
        this.fitxerTasca = tascaXML;
        ID_TASK = tascaXML.getName();
        this.aLlistatContent = new ArrayList<String>();
        this.hmMetas = new HashMap();
    }

    /*
     * Carrega el xml a partir del fitxer xml. posteriorment extraurem les
     * dades.
     *
     */
    public int LoadXMLTasca() throws FileErrorException {
        try {
            Processor proc = new Processor(false);
            this.xpath = proc.newXPathCompiler();
            this.xpath.declareNamespace(Xsl.NAMESPACE.eval(), Xsl.PARSER.eval());

            DocumentBuilder builder = proc.newDocumentBuilder();
            builder.setLineNumbering(true);
            builder.setWhitespaceStrippingPolicy(WhitespaceStrippingPolicy.ALL);

            logger.info(fitxerTasca.getPath());
            this.booksDoc = builder.build( this.fitxerTasca);
        } catch (SaxonApiException e) {
            logger.error("Error fitxer:" +  this.fitxerTasca.getName() + " - " + e.getMessage());
            return 0;
        }

        if (this.xpath != null) {
            this.loadMetasNodes();
            this.loadContentNodes();

        } else {
            logger.error("Fallo creación elemento xpath.");
            return 0;
        }

        return 1;
    }

    
    /*
     * A partir del xml carregat extreiem els nodes content, que ens marquen
     * quins fitxers hem de fusionar, el nom del fitxer esta en el atribut href.
     */
    private void loadContentNodes() throws FileErrorException {
        try {
            XPathSelector selector = xpath.compile(Xsl.XPATH_CONTINGUT.eval()).load();
            selector.setContextItem(booksDoc);
            for (XdmItem item : selector) {
                this.content = true;
                QName nameAttribut = new QName("href");
                String nomDocumentMerge = ((XdmNode) item).getAttributeValue(nameAttribut);
                File mergeFile = new File(nomDocumentMerge);
                if (!mergeFile.exists() || !mergeFile.canRead() || !mergeFile.canWrite()) {
                    throw new FileErrorException("Can't merge documents.", "file " + nomDocumentMerge + " in "+ this.fitxerTasca.getName() +"does not exist or is not readeable, please check the path.");
                }
                this.getaLlistatContent().add(nomDocumentMerge);
            }
        } catch (SaxonApiException e) {
            logger.error("Error Loading file task:" + this.fitxerTasca.getName() + " - " + e.getMessage());
        }
    }

    private void loadMetasNodes() {
        try {
            XPathSelector selector = xpath.compile(Xsl.XPATH_METAS.eval()).load();
            selector.setContextItem(booksDoc);
            for (XdmItem item : selector) {
                this.metas = true;
                QName nameAttributKey = new QName("key");
                QName nameAttributValue = new QName("value");
                String key = ((XdmNode) item).getAttributeValue(nameAttributKey);
                String value = ((XdmNode) item).getAttributeValue(nameAttributValue);
                this.getHmMetas().put(key, value); //Nom del meta i valor
            }
        } catch (SaxonApiException e) {
            logger.error("Error loading content:" + this.fitxerTasca.getName() + " - " + e.getMessage());
        }
    }

    /**
     * @return the aLlistatContent
     */
    public ArrayList<String> getaLlistatContent() {
        return aLlistatContent;
    }

    /**
     * @return the hmMetas
     */
    public HashMap getHmMetas() {
        return hmMetas;
    }

   
    /**
     * @return the content
     */
    public boolean teContent() {
        return content;
    }

    /**
     * @return the metas
     */
    public boolean teMetas() {
        return metas;
    }

  
    public void addMeta(String key, String value) {
        this.hmMetas.put(key, value);
    }

    public void addContent(String content) {
        this.aLlistatContent.add(content);
    }

    public int getNumDocuments() {
        return this.aLlistatContent.size();
    }
}
