/*
 * Copyright (c) 2010, 2013, Fados-productions S.L. , omaonk SCP and/or its affiliates. All rights reserved.
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
package omaonk.merge;

import omaonk.utils.*;
import omaonk.xml.*;
import java.text.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.*;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import omaonk.config.Config;
import omaonk.exceptions.FileErrorException;
import omaonk.utils.Utilitats;
import omaonk.params.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import omaonk.exceptions.*;
import org.apache.log4j.Logger;

/**
 *
 * @author omaonk
 */
public class standaloneDocx {
    
    public static String ID_TASK = "";
    private profile tascaXML = null;
    private String xml_file_name = null;
    private Logger logger = Logger.getLogger(standaloneDocx.class.getName());
    private String[] multimediaFolders = {"media", "embeddings", "diagrams"};
     
    public standaloneDocx( profile xmlTask) {
         this.tascaXML = xmlTask;
         xml_file_name = this.tascaXML.ID_TASK;      
    }
    
    public void execute()  throws FileErrorException {
        
        System.out.println("Start merging [Docx]...");
        long start_temps = System.currentTimeMillis();
        int iteracio = 0;
        Config conf = Config.getInstance("");
        String ID_TAK_ID = "[Docx " + xml_file_name +"]"; 
        String base = conf.eval("base");
        String tasks = base + conf.eval("tasks");
        String results = conf.eval("results");
        String app = conf.eval("app");        
        String tmp = app + conf.eval("tmp");
        String templates = app + conf.eval("templates");
        String filename;
        String resultFileName = "";
        Utilitats fileUtils = new Utilitats();
        /*
        System.out.println("[Docx]Variables de configuracion: ");
        System.out.println("[Docx]base: " + base);
        System.out.println("[Docx]tasks: " + tasks);
        System.out.println("[Docx]results: " + results);
        System.out.println("[Docx]app: " + app);
        System.out.println("[Docx]tmp: " + tmp);
        System.out.println("[Docx]templates: " + templates);
*/
            
        filename = fileUtils.sanitizeFilename((String) this.tascaXML.getHmMetas().get("project_name"));
        System.out.println(ID_TAK_ID + " Number of documents to merge: " + this.tascaXML.getNumDocuments());
        
        try {
            File temp = new File(tmp);
            if (!temp.exists()) {
                FileUtils.forceMkdir(temp);
            }
            File task = new File(tmp + xml_file_name + "\\");
            
            if (!task.exists()) {
                FileUtils.forceMkdir(task);
            } else {
                // Si existeix el borrem per si hi han temporals
                FileUtils.forceDelete(task);
                // El creem net.
                FileUtils.forceMkdir(task);
                
            }
        } catch (IOException e) {
            System.err.println(ID_TAK_ID + " Error creant directory:" + tmp + xml_file_name
                    + "\\" + e.getMessage());
        }
        
        Iterator<String> itr = this.tascaXML.getaLlistatContent().iterator();
        System.out.println(ID_TAK_ID + "  Start merging... ");
        
        File file1 = new File("");
        String resultName = xml_file_name + ".docx";
        iteracio = 0;
        int stateMerge = 0;
        while (itr.hasNext() && stateMerge != -1) {
            System.out.println(ID_TAK_ID + " Current document number: " + (iteracio + 1));
            String fileName = itr.next();
            File file2 = new File(fileName);
            System.out.println(ID_TAK_ID + " Merging: " + file2.getAbsolutePath());
            //System.out.println("El resultado se guarda como: " + tmp + xml_file_name + "\\" + resultName);
            
            if (!file1.exists() && iteracio != 0) {
                stateMerge = -1;
                System.err.println("File : " + file1.getAbsolutePath()
                        + " doesn't");
            }
           
            if (stateMerge != -1) {
                MergeDocx m = new MergeDocx(file1, file2, xml_file_name, "docx", resultName);
                stateMerge = m.run(iteracio);
                
                System.out.println(ID_TAK_ID + " Merge OK.");

                // file1 = new File(tmp + xml_file_name + "\\" + resultName);
                file1 = new File(tmp + xml_file_name + "\\" + Path.PATH_FITXER_ORIGEN.eval());
            } else {
                System.err.println(ID_TAK_ID + " Merge Error.");
            }
            
            iteracio++;
        }
        
        if (stateMerge != -1) {
           
               //Agafem la capçalera del word i la modifiquem amb el metas de projecte
            //this.changeHeaders(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval() + "\\word\\", hmMetas.entrySet());

            /*
             * Analitzem tots els Header per modificar els identificadors únics d'imatge, si el header
             * té imatges el seu identificador és: suma de tots els identificadors del docuement.xml + el del header
             * Els identificadors únics d'imatge venen pel tag  <wp:docPr id="1" name="Rectangle 6"/>
             */
            this.changeImageReferenceHeaders(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval() + "\\word\\");

            // suprimimos las variables sin valor
            fileUtils.replace(new File(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval() + "word\\document.xml"), "\\[\\[.*?\\]\\]",
                    "");
             //Copiem els arxius multimedia de la carpeta mediaobject, son arxius de video, audio, diagrames, etc...
            for (int j = 0; j < multimediaFolders.length; j++) {
                String pathMultimedia = tmp + xml_file_name + Path.PATH_FITXER_TMP_DOCX.eval() + multimediaFolders[j];
                String mediaFolder = tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval() + "word\\" + multimediaFolders[j] + "\\";
                fileUtils.copyFolder(new File(pathMultimedia), new File(mediaFolder + "\\"));
            }
            
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            resultFileName = results + filename + "_" + ft.format(dNow) + ".docx";
            fileUtils.saveAsOffice(resultFileName, tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval());
            System.out.println(ID_TAK_ID + " Merged all files Correctly.");

            
            File tempFile = new File(tmp + xml_file_name + "\\");
            System.out.println(ID_TAK_ID + " Removing temporal files."
                    + tempFile.getAbsolutePath());
            
            fileUtils.deleteDirectory(tempFile);
        } else {

            // FileUtils f = new FileUtils();

            File tempFile = new File(tmp + xml_file_name + "\\");
            System.err.println(ID_TAK_ID + " Removing temporal files."
                    + tempFile.getAbsolutePath());
            
            fileUtils.deleteDirectory(tempFile);
            System.err.println(ID_TAK_ID + " Error merging KO.");
            
        }
       
        long end_temps = System.currentTimeMillis();
        long total_temps = end_temps - start_temps;
        elapsed temps = new elapsed(total_temps);
        System.out.println(ID_TAK_ID + " Merge Report:  ");
        System.out.println(ID_TAK_ID + " Total merge time:" + temps.toString() + ", " + iteracio + " documents merged.");
        System.out.println(ID_TAK_ID + " Merge ended " + DocumentType.DOCX + "'.");
        System.out.println(ID_TAK_ID + " Result file in: " + resultFileName);
        
    }
    
    
    private void changeImageReferenceHeaders(String path) {
        xpathDom xpthdom = new xpathDom(path + "document.xml", "//wp:docPr");
        int total_imatges = 0; //Obtenim el número total de referencies a imatges que hi ha dins el document
        try {
            total_imatges = xpthdom.execute().getLength();
        } catch (ParserConfigurationException pce) {
            logger.error(pce.getMessage());
        } catch (SAXException sae) {
            logger.error(sae.getMessage());
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } catch (XPathExpressionException xee) {
            logger.error(xee.getMessage());
        }

        //El total_imatges ens servirà per renumerar les imatges de la capçalera, busquem Header?.xml
        // Ex tag: <wp:docPr id="65" name="Imagen 1"/>
        File wordDir = new File(path);
        FilenameFilter select = new FileListFilter("header", "xml");
        File[] contents = wordDir.listFiles(select);
        if (contents != null) {
            for (File file : contents) {
                xpthdom = new xpathDom(path + file.getName(), "//wp:docPr");
                try {
                    NodeList nodesImatge = xpthdom.execute();
                    for (int i = 0; i < nodesImatge.getLength(); i++) {
                        Node node = nodesImatge.item(i);
                        if (node.hasAttributes()) {
                            Attr attr = (Attr) node.getAttributes().getNamedItem("id");

                            if (attr != null) {
                                attr.setValue(Integer.toString(++total_imatges));
                            }
                        }
                    }
                    // Guardem el resultat
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(xpthdom.doc);
                    StreamResult result = new StreamResult(new File(path + file.getName()));
                    transformer.transform(source, result);

                } catch (ParserConfigurationException pce) {
                    logger.error(pce.getMessage());
                } catch (SAXException sae) {
                    logger.error(sae.getMessage());
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                } catch (XPathExpressionException xee) {
                    logger.error(xee.getMessage());
                } catch (TransformerException tfe) {
                     logger.error(tfe.getMessage());
                } 
            }
        }

    }

    ;
/*
 * Busca els headers del word i substitueix les metadades @param path:
 * Directori omn estan els header @param Metas: Conjunt de metas
 */
private void changeHeaders(String path, Set Metas) {
        Utilitats fileUtils = new Utilitats();
        File wordDir = new File(path);
        // Define a filter for java source files beginning with F
        FilenameFilter select = new FileListFilter("header", "xml");
        File[] contents = wordDir.listFiles(select);
        if (contents != null) {
            for (File file : contents) {
                Iterator itMetas = Metas.iterator();
                while (itMetas.hasNext()) {
                    Map.Entry e = (Map.Entry) itMetas.next();
                    String key = (String) e.getKey();
                    String value = (String) e.getValue();
                    if (file.exists()) {
                        fileUtils.replace(file, "\\[\\[[^\\]]*?" + key + ".*?\\]\\]", value);
                    }
                }
                //Eliminem els tags vuits
                fileUtils.replace(file, "\\[\\[.*?\\]\\]", "");
            }
        }

    }
}
