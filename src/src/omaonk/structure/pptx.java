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
 */

package omaonk.structure;

/**
 *
 * @author Albert
 */
public class pptx {
    private int slideMaster = 0;
    private int handoutMaster = 0;
    private int notesMaster = 0;
    private int slideLayouts = 0;
    private int slides = 0;
    private int tags = 0;
    private int theme = 0;

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
