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
package camaleonx;

import omaonk.config.Config;
import java.io.File;
import omaonk.exceptions.FileErrorException;

/**
 *
 * @author omaonk CamaleonX
 */
public class Main {

    /**
     * @param args the command line arguments
     *
     */
    public static void main(String[] args) throws Exception {
        int i = 0;
        String arg;
        String usage = "Usage: java [-options] -jar camaleonx.jar [file] [args...] \n"
                + "(to execute a camaleonx jar file)\n"
                + "where options include:\n"
                + "-Xms256m          extended memory\n"
                + "-Xmx1024m         expanded memory\n\n"
                + "where file are the task file to execute\n"
                + " ex: java -jar camaleonx.jar PID_xxxxx.xml \n\n"
                + "where args include:\n"
                + "-version      print product version and exit\n"
                + "-check        check the daemon function mode properties\n"
                + "-D<name>=<value>\n"
                + "-Dlog4j.configuration=<value>  Path of config log file, ex: file:/C:/camaleonx/logs/camaleonx.log4j.xml\n"
                + "-Dlog4j.debug=<value> Execute in debug mode. ex:true \n"
                + "-U <value>  Configuration file, ex:C:/camaleonx/camaleonx.properties\n"
                + "-Dcamaleonx.local.home=<value> Logs file. ex:c:\\camaleonx\\logs \n";

        if (args.length > i && args[i].startsWith("-")) {
            arg = args[i++];

            if (arg.equals("-version")) {
                System.out.println("CamaleonX " + omaonk.params.general.version + "v.");
            }
            if (arg.equals("-checker")) {
                System.out.println("Cheking parameters ");
                checker chck = new checker();
                chck.check();
            }
            if (arg.equals("-U")) {
                String Configfilename = args[i++];
                File configFile = new File(Configfilename);
                if (!configFile.exists() || !configFile.canRead() || !configFile.canWrite()) {
                    System.out.println("Error: " + configFile + " does not exist or is not readeable, please check the path.");
                } else {
                    System.out.println("Reading config file: " + Configfilename);
                    Config.getInstance(Configfilename);
                }
            } else {
                System.out.println("Reading default config file: camaleonx.properties");
                Config.getInstance("camaleonx.properties");
            }

        }

        System.out.println("CamaleonX  " + omaonk.params.general.version + "v. Command line execution.");
        if (args.length > i) {
            String Taskfilename = args[i++];
            File tskFile = new File(Taskfilename);
            if (!tskFile.exists() || !tskFile.canRead() || !tskFile.canWrite()) {
                System.out.println("Error: " + Taskfilename + " does not exist or is not readeable, please check the path.");
            } else {
                System.out.println("Processing " + Taskfilename + "...");
                controller cont = new controller(tskFile);
                try {
                    cont.execute();
                } catch (FileErrorException e) {
                    System.err.println(e.getMessage());
                }
            }
        } else {
            System.out.println(usage);
        }


    }
}
