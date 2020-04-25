package io.jaziu.springauthorizationserver.message.response;




public class ResponseMessage {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseMessage(String message) {
		this.message = message;
	}
}
