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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import java.nio.channels.FileChannel;

public class Utilitats {

    static final int BUFF_SIZE = 100000;
    static final byte[] buffer = new byte[BUFF_SIZE];

    public String getFileFormat(String fileName) {

        int i;
        String result;

        i = fileName.lastIndexOf(".");

        result = fileName.substring(i);

        return result;
    }

    public String removeFileFormat(String fileName) {

        int i;
        String result;

        i = fileName.lastIndexOf(".");

        result = fileName.substring(0, i);

        return result;
    }

    public String removeLastDigits(String fileName) {

        int i;
        int length = fileName.length();
        int numDigits = 0;

        i = length - 1;
        char ch = fileName.charAt(i);
        while (i > 0 && Character.isDigit(ch)) {
            i--;
            ch = fileName.charAt(i);
            numDigits++;
        }

        length = length - numDigits;

        return fileName.substring(0, length);
    }

    public String getLastDigits(String fileName) {
        String tempFileName;

        int i = fileName.lastIndexOf(".");
        tempFileName = fileName.substring(0, i);

        int length = tempFileName.length();
        int numDigits = 0;

        i = length - 1;
        char ch = tempFileName.charAt(i);
        while (i > 0 && Character.isDigit(ch)) {
            i--;
            ch = tempFileName.charAt(i);
            numDigits++;
        }
        String numFitxer = tempFileName.substring(length - numDigits, length);
        return numFitxer;
    }

    public void saveAsOffice(String file, String path) {
        try {
            pack packDocument = new pack();
            packDocument.zip(new File(path), new File(file));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Desenzippa un fitxer .zip en un directori
     *
     * @param zip
     * @param extractTo
     */
    public static void unzip(File zip, File extractTo) {
        try {
            ZipFile archive = new ZipFile(zip);
            Enumeration<?> e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File file = new File(extractTo, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else if (!entry.isDirectory()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    InputStream in = archive.getInputStream(entry);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                    byte[] buffer = new byte[8192];
                    int read;
                    while (-1 != (read = in.read(buffer))) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    out.close();
                }
            }
            archive.close();
        } catch (IOException e) {
            System.err.println("Error desenzippant el fitxer: " + zip.getName() + " al directori: " + extractTo + ". Error: " + e.getMessage());
        }
    }

    // public void mkDirAll(File dir){
    public void mkDirAll(String base, String strFile) {

        String[] strFiles = strFile.replace('\\', '/').split("/");
        String acum = null;

        if (base.endsWith("\\")) {

            acum = base.substring(0, base.length() - 1);

        } else {

            acum = base;
        }

        for (String i : strFiles) {

            acum = acum + "\\" + i;
            File file = new File(acum);

            if (!file.exists()) {

                this.mkDir(file);

            }
            // System.out.println("es un directori: " + file.getPath());

        }

    }

    public void mkDir(File dir) {

        //boolean status;
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        //status = dir.mkdir();

    }

    public void deleteDirectory(File file) {

        if (file.isDirectory()) {

            String[] oChildren = file.list();
            for (int i = 0; i < oChildren.length; i++) {
                deleteDirectory(new File(file.getPath() + "\\" + oChildren[i]));
            }
        }

        file.delete();
    }

    public void copyFolder(File srcFolder, File destFolder) {
        if (!srcFolder.getName().equals(".svn")) { // evitem copiar fitxers de
            // subversion
            if (srcFolder.isDirectory()) {
                if (!destFolder.exists()) {
                    destFolder.mkdir();
                    try {
                        FileUtils.forceMkdir(destFolder);
                    } catch (IOException ioe) {
                        System.err.println(ioe.getMessage());
                    }
                }

                String[] oChildren = srcFolder.list();
                for (int i = 0; i < oChildren.length; i++) {
                    copyFolder(new File(srcFolder, oChildren[i]), new File(
                            destFolder, oChildren[i]));
                }
            } else {
                try {
                    if (destFolder.isDirectory()) {
                        copyFile(srcFolder, new File(destFolder, srcFolder.getName()));
                    } else {
                        copyFile(srcFolder, destFolder);
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
    }

    public void replace(File srcFile, String patternStr, String replacement) {
        try {
            String fileContent;
            String output = "";

            Pattern pattern = Pattern.compile(patternStr);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(srcFile), "UTF8"));

            // almacenamos el contenido del input sin los saltos de linea
            while ((fileContent = in.readLine()) != null) {
                output = output.concat(fileContent);
            }

            fileContent = output;

            // reemplazamos la variable en cuestión por su contenido
            // correspondiente
            Matcher matcher = pattern.matcher(fileContent);
            output = matcher.replaceAll(replacement);

            in.close();

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(srcFile.getAbsolutePath()), "UTF8"));

            out.write(output);
            out.close();

        } catch (FileNotFoundException e) {

            System.err.println(e);

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void renameDirectory(String fromDir, String toDir) {

        File from = new File(fromDir);

        if (!from.exists() || !from.isDirectory()) {

            System.out.println("Directory does not exist: " + fromDir);
            return;
        }

        File to = new File(toDir);

        from.renameTo(to);


    }

    public void copyFile(File from, File to) throws IOException {
        /*
         * v1 try { InputStream oInStream = new FileInputStream(srcFile);
         * OutputStream oOutStream = new FileOutputStream(destFile);
         *
         * // Transfer bytes from in to out byte[] oBytes = new byte[1024]; int
         * nLength; BufferedInputStream oBuffInputStream = new
         * BufferedInputStream( oInStream); while ((nLength =
         * oBuffInputStream.read(oBytes)) > 0) { oOutStream.write(oBytes, 0,
         * nLength); } oInStream.close(); oOutStream.close();
         * oBuffInputStream.close(); } catch (FileNotFoundException e) {
         *
         * System.err.println(e);
         *
         * } catch (IOException e) { System.err.println(e); }
         *
         */
        /*
         * InputStream in = null; OutputStream out = null; try { in = new
         * FileInputStream(from); out = new FileOutputStream(to); while (true) {
         * synchronized (buffer) { int amountRead = in.read(buffer); if
         * (amountRead == -1) { break; } out.write(buffer, 0, amountRead); } } }
         * catch (FileNotFoundException e) {
         *
         * System.err.println(e);
         *
         * } catch (IOException e) { System.err.println(e); } finally { if (in
         * != null) { in.close(); } if (out != null) { out.close(); } }
         *
         */


        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            srcChannel = new FileInputStream(from).getChannel();
            destChannel = new FileOutputStream(to).getChannel();
            long size = srcChannel.size();
            destChannel.transferFrom(srcChannel, 0, size);
        } finally {
            if (srcChannel != null) {
                srcChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }

        //FileUtils.copyFile(from, to);


    }

    public int copyFiles(File srcFile, File destFile, String newFileName, int k) {
        Utilitats f = new Utilitats();

        if (srcFile.exists()) {

            File[] files = srcFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {

                    newFileName = newFileName + String.valueOf(k) + ".xml";
                    File newFile = new File(destFile.getPath() + newFileName);
                    try {
                        f.copyFile(files[i], newFile);
                    } catch (IOException ioe) {
                        System.err.println(ioe.getMessage());
                    }
                }

                k++;
            }
        }

        return k;
    }

    public static String sanitizeFilename(String name) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = name;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }

        return output.replaceAll("[^A-Za-z0-9 ]", "_");
    }
}
