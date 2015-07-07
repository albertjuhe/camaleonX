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
package omaonk.transforms;

import java.io.*;
import org.apache.log4j.Logger;
import omaonk.utils.Utilitats;
import omaonk.utils.xslTransform;
import java.util.HashMap;

public class TransformDocx extends transform {
    HashMap<String,String> params = new HashMap();  
    private static Logger logger = Logger.getLogger(TransformDocx.class.getName());

    public TransformDocx(String job, int iteracio) {
        super();
        this.xslTranformacio = new xslTransform();
        this.job = job;
        this.iteracio = iteracio;
        tmp = app + conf.eval("tmp");
        xsl_path = app + conf.eval("xsl");
    }

    public void loadParams() {
    }

    public void Transform() {

        logger.info("Inici de les transformacions XSL");

        String input = tmp + job + "/file1/word/document.xml";
        String output = tmp + job + "/result/word/document.xml";
        String stylesheet = xsl_path + "mergeDocx.xsl";

        params.put("docx.merge", tmp.replace('\\', '/') + "/" + job + "/file2/word");
        params.put("docx.relations", tmp.replace('\\', '/') + "/" + job + "/file1/word");
        //int aux_iteracio = iteracio + 1;
        params.put("docx.iteracio", String.valueOf(iteracio + 1));
        params.put("docx.media", String.valueOf(iteracio));
        params.put("docx.numbering", tmp.replace('\\', '/') + "/" + job + "/file1/word/numbering.xml");
        params.put("docx.titol", "");
        xslTranformacio.execute(input, output, stylesheet, params);

        input = tmp + job + "/file1/word/_rels/document.xml.rels";
        output = tmp + job + "/result/word/_rels/document.xml.rels";
        stylesheet = xsl_path + "mergeRelacions.xsl";

        params.put("docx.merge", tmp.replace('\\', '/') + "/" + job + "/file2/word/_rels/document.xml.rels");
        params.put("docx.media", String.valueOf(iteracio));
        xslTranformacio.execute(input, output, stylesheet, params);

        input = tmp + job + "/file1/word/styles.xml";
        output = tmp + job + "/result/word/styles.xml";
        stylesheet = xsl_path + "mergeStyles.xsl";

        params.put("docx.merge", tmp.replace('\\', '/') + "/" + job + "/file2/word/styles.xml");
        params.put("docx.numbering", tmp.replace('\\', '/') + "/" + job + "/file1/word/numbering.xml");
        params.put("docx.iteracio", String.valueOf(iteracio + 1));
        xslTranformacio.execute(input, output, stylesheet, params);

        Utilitats fileUtils = new Utilitats();

        try {
            fileUtils.copyFile(new File(tmp + job + "/file2/[Content_Types].xml"), new File(tmp + job + "/file2/Content_Types.xml"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        input = tmp + job + "/file1/[Content_Types].xml";
        output = tmp + job + "/result/[Content_Types].xml";
        stylesheet = xsl_path + "index.xsl";

        params.put("docx.merge", tmp.replace('\\', '/') + "/" + job + "/file2/Content_Types.xml");
        // paramValue = "E:/Internet/Samples/tmp/MyWay/word/styles.xml";

        xslTranformacio.execute(input, output, stylesheet, params);

        File numbering1 = new File(tmp + job + "/file1/word/numbering.xml");
        File numbering2 = new File(tmp + job + "/file2\\word/numbering.xml");

        if (numbering1.exists() && numbering2.exists()) {

            input = tmp + job + "/file1/word/numbering.xml";
            output = tmp + job + "/result/word/numbering.xml";
            stylesheet = xsl_path + "mergeNumbering.xsl";

            params.put("docx.iteracio", String.valueOf(iteracio + 1));
            params.put("docx.doc2", tmp.replace('\\', '/') + job + "/file2/word/numbering.xml");

            xslTranformacio.execute(input, output, stylesheet, params);
        } else if (numbering1.exists()) {
            try {
                fileUtils.copyFile(numbering1, new File(tmp + job
                        + "/result/word/numbering.xml"));
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
            }

        } else if (numbering2.exists()) {
            try {
                fileUtils.copyFile(numbering2, new File(tmp + job
                        + "/result/word/numbering.xml"));
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
            }
        }

        logger.info("Final de les transformacions XSL");
    }
}
