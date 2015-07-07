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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.transforms;

import java.io.*;
import java.util.HashMap;
import org.apache.log4j.Logger;
import omaonk.params.*;
import omaonk.fonts.*;
import omaonk.utils.Utilitats;
import omaonk.utils.xslTransform;

/**
 *
 * @author omaonk
 */
public class TransformPptx extends transform {

    private static Logger logger = Logger.getLogger(TransformPptx.class.getName());
    int nFitxers;
    public int nSlides = 0;

    public void loadParams() {
    }

    public TransformPptx(String job, int iteracio, int nFitxers, boolean annexes) {
        super();
        this.xslTranformacio = new xslTransform();
        this.job = job;
        this.iteracio = iteracio;
        this.nFitxers = nFitxers;
        this.tmp = app + conf.eval("tmp_pptx");
        this.xsl_path = app + conf.eval("xsl_pptx");
    }

    public void Transform() {
        HashMap<String,String> params = new HashMap(); 
        boolean ultimaiteracio = (iteracio == (nFitxers - 1));
        //logger.info("Inici de les transformacions XSL");

        String input = tmp + job + "/file1/[Content_Types].xml";
        String output = tmp + job + "/result/[Content_Types].xml";
        String stylesheet = this.xsl_path + "index.xsl";

        params.put("pptx.job", job);
        params.put("pptx.doc2", "file2");
        params.put("pptx.docfinal", "result");
        params.put("pptx.tmp", "file:/" + tmp.replace('\\', '/') + job + "/");
        params.put("pptx.merge", tmp.replace('\\', '/') + job + "/file2/Content_Types.xml");
        params.put("numdoc", String.valueOf(this.iteracio + 1));

        if (ultimaiteracio) {

          params.put("tasca.xml", tasks.replace('\\', '/') + job + ".xml");

        }

        //this.execute(input, output, stylesheet, params);
        xslTranformacio.execute(input, output, stylesheet, params);
        
        //Creació de l'element relacio
        output = tmp + job + "/relation.xml";
        stylesheet = this.xsl_path + "relacions_files.xsl";
        xslTranformacio.execute(input, output, stylesheet, params);
        //notesMaster1.xml
        File f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/notesMasters/");
        //notesMaster2.xml
        File f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "notesMasters/");
        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);
        
        //CustomXML
        /*
        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "\\customXML\\");
        f2 = new File(tmp + job + Path.PATH_FITXER_PPTX.eval() + "\\customXML\\");
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);
*/

        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/theme/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "theme/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, false, false, iteracio, ultimaiteracio);

        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/tags/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "tags/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, false, false, iteracio, ultimaiteracio);

        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/notesSlides/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "notesSlides/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);
        
        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/charts/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "charts/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);


        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/slideMasters/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "slideMasters/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);



        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/slideLayouts/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "slideLayouts/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);



        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/handoutMasters/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "handoutMasters/");

        //el parametre es el numero de fitxers es mes un
        this.copyRenumberedFiles(f1, f2, job, true, false, iteracio, ultimaiteracio);


        f1 = new File(tmp + job + Path.PATH_FITXER_MERGE.eval() + "ppt/slides/");
        f2 = new File(tmp + job + Path.PATH_FITXER_TMP_PPTX.eval() + "slides/");

        //el parametre es el numero de fitxers es mes un
        this.nSlides = this.copyRenumberedFiles(f1, f2, job, true, true, iteracio, ultimaiteracio);


        input = tmp + job + "/relationsDrawing.xml";
        output = tmp + job + "/tmp.tmp";
        stylesheet = this.xsl_path + "drawing.xsl";

        params.put("numdoc", String.valueOf(this.iteracio + 1));
        params.put("pptx.tmp", "file:/" + tmp.replace('\\', '/') + job + "/result");
        params.put("pptx.doc2", tmp.replace('\\', '/') + job + "/file2");

        //this.execute(input, output, stylesheet, params);
        xslTranformacio.execute(input, output, stylesheet, params);

        input = tmp + job + "/file1/ppt/_rels/presentation.xml.rels";
        output = tmp + job + "/result/ppt/_rels/presentation.xml.rels";
        stylesheet = this.xsl_path + "mergeRelacions.xsl";

        params.put("pptx.numrelacions", "file:/" + tmp.replace('\\', '/') + job + "/num_relation.xml");
        params.put("pptx.relacions", tmp.replace('\\', '/') + job + "/relation.xml");
        params.put("pptx.merge", tmp.replace('\\', '/') + job + "/file2/ppt/_rels/presentation.xml.rels");
    
        if (iteracio == (nFitxers - 1)) {
           params.put("tasca.xml", tasks.replace('\\', '/') + job + ".xml");

        }

        //this.execute(input, output, stylesheet, params);
        xslTranformacio.execute(input, output, stylesheet, params);

        input = tmp + job + "/file1/ppt/presentation.xml";
        output = tmp + job + "/result/ppt/presentation.xml";
        stylesheet = this.xsl_path + "presentacion.xsl";
        fonts fontsFiles = new fonts();
        int maxId = fontsFiles.countMaxId(tmp + job + "/file1/ppt/fonts/");



        params.put("pptx.relacions", tmp.replace('\\', '/') + job + "/num_relation.xml");

        params.put("pptx.merge", tmp.replace('\\', '/') + job + "/file2/ppt/presentation.xml");

        params.put("pptx.doc2", tmp.replace('\\', '/') + job + "/file2");

        params.put("pptx.doc1", tmp.replace('\\', '/') + job + "/file1");

        params.put("maxId", String.valueOf(maxId));

        if (iteracio == 1) {

            params.put("portada", "0");
        }

        if (iteracio == (nFitxers - 1)) {
          
            params.put("tasca.xml", tasks.replace('\\', '/') + job + ".xml");

            //serveix per saber on crear el document final...
            params.put("pptx.doc_final", "file:/" + tmp.replace('\\', '/') + job + "/result");

        }

        // this.execute(input, output, stylesheet, params);
        xslTranformacio.execute(input, output, stylesheet, params);
        //logger.info("Final de les transformacions XSL");
    }

    public int countFiles(File dir, String fileName) {

        Utilitats f = new Utilitats();
        String currentFile;
        int result = 0;

        if (dir.exists()) {

            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {

                if (files[i].isFile()) {

                    currentFile = files[i].getName();

                    currentFile = f.removeFileFormat(currentFile);
                    currentFile = f.removeLastDigits(currentFile);

                    if (currentFile.equals(fileName)) {
                        result++;
                    }
                }


            }
        }

        return result;
    }

    public int copyRenumberedFiles(File srcFile, File destFile, String job, boolean cridarXSL, boolean esSlides, int iteracio, boolean ultimaiteracio) {

        Utilitats f = new Utilitats();
        String input;
        String output;
        String stylesheet;
        String newFileNameAux;
        String originalFileName;
        String currentFile = "";
        int k = 0;

        if (srcFile.exists()) {




            File[] files = srcFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {

                    originalFileName = files[i].getName().toString();

                    String fileFormat = f.getFileFormat(originalFileName);
                    int numFitxer = Integer.parseInt(f.getLastDigits(originalFileName));

                    originalFileName = f.removeFileFormat(originalFileName);

                    originalFileName = f.removeLastDigits(originalFileName);

                    if (currentFile.equals("") || !currentFile.equals(originalFileName)) {

                        currentFile = originalFileName;
                        k = this.countFiles(destFile, originalFileName);
                    }

                    int numNouFitxer = k + numFitxer;


                    newFileNameAux = originalFileName + String.valueOf(numNouFitxer) + fileFormat;
                    File newFile = new File(destFile.getPath() + "/" + newFileNameAux);

                    if (originalFileName.equals("slideMaster")) {
                        input = srcFile.getAbsolutePath() + "/" + files[i].getName();
                        output = destFile.getAbsolutePath() + "/" + newFileNameAux;
                        stylesheet = this.xsl_path + "relacions.xsl";

                        HashMap<String, String> params = new HashMap();

                        params.put("numdoc", String.valueOf(this.iteracio + 1));

                        params.put("numfile", String.valueOf(numNouFitxer));

                        params.put("pptx.job", job);

                        params.put("path.tmp", tmp.replace('\\', '/'));

                        //this.execute(input, output, stylesheet, params);
                        xslTranformacio.execute(input, output, stylesheet, params);
                    } else {
                        try {
                            f.copyFile(files[i], newFile);
                        } catch (IOException ioe) {
                            logger.error(ioe.getMessage());
                        }
                    }

                    if (cridarXSL) {
                        input = srcFile.getAbsolutePath() + "/_rels/" + files[i].getName() + ".rels";
                        output = destFile.getAbsolutePath() + "/_rels/" + newFileNameAux + ".rels";
                        stylesheet = this.xsl_path + "relacions.xsl";

                        HashMap<String, String> params = new HashMap();

                        params.put("numdoc", String.valueOf(this.iteracio + 1));

                        params.put("numfile", String.valueOf(numNouFitxer));

                        params.put("pptx.job", job);

                        params.put("path.tmp", tmp.replace('\\', '/'));

                        //this.execute(input, output, stylesheet, params);
                        xslTranformacio.execute(input, output, stylesheet, params);
                    }
                }
            }
        }
        return k;
    }
}
