package omaonk.exceptions;

public class FormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4088225408322153578L;
	private String message;
	private String tipusError;
	
	public FormatException(String tError, String msg){
		this.tipusError = tError;
		this.message = msg;
	}
	
	public String getMessage() {
		return "Format Error: " + message + "\n" + super.getMessage();
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTipusError() {
		return tipusError;
	}

	public void setTipusError(String tipusError) {
		this.tipusError = tipusError;
	}
}
