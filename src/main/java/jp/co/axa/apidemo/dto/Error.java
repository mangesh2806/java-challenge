package jp.co.axa.apidemo.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error extends Exception {

	public Error(int status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
	private int status;
	private String message;
	
}
