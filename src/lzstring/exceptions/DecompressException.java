package lzstring.exceptions;

public class DecompressException extends Exception {

	private static final long serialVersionUID = 207894408656310263L;

	public DecompressException(String message, Exception e){
		super(message, e);
	}

	public DecompressException(String message){
		super(message);
	}
}
