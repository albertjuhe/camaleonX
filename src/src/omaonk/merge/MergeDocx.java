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
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import omaonk.params.*;
import omaonk.transforms.TransformDocx;
import omaonk.utils.*;

/**
 *
 * @author omaonk
 */
public class MergeDocx extends Merge {

    private static Logger logger = Logger.getLogger(MergeDocx.class.getName());
    private String[] tipologia = {"header", "footer", "stylesWithEffects"};

    public MergeDocx(File file1, File file2, String id_task, String type,
            String fileNameResult) {
        this.file1 = file1;
        this.file2 = file2;
        this.id_task = id_task;
        this.type = type;
        this.fileNameResult = fileNameResult;
        this.tipo_document = DocumentType.DOCX.eval();
        this.skeleton = app + conf.eval("skeleton");
        this.tmp = app + conf.eval("tmp");
        this.temp = new File(tmp);
        this.task = new File(tmp + this.id_task + "\\");

    }

    protected int merging(int iteracio) {
        Utilitats f = new Utilitats();

        //Create temporal media files folder and clean temporal folders
        this.createStructure(Path.PATH_FITXER_TMP_DOCX.eval());

        File skeletonFile = new File(skeleton);

        //El unzip de la classe pack dona problemes de vegades, utilitzarem el que hi havia avans.
        if (skeletonFile.exists()) {
            f.copyFolder(new File(skeleton), task);
            f.unzip(file2, new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval()));

            TransformDocx mergeXSLDOCX = new TransformDocx(this.id_task, iteracio);
            mergeXSLDOCX.Transform();

            //------------- Diagrams ----------------------------------------
            String typeMedia = "diagrams";
            String pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_DOCX.eval() + typeMedia;
            File media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "\\word\\" + typeMedia + "\\");
            if (iteracio == 1) {
                if (media.exists()) {
                    f.copyFolder(media, new File(pathMultimedia + "\\"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "\\word\\" + typeMedia + "\\");
            if (media.exists()) {
                f.copyFolder(media, new File(pathMultimedia + "\\" + typeMedia + +(iteracio + 1) + "\\"));
            }
            //------------- Media ----------------------------------------
            typeMedia = "media";
            pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_DOCX.eval() + typeMedia;
            if (iteracio == 1) {
                media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "\\word\\" + typeMedia + "\\");
                if (media.exists()) {
                    //f.copyFolder(media, new File(tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval() + "\\ppt\\media\\"));
                    f.copyFolder(media, new File(pathMultimedia + "\\"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "\\word\\" + typeMedia + "\\");
            if (media.exists()) {
                f.copyFolder(media, new File(pathMultimedia + "\\" + typeMedia + +(iteracio + 1) + "\\"));
            }

            //------------- embeddings ----------------------------------------

            typeMedia = "embeddings";
            pathMultimedia = tmp + this.id_task + Path.PATH_FITXER_TMP_DOCX.eval() + typeMedia;
            if (iteracio == 1) {
                media = new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "\\word\\" + typeMedia + "\\");
                if (media.exists()) {
                    f.copyFolder(media, new File(pathMultimedia + "\\"));
                }
            }

            media = new File(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval() + "\\word\\" + typeMedia + "\\");
            if (media.exists()) {
                f.copyFolder(media, new File(pathMultimedia + "\\" + typeMedia + +(iteracio + 1) + "\\"));
            }

            //Fem esquelet

            // TO DO: el fitxer settings esta al skeleton. Llavors estaria
            // millor copiar-ho del primer document cap al resultat
            // Al skeleton no hauria d'haver-hi cap fitxer
            File settings = new File(tmp + this.id_task
                    + "\\file1\\word\\settings.xml");
            if (settings.exists()) {
                try {
                    f.copyFile(settings, new File(tmp + this.id_task
                            + "\\result\\word\\settings.xml"));
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }

            //Modificar per Header i footer
            //Tant el header com el footer poden tenir elements relacionals al _rels
            copyHeaderFooters(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "word\\", tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval() + "word\\", tipologia);
            copyHeaderFooters(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval() + "word\\_rels\\", tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval() + "word\\_rels\\", tipologia);

            try {
                FileUtils.deleteDirectory(new File(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval()));
            } catch (java.io.IOException ioe) {
                logger.error("Error:" + ioe.getMessage());
            }
            f.renameDirectory(tmp + this.id_task + "\\" + Path.PATH_FITXER_RESULTANT.eval(), tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval());


            return 0;
        } else {

            logger.error("No existe el directorio: "
                    + skeletonFile.getAbsolutePath());

            logger.error("Update el estado de la tarea " + this.id_task
                    + " de 'En proceso' a 'Fallo'...");


            return -1;
        }
    }

    /*
     * Copia tots fitxers que estan al directori origen directoryXML al
     * directori directoryXMLDesti que continguin una cadena de les que hi ha a
     * tipologia. @param directoryXML: Directori origen @param
     * directoryXMLDesti: Direcori desti @param tipologia: conjunt de strings
     * que marquen quins fitxers copiar.
     */
    private void copyHeaderFooters(String directoryXML, String directoryXMLDesti, String[] tipologia) {
        File directoryXml = new File(directoryXML);
        Utilitats utils = new Utilitats();

        if (directoryXml.exists()) {
            //Per cada fitxer copiem els de la tipoligia que es marca.
            File[] files = directoryXml.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    for (int j = 0; j < tipologia.length; j++) {
                        if (files[i].getName().contains(tipologia[j])) {
                            try {
                                utils.copyFile(new File(directoryXML + files[i].getName()), new File(directoryXMLDesti + files[i].getName()));
                            } catch (IOException io) {
                                logger.error(io.getMessage());
                            }
                        }
                    }
                }
            }
        } else {
            logger.warn("No existe el fichero o directorio: " + directoryXML);
        }


    }
}
