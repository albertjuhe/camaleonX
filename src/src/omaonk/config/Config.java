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
package omaonk.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author omaonk
 */
public class Config {

    private static Config INSTANCE = null;
    private Properties properties = null;

    private Config(String path) {

        try {
            this.properties = new Properties();
            FileInputStream inputStream = new FileInputStream(path);
            this.properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized static void createInstance(String path) {
        if (INSTANCE == null) {
            INSTANCE = new Config(path);
        }
    }

    public static Config getInstance(String path) {
        if (INSTANCE == null) {
            createInstance(path);
        }
        return INSTANCE;
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            return null;
        }
        return INSTANCE;
    }

    public String eval(String property) {

        String result = "";

        result = properties.getProperty(property);
        return result;

    }
}
