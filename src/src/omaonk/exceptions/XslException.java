package omaonk.exceptions;

public class XslException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291059673021237943L;
	private String message;
	
	public XslException(String msg){
		this.message = msg;
	}

	public String getMessage() {
		return "XSL Error: " + message + "\n Error: " + super.getMessage();
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
