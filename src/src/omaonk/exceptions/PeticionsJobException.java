package omaonk.exceptions;

/**
 * Classe que representa una excepció en el treball que consumeix peticions (i genera els formats)
 */
public class PeticionsJobException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4570702546933354156L;
	private String message;
	
	public PeticionsJobException(String msg){
		this.message = msg;
	}

	public String getMessage() {
		return "PeticionsJob Error: " + message + "\n" + super.getMessage();
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
