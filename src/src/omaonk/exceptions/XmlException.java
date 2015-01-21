package omaonk.exceptions;

public class XmlException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291059673021237943L;
	private String message;
	
	public XmlException(String msg){
		this.message = msg;
	}

	public String getMessage() {
		return "XML Error: " + message + "\n Error: " + super.getMessage();
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
