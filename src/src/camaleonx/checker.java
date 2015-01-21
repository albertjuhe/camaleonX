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
package camaleonx;

import omaonk.config.Config;
import java.io.File;
/**
 *
 * @author Albert
 */
public class checker {

    Config conf;

    public checker() {
        this.conf = Config.getInstance("");
    }

    public void check() {
        String base = conf.eval("base");
        testDir(base);
        testDir(base + conf.eval("tasks"));
        testDir(base + conf.eval("documents"));
        testDir(base + conf.eval("results"));

        String app = conf.eval("app");
        testDir(app);
        testDir(app + conf.eval("tmp"));
        testDir(app + conf.eval("templates"));
        testDir(conf.eval("path_portades"));
        testDir(app + conf.eval("anexosDocx"));

      

    }

    private void testDir(String path) {
        File test = new File(path);
        if (!test.exists() || !test.canRead() || !test.canWrite()) {
            System.out.println("Document directory " + path + ":ERROR does not exist or is not readeable, please check the path.");
        } else {
            System.out.println("Document directory " + path + ": PASSED.");
        }
    }
}
