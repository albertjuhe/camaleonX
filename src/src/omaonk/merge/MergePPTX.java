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

import java.io.File;
import java.io.IOException;

import omaonk.params.DocumentType;
import omaonk.params.Path;
import omaonk.transforms.TransformPptx;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.eclipsesource.json.*;
import omaonk.params.general;
import omaonk.utils.*;

/**
 * 
 * @author omaonk
 */
public class MergePPTX extends Merge {

    private static Logger LOGGER = Logger.getLogger(MergePPTX.class.getName());
    private int numFitxers = 0;
    private final String xsl_pptx;
    private final String[] elements_estructurals = {"charts","tags", "theme", "slides", "handoutMasters", "notesMasters", "notesSlides", "slideLayouts"};
    public int Slides=0;
    private final String PPTXFOLDERSTRUCTURE = " {\n" +
" 	\"folders\": [{\n" +
" 		\"name\": \"_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"customXML\"\n" +
" 	}, {\n" +
" 		\"name\": \"customXML/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"docProps\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/charts\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/charts/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/diagrams\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/drawings\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/drawings/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/embeddings\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/embeddings/embeddings1\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/embeddings/embeddings2\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/fonts\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/handoutMasters\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/handoutMasters/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/media\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/media/media1\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/media/media2\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/notesMasters\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/notesMasters/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/notesSlides\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/notesSlides/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slideLayout\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slideLayout/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slideMaster\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slideMaster/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slides\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/slides/_rels\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/tags\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/theme\"\n" +
" 	}, {\n" +
" 		\"name\": \"ppt/theme/_rels\"\n" +
" 	}]\n" +
" }";

    public MergePPTX(File file1, File file2, String id_task, String type,
            String fileNameResult, int numFitxers) {
        this.file1 = file1;
        this.file2 = file2;
        this.id_task = id_task;
        this.type = type;
        this.fileNameResult = fileNameResult;
        this.numFitxers = numFitxers;
        this.tipo_document = DocumentType.PPTX.eval();
        this.tmp = app + conf.eval("tmp_pptx");
        //this.skeleton = app + conf.eval("skeleton_pptx");
        this.xsl_pptx = app + conf.eval("xsl_pptx");
        this.temp = new File(tmp);
        this.task = new File(tmp + this.id_task + "/");
        //Crea l'estructura temporal final on es guarden els objectes immutables
        this.creaEstructuraTemporal();


    }

    private void creaEstructuraTemporal() {
       // File skeletonFile = new File(skeleton);
        Utilitats fileUtils = new Utilitats();
        /*
        if (skeletonFile.exists()) {
         //   fileUtils.copyFolder(new File(skeleton), new File(tmp + this.id_task));
            this.createTemporalPPTXFolders(tmp + this.id_task,this.PPTXFOLDERSTRUCTURE);
        } else {
            LOGGER.error("Error: Can't find pptx skeleton in " + skeleton);
        }
        */
        this.createTemporalPPTXFolders(tmp + this.id_task,this.PPTXFOLDERSTRUCTURE);
        fileUtils.renameDirectory(tmp + this.id_task + "/result/", tmp + this.id_task + "/result_final/");
    }
    
    /*
    * Create the temporal folders for PPTX
    * The folder structure to create is described via JSON
    Folder structure:     
  
    */
    private void createTemporalPPTXFolders(String targetFolder,String structure) {
        Utilitats fileUtils = new Utilitats();
        
        fileUtils.mkDir(new File(targetFolder));
        fileUtils.mkDir(new File(targetFolder + "/result/"));
              
        JsonArray items= Json.parse(structure).asObject().get("folders").asArray();
        String path = targetFolder + "/result/";
        for(JsonValue item : items) {
            String value = item.asObject().get("name").asString();
            File file = new File(path + value);
            if (!file.exists()) {
                if (file.mkdir()) {
                   LOGGER.debug("directory created successfully");
                } else {
                   LOGGER.error("directory is not created");
                }
            }
        }
    }

    protected int merging(int iteracio) {

        Utilitats fileUtils = new Utilitats();

        //Create temporal media files folder and clean temporal folders
        this.createStructure(Path.PATH_FITXER_TMP_PPTX.eval());
      
       // File skeletonFile = new File(skeleton);
        this.createTemporalPPTXFolders(tmp + this.id_task,this.PPTXFOLDERSTRUCTURE);      
            
        if (task.exists()) {
            //fileUtils.copyFolder(new File(skeleton), task);
          fileUtils.unzip(file2, new File(tmp + id_task + Path.PATH_FITXER_MERGE.eval()));

            // Renombrem el fitxer [Content_types].xml del segon document,
            // perque sinó el xsl no el llegeix, el deicem com:
            // Content_Types.xml
            if (general.debug == 1) {
                LOGGER.info("Converting Content_type ...");
            }
            try {
                fileUtils.copyFile(new File(tmp + id_task + Path.PATH_FITXER_MERGE.eval()
                        + "/[Content_Types].xml"), new File(tmp
                        + id_task + Path.PATH_FITXER_MERGE.eval()
                        + "/Content_Types.xml"));

                // copiem el ppt\presProps.xml del fitxer 1
                if (general.debug == 1) {
                    LOGGER.info("Copiant presProps.xml ...");
                }
                fileUtils.copyFile(new File(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval()
                        + "/ppt/presProps.xml"), new File(tmp
                        + id_task + Path.PATH_FITXER_RESULTANT.eval()
                        + "/ppt/presProps.xml"));
                if (general.debug == 1) {
                    LOGGER.info("Copiant tableStyles.xml ...");
                }
                fileUtils.copyFile(new File(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval()
                        + "/ppt/tableStyles.xml"), new File(tmp
                        + id_task + Path.PATH_FITXER_RESULTANT.eval()
                        + "/ppt/tableStyles.xml"));

                if (general.debug == 1) {
                    LOGGER.info("Copiant viewProps.xml ...");
                }
                fileUtils.copyFile(new File(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval()
                        + "/ppt/viewProps.xml"), new File(tmp
                        + id_task + Path.PATH_FITXER_RESULTANT.eval()
                        + "/ppt/viewProps.xml"));

                // -------------- DocProps ---------------------
                if (general.debug == 1) {
                    LOGGER.info("Copying DocProps ...");
                }
                copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/docProps/",
                        tmp + id_task + Path.PATH_FITXER_RESULTANT.eval() + "/docProps/");


                //-------------- _rels ---------------------------------------
                if (general.debug == 1) {
                    LOGGER.info("Copying rels.xml ...");
                }
                fileUtils.copyFile(new File(xsl_pptx + "/rels.xml"), new File(tmp
                        + id_task + Path.PATH_FITXER_RESULTANT.eval()
                        + "/_rels/.rels"));
            } catch (IOException ioe) {
                LOGGER.error(ioe.getMessage());
            }

            //Tractament d'elements estructurals sense tractament, nomes copia
            for (int i = 0; i < elements_estructurals.length; i++) {
                if (general.debug == 1) {
                    LOGGER.info("Copiant " + elements_estructurals[i] + " ...");
                }
                copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/" + elements_estructurals[i] + "/",
                        tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + elements_estructurals[i] + "/");
                copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/" + elements_estructurals[i] + "/_rels/",
                        tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + elements_estructurals[i] + "/_rels/");
            }
            // ------- customXML ----
            /*
            if (general.debug == 1) {
                logger.info("Copy customXML ...");
            }
            this.copiaContingutEstructuralXSL(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "\\customXML\\",
                    tmp + id_task + Path.PATH_FITXER_PPTX.eval() + "customXML\\",
                    xsl_pptx + "relacions.xsl");
            copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "\\customXML\\_rels\\",
                    tmp + id_task + Path.PATH_FITXER_PPTX.eval() + "customXML\\_rels\\");
*/
            // ------- slideMasters, son les plantilles master per crear slides ----
            if (general.debug == 1) {
                LOGGER.info("Copying slideMasters ...");
            }
            this.copiaContingutEstructuralXSL(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/slideMasters/",
                    tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + "slideMasters/",
                    xsl_pptx + "relacions.xsl");
            copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/slideMasters/_rels/",
                    tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + "slideMasters/_rels/");



            // -------------- fonts ---------------------
            if (general.debug == 1) {
                LOGGER.info("Copiant fonts ...");
            }
            copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/fonts/",
                    tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + "fonts/");
            copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_MERGE.eval() + "/ppt/fonts/",
                    tmp + id_task + Path.PATH_FITXER_TMP_PPTX.eval() + "fonts/");

            //------------- Diagrams ----------------------------------------
            if (general.debug == 1) {
                LOGGER.info("Copying Diagrams ...");
            }
            String typeMedia = "diagrams";
            String pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_PPTX.eval() + typeMedia;
            File media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/" + typeMedia + "/");


            if (iteracio == 1) {
                if (media.exists()) {
                    //f.copyFolder(media, new File(tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval() + "\\ppt\\diagrams\\"));
                    fileUtils.copyFolder(media, new File(pathMultimedia + "/"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "/ppt/" + typeMedia + "/");
            /*
            Els diagrams tenen estructura XML i contenen una carpeta _rels que s'han de modificar
            els seus arxius per canviar els punteres a imatges
            ../imatges/imatgexx.png -> ../../imatges/imatges(iteracio)/imatgexx.png
             */
            File relsDiagrams = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "ppt/" + typeMedia + "/_rels/");

            if (relsDiagrams.exists()) {
                File[] files = relsDiagrams.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        if (files[i].getName().toLowerCase().endsWith(".xml.rels")) {
                            fileUtils.replace(files[i], "Target=\"\\.\\./media/", "Target=\"../../media/media" + (iteracio + 1) + "/");
                        }
                    }
                }
            }
            if (media.exists()) {
                //f.copyFolder(media, new File(tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval() + "/ppt/"+typeMedia+"/" + typeMedia + (iteracio + 1) + "/"));
                fileUtils.copyFolder(media, new File(pathMultimedia + "/" + typeMedia + +(iteracio + 1) + "/"));
            }





            //---------------------- Drawings ----------
            if (general.debug == 1) {
                LOGGER.info("Copiant Drawings ...");
            }
            copiaContingutEstructural(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/drawings/",
                    tmp + id_task + Path.PATH_FITXER_RESULTANT.eval() + "/ppt/drawings/");
            File drawings_rels = new File(tmp + id_task
                    + Path.PATH_FITXER_ORIGEN.eval()
                    + "/ppt/drawings/_rels/");
            if (drawings_rels.exists()) {
                fileUtils.copyFolder(
                        new File(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval()
                        + "/ppt/drawings/_rels/"), new File(tmp
                        + id_task + Path.PATH_FITXER_RESULTANT.eval()
                        + "/ppt/drawings/_rels/"));
            }

            drawings_rels = new File(tmp + id_task
                    + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/drawings");
            // if(drawings_rels.exists()){
            TractamentDrawings td = new TractamentDrawings(tmp + id_task
                    + Path.PATH_FITXER_ORIGEN.eval() + "/", tmp + id_task
                    + Path.PATH_FITXER_MERGE.eval() + "/", tmp + id_task
                    + Path.PATH_FITXER_RESULTANT.eval() + "/", tmp + id_task
                    + "/");


            TransformPptx mergeXSLPPTX = new TransformPptx(id_task, iteracio, numFitxers, this.annexes);
            mergeXSLPPTX.Transform();
             this.Slides = mergeXSLPPTX.nSlides;
            //------------- Media ----------------------------------------
            if (general.debug == 1) {
                LOGGER.info("Copiant media ...");
            }
            typeMedia = "media";
            pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_PPTX.eval() + typeMedia;
            if (iteracio == 1) {
                media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/" + typeMedia + "/");
                if (media.exists()) {
                    fileUtils.copyFolder(media, new File(pathMultimedia + "/"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "/ppt/" + typeMedia + "/");
            if (media.exists()) {
                fileUtils.copyFolder(media, new File(pathMultimedia + "/" + typeMedia + +(iteracio + 1) + "/"));
            }

            //------------- embeddings ----------------------------------------
            if (general.debug == 1) {
                LOGGER.info("Copiant embeddings ...");
            }
            typeMedia = "embeddings";
            pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_PPTX.eval() + typeMedia;
            if (iteracio == 1) {
                media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "/ppt/" + typeMedia + "/");
                if (media.exists()) {
                    fileUtils.copyFolder(media, new File(pathMultimedia + "/"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "/ppt/" + typeMedia + "/");
            if (media.exists()) {
                fileUtils.copyFolder(media, new File(pathMultimedia + "/" + typeMedia + +(iteracio + 1) + "/"));
            }
            //-----------------------------------------------------------------------------------------
            if (general.debug == 1) {
                LOGGER.info("Remove and copy folder ...");
            }
            try {
                FileUtils.deleteDirectory(new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval()));
            } catch (java.io.IOException ioe) {
                LOGGER.error("Error:" + ioe.getMessage());
            }
            fileUtils.renameDirectory(tmp + this.id_task + "/" + Path.PATH_FITXER_RESULTANT.eval(), tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval());


            return 0;

        } else {
            LOGGER.error("Doesn't exist: "
                    + task.getAbsolutePath());


            return -1;
        }


    }

    /*
     * Copia un fitxer d'un lloc a l'altre, però en el viatge aplica una xsl.
     * @param directori_origen: Directori on es troben els elements que volem processar
     * @param directori_desti: On deixa els fitxers processats
     * @param stylesheet: xsl que aplica a cada element
     */
    private void copiaContingutEstructuralXSL(String directori_origen, String directori_desti, String stylesheet) {
        File fdirOrigen;
        Utilitats f = new Utilitats();
        xslTransform xsl = new xslTransform();

        fdirOrigen = new File(directori_origen);

        if (fdirOrigen.exists()) {
            //Per cada fitxer apliquem una xsl.
            File[] files = fdirOrigen.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    //No necessita cap tipus de paràmetre
                    xsl.execute(directori_origen + files[i].getName(), directori_desti + files[i].getName(), stylesheet, null);
                }
            }
        } else {
            LOGGER.warn("No existe el fichero o directorio: " + directori_origen);
        }

    }

    /*
     * Simplement copia d'una carpeta origen a un altre desti
     * @param path_origen: Directori on es troben els elements que volem processar
     * @param path_desti: On deixa els fitxers processats
     * @return 0 si no existeix carpeta i 1 si ha anat tot bé
     */
    private int copiaContingutEstructural(String path_origen, String path_desti) {
        File fd = new File(path_origen);
        Utilitats f = new Utilitats();

        if (fd.exists()) {
            f.copyFolder(
                    new File(path_origen), new File(path_desti));
        } else {
            //logger.warn("No existe el fichero o directorio: " + path_origen);
            return 0;
        }
        return 1;
    }
}
