/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.structure;

/**
 *
 * @author Preproduccio
 */
public class slideStructure {

    private String rId = "";
    private String slideFile = "";

    public slideStructure(String rId, String slideFile) {
        this.rId = rId;
        this.slideFile = slideFile;
    }

    /**
     * @return the rId
     */
    public String getrId() {
        return rId;
    }

    /**
     * @param rId the rId to set
     */
    public void setrId(String rId) {
        this.rId = rId;
    }

    /**
     * @return the slideFile
     */
    public String getSlideFile() {
        return slideFile;
    }

    /**
     * @param slideFile the slideFile to set
     */
    public void setSlideFile(String slideFile) {
        this.slideFile = slideFile;
    }
}
