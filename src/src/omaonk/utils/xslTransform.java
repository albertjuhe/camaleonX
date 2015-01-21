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

import net.sf.saxon.TransformerFactoryImpl;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import omaonk.fonts.*;
import omaonk.config.Config;
import omaonk.params.Path;
import omaonk.utils.Utilitats;

/**
 *
 * @author Albert Aquesta classe s'encarrega de fer les transformacions
 */
public class xslTransform {

    private static Logger logger = Logger.getLogger(xslTransform.class.getName());

    public xslTransform() {
    }

    public void execute(String input, String output, String stylesheetStr, HashMap<String, String> params) {
        try {
            File stylesheet = new File(stylesheetStr);

            System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");

            Source source = new StreamSource(new FileInputStream(input));

            StreamResult result = new StreamResult(new FileOutputStream(output));

            TransformerFactory factory = new TransformerFactoryImpl();
            Templates templates = factory.newTemplates(new StreamSource(stylesheet));
            Transformer transformer = templates.newTransformer();
            
            if (params != null) {
                HashMap hmParams = params;
                Iterator itMetas = hmParams.entrySet().iterator();
                while (itMetas.hasNext()) {
                    Map.Entry e = (Map.Entry) itMetas.next();
                    String key = (String) e.getKey();
                    String value = (String) e.getValue();
                    transformer.setParameter(key, value);
                }
            }
            /*
             * if (params != null) { for (int i = 0; i < params.size() - 1; i =
             * i + 2) {
             *
             * String key = params.get(i); String value = params.get(i + 1);
             *
             * transformer.setParameter(key, value); } }
             */
            transformer.transform(source, result);

            result.getOutputStream().close();

        } catch (TransformerException e) {

            logger.error(e);

        } catch (FileNotFoundException e) {

            logger.error(e);

        } catch (IOException e) {

            logger.error(e);

        }
    }
}
