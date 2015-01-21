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
 */
package omaonk.merge;

import omaonk.params.*;
import omaonk.utils.elapsed;
import java.text.*;
import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import omaonk.config.Config;
import omaonk.exceptions.FileErrorException;
import omaonk.utils.Utilitats;
import omaonk.xml.*;

/**
 *
 * @author omaonk
 */
public class standalonePPTX {

    public static String ID_TASK = "";
    private int iteracio = 0;
    private profile tascaXML = null;
    private int stateMerge = 0;
    private String resultName = "";
    private File file1 = new File("");
    private File xmlfilename = null;
    private String xml_file_name = null;

    public standalonePPTX(profile xmlTask) {
        this.tascaXML = xmlTask;
        xml_file_name = this.tascaXML.ID_TASK; 
    }

    public void execute() throws FileErrorException {

        System.out.println("Start merging [PPTX]...");
        long start_temps = System.currentTimeMillis();
        Config conf = Config.getInstance("");
        
        String ID_TAK_ID = "[Pptx " + xml_file_name +"]";
        String base = conf.eval("base");
        String results = conf.eval("results");
        String app = conf.eval("app");
        String tmp = app + conf.eval("tmp_pptx");
        String filename = "";
        Utilitats fileUtils = new Utilitats();
        /*
        System.out.println("[pptx] Variables de configuracion: ");
        System.out.println("[pptx] base: " + base);
        System.out.println("[pptx] tasks: " + tasks);
        System.out.println("[pptx] results: " + results);
        System.out.println("[pptx] app: " + app);
        System.out.println("[pptx] tmp: " + tmp);
*/
        filename = fileUtils.sanitizeFilename((String) this.tascaXML.getHmMetas().get("project_name"));
        System.out.println(ID_TAK_ID + " Number of documents to merge: " + this.tascaXML.getNumDocuments());


        this.startMerge(tmp, xml_file_name);

        if (stateMerge != -1) {
            System.out.println(ID_TAK_ID + " Merging process ended.");
          
        }

        //Preparacio de pptx
        fileUtils.copyFolder(new File(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval()), new File(tmp + xml_file_name + Path.PATH_FITXER_PPTX.eval()));
        this.copiaMultimediaObjects(tmp + xml_file_name);
        try {
            FileUtils.deleteDirectory(new File(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval()));
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        fileUtils.renameDirectory(tmp + xml_file_name + Path.PATH_FITXER_PPTX.eval(), tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval());

       File comprovacio = new File(tmp + xml_file_name + Path.PATH_FITXER_ORIGEN.eval());

        if (comprovacio.exists()) {
            //Copiem els arxius multimedia de la carpeta mediaobject
          
            System.out.println("Saving result file "
                    + file1.getAbsolutePath() + " as "
                    + results + resultName + "...");
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String resultFileName = results + filename + "_" + ft.format(dNow) + ".pptx";
            fileUtils.saveAsOffice(resultFileName, tmp
                    + xml_file_name + Path.PATH_FITXER_ORIGEN.eval());
        } else {
            System.err.println("This folder doesn't exists:"
                    + comprovacio.getAbsolutePath());
        }

        System.out.println("Result file saved OK.");

        File tempFile = new File(tmp + xml_file_name + "\\");
       
        fileUtils.deleteDirectory(tempFile);

        System.out.println("Temporal files removed.");
         
        long end_temps = System.currentTimeMillis();
        long total_temps = end_temps - start_temps;
        elapsed temps = new elapsed(total_temps);

        System.out.println("MERGE TIME:" + temps.toString() + ", " + iteracio + " Documents.");
        System.out.println("End meging PPTX Files" + DocumentType.PPTX + "'.");
    }

    private void startMerge(String tmp, String xml_file_name) {
        Iterator<String> itr = this.tascaXML.getaLlistatContent().iterator();
        System.out.println("Start merging... ");
        iteracio = 0;

        /*
         * Comencem la fusió entre els documents que marca el xml de la tasca.
         */

        while (itr.hasNext() && stateMerge != -1) {
            System.out.println("Current document: " + (iteracio +1));
            String nom = itr.next();
            File file2 = new File(nom);
            System.out.println("Merging: " + file2.getAbsolutePath());

            if (!file1.exists() && iteracio != 0) {
                stateMerge = -1;
                System.err.println("File " + file1.getAbsolutePath()
                        + " doesn't exists.");
            }
            /*
             * if (!file1.getAbsolutePath().endsWith(".pptx") && iteracio != 0)
             * { stateMerge = -1;System.err.println("El fichero : " +
             * file1.getAbsolutePath()+ " no es un pptx"); }
             */
            if (!file2.getAbsolutePath().endsWith(".pptx")) {
                stateMerge = -1;
                System.err.println("File : " + file2.getAbsolutePath()
                        + " doesn't exists.");
            }
            // Fem el merge de dos pptx
            if (stateMerge != -1) {
                MergePPTX m = new MergePPTX(file1, file2, xml_file_name,
                        "pptx", resultName, this.tascaXML.getaLlistatContent().size());
                stateMerge = m.run(iteracio);
                //System.out.println("Merge OK.");

                file1 = new File(tmp + xml_file_name + "\\" + Path.PATH_FITXER_ORIGEN.eval());
            } else {
                System.err.println("Merge KO.");
            }
            iteracio++;
        }
        if (iteracio == 0) {
            System.err.println("No files to merge!!!.");
        }
    }

  
    private void copiaMultimediaObjects(String pathFile) {
        Utilitats f = new Utilitats();
        String mediaTypes[] = {"media", "embeddings", "diagrams"};

        for (int i = 0; i < mediaTypes.length; i++) {
            String pathMultimedia = pathFile + Path.PATH_FITXER_TMP_PPTX.eval() + mediaTypes[i];
            String mediaFolder = pathFile + Path.PATH_FITXER_ORIGEN.eval() + "ppt\\" + mediaTypes[i] + "\\";
            f.copyFolder(new File(pathMultimedia), new File(mediaFolder + "\\"));
        }
    }
}
