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

import java.io.FileInputStream;
import java.io.InputStream;

import org.docx4j.openpackaging.Base;
import org.docx4j.openpackaging.contenttype.ContentTypeManager;
import org.docx4j.openpackaging.io.Load;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.apache.log4j.Logger;

/**
 *
 * @author Albert
 */
public class wordPackage {

    private String path_contentType;
    private String path_output;
    private String nameFile;
    private static Logger logger = Logger.getLogger(wordPackage.class.getName());

    public wordPackage(String path_contentType, String path_output, String nameFile) {
        this.nameFile = nameFile;
        this.path_output = path_output;
        this.path_contentType = path_contentType;
    }

    public void SaveDocx() throws Exception {

        System.out.println("Creating package..");
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

        // Need to know how what type of part to map to
        InputStream in = new FileInputStream(path_contentType + "[Content_Types].xml");
        ContentTypeManager externalCtm = new ContentTypeManager();
        externalCtm.parseContentTypesFile(in);

        // Example of a part which become a rel of the word document
        in = new FileInputStream(path_contentType + "word/settings.xml");
        attachForeignPart(wordMLPackage, wordMLPackage.getMainDocumentPart(),
                externalCtm, "word/settings.xml", in);

        // Example of a part which become a rel of the package
        in = new FileInputStream(path_contentType + "docProps/app.xml");
        attachForeignPart(wordMLPackage, wordMLPackage,
                externalCtm, "docProps/app.xml", in);

        // Now save it
        wordMLPackage.save(new java.io.File(path_output + "/" + nameFile));

        logger.info("Docx "+path_output + "/" + nameFile+" creado correctamente.");

    }

    public void attachForeignPart(WordprocessingMLPackage wordMLPackage,
            Base attachmentPoint,
            ContentTypeManager foreignCtm,
            String resolvedPartUri, InputStream is) throws Exception {


        Part foreignPart = Load.getRawPart(is, foreignCtm, resolvedPartUri, null);
        // the null means this won't work for an AlternativeFormatInputPart
        attachmentPoint.addTargetPart(foreignPart);
        // Add content type
        ContentTypeManager packageCtm = wordMLPackage.getContentTypeManager();
        packageCtm.addOverrideContentType(foreignPart.getPartName().getURI(), foreignPart.getContentType());


    }
}
