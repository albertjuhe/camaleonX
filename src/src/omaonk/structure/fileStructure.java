/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.structure;

/**
 *
 * @author Preproduccio
 */
public class fileStructure {

    private String filename;
    private String type;
    private String Id;
    private String nom;

    public fileStructure(String fn, String t, String Id,String nom) {
        this.filename = fn;
        this.type = t;
        this.Id = Id;
        this.nom = nom;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return type;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.type = fileType;
    }

    /**
     * @return the Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
