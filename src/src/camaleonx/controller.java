/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camaleonx;

import java.io.File;
import omaonk.exceptions.FileErrorException;
import omaonk.merge.standaloneDocx;
import omaonk.merge.standalonePPTX;
import omaonk.xml.profile;

/**
 *
 * @author Albert
 */
public class controller {

    private profile tascaXML = null;
    boolean portada = true;
    private File xmlfilename = null;
    private String xml_file_name = null;

    public controller(File xmlfile) {
        this.xmlfilename = xmlfile;
        this.xml_file_name = xmlfilename.getName();
        int pos = xml_file_name.lastIndexOf(".");
        if (pos > 0) {
            xml_file_name = xml_file_name.substring(0, pos);
        }
    }

    /*
     *
     * Load XML file with the merge description task
     */
    public void execute() throws FileErrorException {
        //Carrega el xml relacionat amb la tasca

        this.tascaXML = new profile(this.xmlfilename);
        this.tascaXML.LoadXMLTasca(); //Carrega les dades del xml.
        this.tascaXML.ID_TASK = this.xml_file_name;
        String taskType = (String) this.tascaXML.getHmMetas().get("type");
        if (taskType.equalsIgnoreCase("docx")) {
            standaloneDocx merger = new standaloneDocx(this.tascaXML);
            merger.execute();
        } else if (taskType.equalsIgnoreCase("pptx")) {
            standalonePPTX merger = new standalonePPTX(this.tascaXML);
            merger.execute();
        }
    }
}
