/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omaonk.structure;

import java.io.*;
/**
 *
 * @author Albert
 */
public class PptxPackage {
    private int slideMaster = 0;
    private int handoutMaster = 0;
    private int notesMaster = 0;
    private int slideLayouts = 0;
    private int slides = 0;
    private int tags = 0;
    private int theme = 0;
    private File fpptx;

    
    void PptxPackage(File pptxFile) {
        this.fpptx = pptxFile;
    }
    /**
     * @return the slideMaster
     */
    public int getSlideMaster() {
        return slideMaster;
    }

    /**
     * @param slideMaster the slideMaster to set
     */
    public void setSlideMaster(int slideMaster) {
        this.slideMaster = slideMaster;
    }

    /**
     * @return the handoutMaster
     */
    public int getHandoutMaster() {
        return handoutMaster;
    }

    /**
     * @param handoutMaster the handoutMaster to set
     */
    public void setHandoutMaster(int handoutMaster) {
        this.handoutMaster = handoutMaster;
    }

    /**
     * @return the notesMaster
     */
    public int getNotesMaster() {
        return notesMaster;
    }

    /**
     * @param notesMaster the notesMaster to set
     */
    public void setNotesMaster(int notesMaster) {
        this.notesMaster = notesMaster;
    }

    /**
     * @return the slideLayouts
     */
    public int getSlideLayouts() {
        return slideLayouts;
    }

    /**
     * @param slideLayouts the slideLayouts to set
     */
    public void setSlideLayouts(int slideLayouts) {
        this.slideLayouts = slideLayouts;
    }

    /**
     * @return the slides
     */
    public int getSlides() {
        return slides;
    }

    /**
     * @param slides the slides to set
     */
    public void setSlides(int slides) {
        this.slides = slides;
    }

    /**
     * @return the tags
     */
    public int getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(int tags) {
        this.tags = tags;
    }

    /**
     * @return the theme
     */
    public int getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(int theme) {
        this.theme = theme;
    }

}
