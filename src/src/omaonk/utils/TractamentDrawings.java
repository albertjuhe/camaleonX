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
package omaonk.utils;

import java.io.*;

/**
 *
 * @author omaonk
 */
public class TractamentDrawings {

    //Controlem que estem indexant XML.
    private static class filterFiles implements FileFilter {

        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".vml");
        }
    }

    public static class FileExtensionFilter implements FilenameFilter {

        private String ext = "*";

        public FileExtensionFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            if (name.endsWith(ext)) {
                return true;
            }
            return false;
        }
    }
/*
    public static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
*/
    public TractamentDrawings(String input1, String input2, String outputDir, String workDir) {

        filterFiles filter = new filterFiles();
        Utilitats utils = new Utilitats();

        String path_source = input1 + "\\ppt\\drawings\\"; //path del fitxer pptx 1
        File docDir_source = new File(path_source);
        if (!docDir_source.exists() || !docDir_source.canRead()) {
            //System.out.println("Document directory '" + docDir_source.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            path_source = null;
        }
        int num_source = 0;
        if (path_source != null) {
            num_source = docDir_source.listFiles(filter).length;
        }


        String path_target = input2 + "\\ppt\\drawings\\"; //path del fitxer pttx 2
        File docDir_target = new File(path_target);
        if (!docDir_target.exists() || !docDir_target.canRead()) {
            //System.out.println("Document directory '" + docDir_target.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            path_target = null;
        }


        String path_final = outputDir + "\\ppt\\drawings\\"; //path fitxer fusionat
        File docDir_final = new File(path_final);
        if (!docDir_final.exists() || !docDir_final.canRead()) {
            //System.out.println("Document directory '" + docDir_final.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }


        String path_job = workDir;

        //fitxer que creem
        Writer output = null;
        File file_output = new File(path_job + "\\relationsDrawing.xml");
        try {
            output = new BufferedWriter(new FileWriter(file_output));
            output.write("<drawings>");
            if (path_target != null) {
                System.out.println("Your file has been written");

                //Comencem a copiar els nous vml
                FileExtensionFilter filterName = new FileExtensionFilter(".vml");

                String[] files = docDir_target.list(filterName);
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File file = new File(files[i]);
                        if (!file.isDirectory()) {
                            String numero = files[i].replaceAll("[^0-9]+", "");
                            int n = Integer.parseInt(numero) + num_source;
                            System.out.print(path_target + files[i] + " , " + path_final + "vmlDrawing" + n + ".vml");
                            utils.copyFile(new File(path_target + files[i]), new File(path_final + "vmlDrawing" + n + ".vml"));
                            //copyfile(path_target + "_rels\\" + "vmlDrawing" + numero + ".vml.rels", path_final + "_rels\\" + "vmlDrawing" + n + ".vml.rels");
                            output.write("<drawing origen=\"vmlDrawing" + numero + ".vml\" rel_origen='" + numero + "' desti=\"vmlDrawing" + n + ".vml\" rel_desti='" + n + "' />");
                        }
                    }
                }
            }
            output.write("</drawings>");
            output.close();
        } catch (IOException io) {
        }


    }
}
