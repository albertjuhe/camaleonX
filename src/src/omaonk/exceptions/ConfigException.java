/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package omaonk.exceptions;

/**
 *
 * @author Preproduccio
 */
public class ConfigException  extends Exception {

    private String message;
    private String tipusError;

    public ConfigException(String tError, String msg) {
        this.tipusError = tError;
        this.message = msg;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return "File Error: " + message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the tipusError
     */
    public String getTipusError() {
        return tipusError;
    }

    /**
     * @param tipusError the tipusError to set
     */
    public void setTipusError(String tipusError) {
        this.tipusError = tipusError;
    }
}
