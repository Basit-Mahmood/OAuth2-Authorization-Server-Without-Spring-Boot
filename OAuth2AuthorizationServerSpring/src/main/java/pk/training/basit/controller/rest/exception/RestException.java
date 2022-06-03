package pk.training.basit.controller.rest.exception;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

	private static final long serialVersionUID = -6537830902117302181L;

	private String errorCode;
	private String errorMessage;
	private Object[] args;
	private HttpStatus httpStatus;
	private Exception exception;
	
	public RestException(Exception exception, String errorCode, HttpStatus httpStatus) {
		super();
		this.exception = exception;
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}

	public RestException(String errorCode, String errorMessage, Object[] args, HttpStatus httpStatus) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.args = args;
		this.httpStatus = httpStatus;
	}
	
	public RestException(Exception e) {
		this(e, "error.server.internal", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public RestException(Exception e, String errorCode) {
		this(e, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public RestException(String errorCode, Object[] args, HttpStatus httpStatus) {
		this(errorCode, errorCode, args, httpStatus);
	}
	
	public RestException(String errorCode, Object[] args) {
		this(errorCode, args, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public RestException(String errorCode, HttpStatus httpStatus) {
		this(errorCode, null, httpStatus);
	}
	
	public RestException(String errorCode) {
		this(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
