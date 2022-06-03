package pk.training.basit.controller.rest.advice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pk.training.basit.controller.rest.exception.RestException;

@RestControllerAdvice
public class RestControllerExceptionAdvice {

	private static final Logger LOGGER = LogManager.getLogger(RestControllerExceptionAdvice.class);

	private final MessageSource messageSource;
	
	public RestControllerExceptionAdvice(MessageSource messageSource) {
		Assert.notNull(messageSource, "messageSource must not be null");
		this.messageSource = messageSource;
	}

	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		
		ErrorResponse errors = new ErrorResponse();
		ErrorItem error = new ErrorItem();
		String errorCode = "error.hhtp.request.method.not.supported";
		String errorMessage = e.getMessage();
		String[] supportedMethods = e.getSupportedMethods();
		
		if (supportedMethods != null && supportedMethods.length > 0) {
			errorMessage = errorMessage + ". Supported methods are " + supportedMethods;
		}
		
		error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		errors.addError(error);

		LOGGER.error("HttpRequestMethodNotSupportedException occured. ", e);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
		ErrorResponse errors = new ErrorResponse();
		for (ConstraintViolation violation : e.getConstraintViolations()) {
			ErrorItem error = new ErrorItem();
			String errorMessage = violation.getMessage();
			error.setStatus(HttpStatus.BAD_REQUEST.value());
			error.setCode(violation.getMessageTemplate());
			error.setMessage(errorMessage);
			errors.addError(error);
		}

		LOGGER.error("ConstraintViolationException occured: ", e);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
	    
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
	    List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
	 
	    ErrorResponse errors = new ErrorResponse();
	    
	    for (FieldError fieldError : fieldErrors) {
	    	ErrorItem error = new ErrorItem();
	    	error.setStatus(HttpStatus.BAD_REQUEST.value());
	    	error.setCode(fieldError.getField());
	    	error.setMessage(fieldError.getDefaultMessage());
	        errors.addError(error);
	    }
	    
	    for (ObjectError objectError : globalErrors) {
	    	ErrorItem error = new ErrorItem();
	    	error.setStatus(HttpStatus.BAD_REQUEST.value());
	    	error.setCode(objectError.getObjectName());
	    	error.setMessage(objectError.getDefaultMessage());
	    	errors.addError(error);
	
	    }
	    
	    LOGGER.error("MethodArgumentNotValidException occured: ", ex);
	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RestException.class)
	public ResponseEntity<ErrorResponse> handleRestException(RestException ex, Locale locale) {
		ErrorResponse errors = new ErrorResponse();
		ErrorItem error = new ErrorItem();
		String errorCode = ex.getErrorCode();
		String errorMessage = ex.getErrorMessage();
		Object[] args = ex.getArgs();
		HttpStatus httpStatus = ex.getHttpStatus();
		Exception exception = ex.getException();
		
		String i18nErrorMessage;
		
		if (exception != null) {
			i18nErrorMessage = exception.getLocalizedMessage();
		} else {
			try {
				i18nErrorMessage = messageSource.getMessage(errorMessage, args, locale);
			} catch (NoSuchMessageException e) {
				LOGGER.error("NoSuchMessageException occured: ", e.getMessage());
				i18nErrorMessage = errorCode;
			}
		}
		
		if (httpStatus == null) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		error.setCode(errorCode);
		error.setMessage(i18nErrorMessage);
		error.setStatus(httpStatus.value());
		errors.addError(error);

		if (exception != null) {
			LOGGER.error("RestException occured: ", exception);
		} else {
			LOGGER.error("RestException occured: ", ex);
		}
		
		return new ResponseEntity<>(errors, httpStatus);
	}
	
	@ExceptionHandler(JpaSystemException.class)
	public ResponseEntity<ErrorResponse> handleJpasSystemException(JpaSystemException e) {
		ErrorResponse errors = new ErrorResponse();
		ErrorItem error = new ErrorItem();
		String errorCode = "error.database";
		String errorMessage = e.getMessage();
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		errors.addError(error);

		LOGGER.error("JpaSystemException occured: ", e);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	public ResponseEntity<ErrorResponse> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
		
		ErrorResponse errors = new ErrorResponse();
		ErrorItem error = new ErrorItem();
		String errorCode = "error.database";
		String errorMessage = e.getMessage();
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		errors.addError(error);

		LOGGER.error("InvalidDataAccessResourceUsageException occured. ", e);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleConnectException(HttpMessageNotReadableException e) {
		ErrorResponse errors = new ErrorResponse();
		ErrorItem error = new ErrorItem();
		String errorCode = "error.request.mapping";
		String errorMessage = e.getMessage();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		errors.addError(error);

		LOGGER.error("HttpMessageNotReadableException occured: ", e);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@XmlRootElement(name = "error")
	public static class ErrorItem {
		
		private int status;
		private String code;
		private String message;

		@XmlAttribute
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		@XmlAttribute
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@XmlValue
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	@XmlRootElement(name = "errors")
	public static class ErrorResponse {
		
		private List<ErrorItem> errors = new ArrayList<>();

		@XmlElement(name = "error")
		public List<ErrorItem> getErrors() {
			return errors;
		}

		public void setErrors(List<ErrorItem> errors) {
			this.errors = errors;
		}

		public void addError(ErrorItem error) {
			this.errors.add(error);
		}
	}


}
