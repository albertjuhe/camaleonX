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
import omaonk.config.Config;
import omaonk.utils.*;
import omaonk.params.*;

public abstract class Merge {

    private static Logger logger = Logger.getLogger(Merge.class.getName());
    protected File file1 = null;
    protected File file2 = null;
    protected String id_task = null;
    protected String type = null;
    protected String fileNameResult = null;
    protected String tipo_document = "";
    String[] elements_multimedia = {"media", "diagrams", "embeddings"};
    public boolean annexes = false;
    public static String bbddServerType = "";
    protected Config conf = Config.getInstance("");
    protected String app = conf.eval("app");
    protected String tmp;
    protected String skeleton;
    protected String task_state_error = conf.eval("task_state_error");
    protected File task = null; //Temporal folder, where we merge the files
    protected File temp = null; //Main temporal folder

    // Fa la fusio de dos documents
    abstract protected int merging(int iteracio);

    //The first file to merge is especial.
    protected void frontTreatment(File file) {
        try {
            if (!temp.exists()) {
                FileUtils.forceMkdir(temp);
            }
        } catch (IOException e) {
            logger.error("Error creant directory:" + tmp + this.id_task + "\\");
        }

        Utilitats.unzip(file2, new File(tmp + id_task + Path.PATH_FITXER_ORIGEN.eval()));
    }


    /*
     * Executa la fusio entre document.
     *
     * @param iteracio. NÚmero de fusio que estem fent.
     */
    public int run(int iteracio) {
        int result = 0;

       // logger.info("Inici del " + this.tipo_document + " de dos documents");
        if (file1.exists()) {
            if (file2.exists()) {
                result = this.merging(iteracio);
            } else {
                if (iteracio == 0) {
                    this.frontTreatment(file1);
                } else {
                    logger.error("Fitxer: " + file2.getAbsolutePath()
                            + " no existeix");
                }
            }
        } else {
            if (file2.exists()) {
                this.frontTreatment(file2);
            } else {
                logger.error("Es vol fer un " + this.tipo_document
                        + " de dos fitxers que no existeixen."
                        + file1.getAbsolutePath() + ","
                        + file2.getAbsolutePath());
            }
        }
        //logger.info("Iteracio " + iteracio + " " + this.tipo_document+ " fusionat.");
        return result;
    }

    /*
     * Crea l'estructura de directoris per fer la fusio de documents
     *
     * |--tmp
     *    |-- id_task
     *       |-- file1 (origen)
     *       |-- file2 (merge)
     *       |-- result (resultant)
     *       |-- mediaObjects (media files)
     *
     * result = origen + merge
     * 
     * temp = tmp
     * task = tmp/id_task
     */
    protected void createStructure(String path) {
        //This is the main temporal folder

        try {
            if (!temp.exists()) {
                FileUtils.forceMkdir(temp);
            }
        } catch (IOException e) {
            logger.error("Error creant directory:" + tmp + this.id_task + "\\");
        }

        try {
            if (!task.exists()) {
                FileUtils.forceMkdir(task);
            }
        } catch (IOException e) {
            logger.error("Error creant directory:" + tmp + this.id_task + "\\");
        }

        //Directory temporal per guardar elements multimedia
        this.creaMultimediaFolder(tmp + this.id_task + path, "media");
        this.creaMultimediaFolder(tmp + this.id_task + path, "diagrams");
        this.creaMultimediaFolder(tmp + this.id_task + path, "embeddings");

        //Netaja de directoris temporals resultants d'aplicar XSL
        //--this.netejaDirectory(tmp + this.id_task + Path.PATH_FITXER_ORIGEN.eval());
        this.netejaDirectory(tmp + this.id_task + Path.PATH_FITXER_MERGE.eval());
        this.netejaDirectory(tmp + this.id_task + Path.PATH_FITXER_RESULTANT.eval());
    }

  

    /*
     * Neteja un directori.
     *
     * @param dirTmp: Directori de tasca
     * @param subDirTmp: Subdirectori a netejar.
     * @return 0 error, 1 ok
     */
    protected int netejaDirectory(String dirTmp) {
        try {
            // Netejem les carpetes
            File dir = new File(dirTmp);
            if (dir.exists()) {
                FileUtils.forceDelete(dir);
                FileUtils.forceMkdir(dir);
            } else {
                FileUtils.forceMkdir(dir);
            }
            return 1;
        } catch (IOException e) {
            logger.error("Error creant directory:" + dirTmp);
            return 0;
        }
    }

    protected void creaMultimediaFolder(String pathTasca, String tipo) {
        /*
         * Creem el directori multimedia per no anar arrossegant media en
         * els zips.
         */
        String pathMultimedia = pathTasca + tipo;
        File mediaObjectFolder = new File(pathMultimedia);
        try {
            if (!mediaObjectFolder.exists()) {
                FileUtils.forceMkdir(mediaObjectFolder);
            } else {
            }
        } catch (IOException e) {
            logger.error("Error creant directory:" + pathMultimedia);
        }
    }
}
