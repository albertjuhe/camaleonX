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
package omaonk.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;

/**
 * <p>Class to compress and uncompress files and folders. For now,
 * it just handles the ZIP compressed format, it can be extended to handle
 * any compression format supported by Apache Commons Compress.</p>
 *
*/
public class pack {

    /**
     * Zip the given file or folder (and its contents, recursively)
     *
     * @param fileOrFolder the File object representing the file or folder to
     * zip. If it's a folder, its contents are compressed, recursively
     * @param zipped the File where to store the zipped output
     * @throws IOException if there's I/O errors attempting to compress the
     * given file or folder
     */
    public void zip(File fileOrFolder, File zipped) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipped));
        ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(os);
        /**
         * From http://commons.apache.org/compress/zip.html
         *
         * "For maximum interop it is probably best to set the encoding to
         *  UTF-8, enable the language encoding flag and create Unicode extra
         *  fields when writing ZIPs. Such archives should be extracted
         *  correctly by java.util.zip, 7Zip, WinZIP, PKWARE tools and most
         *  likely InfoZIP tools. They will be unusable with Windows'
         *  "compressed folders" feature and bigger than archives without the
         *  Unicode extra fields, though."
         */
        zaos.setEncoding("UTF-8"); // NOI18N
        zaos.setMethod(ZipArchiveOutputStream.DEFLATED);
        zaos.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
        if (fileOrFolder.isDirectory()) { // Archive folder
            zipFolder(fileOrFolder, zaos, ""); // NOI18N
        } else { // Pack file
            zaos.putArchiveEntry(new ZipArchiveEntry(fileOrFolder.getName()));
            IOUtils.copy(new AutoCloseInputStream(new FileInputStream(fileOrFolder)), zaos);
            zaos.closeArchiveEntry();
        }
        zaos.finish();
        zaos.flush();
        os.close();
        zaos.close();
    }

    /**
     * Zip a folder, recursively
     *
     * @param folder the File representing the folder to zip
     * @param zaos a previously opened ZipArchiveOutputStream where the zip
     * entries are stored
     * @param path the bsae path to prepend to each entry. It starts being the
     * empty string
     * @throws IOException if there's I/O errors attempting to zip the folder
     */
    private void zipFolder(File folder, ZipArchiveOutputStream zaos, String path) throws IOException {
        for (String file : folder.list()) {
            File f = new File(folder, file);
            if (f.isDirectory()) { // Recurse
                zipFolder(f, zaos, path + f.getName() + "/"); // NOI18N
                continue; // We don't know if there's more files or folders to zip after this folder
            } else { // Pack file
                zaos.putArchiveEntry(new ZipArchiveEntry(path + f.getName()));
                IOUtils.copy(new AutoCloseInputStream(new FileInputStream(f)), zaos);
                zaos.closeArchiveEntry();
            }
        }
    }

    /**
     * Unzip (uncompress) a file, storing its contents into a folder
     *
     * @param zipped the zipped file to uncompress
     * @param toFolder the folder to store the uncompressed files. It is
     * created if it doesn't exist, together with all its parents
     * @throws IOException if there's I/O errors unzipping the file
     */
    public void unzip(File zipped, File toFolder) throws IOException {
        if (!toFolder.exists()) {
            FileUtils.forceMkdir(toFolder);
        } else {
            if (!toFolder.isDirectory()) {
               System.out.print("Error desenzipant." + toFolder.getAbsolutePath());
            }
        }
        ZipFile zfile = new ZipFile(zipped);
        for (Enumeration e = zfile.getEntries(); e.hasMoreElements();) {
            ZipArchiveEntry zaentry = (ZipArchiveEntry) e.nextElement();
            File file = new File(toFolder, zaentry.getName());
            if (zaentry.isDirectory() && !file.exists()) {
                FileUtils.forceMkdir(file);
            } else {
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file));
                    IOUtils.copy(new AutoCloseInputStream(zfile.getInputStream(zaentry)), os);
                } finally {
                    IOUtils.closeQuietly(os);
                }
            }
        }
        zfile.close();
    }

    public File extract(ZipInputStream zin, ZipEntry zentry, File toFolder) throws IOException {
        File file = new File(toFolder, zentry.getName());
        if (zentry.isDirectory() && !file.exists()) {
            FileUtils.forceMkdir(file);
        } else {
            OutputStream os = null;
            try {
                os = FileUtils.openOutputStream(file);
                IOUtils.copy(zin, os);
            } finally {
                IOUtils.closeQuietly(os);
            }
        }
        return file;
    }
}
