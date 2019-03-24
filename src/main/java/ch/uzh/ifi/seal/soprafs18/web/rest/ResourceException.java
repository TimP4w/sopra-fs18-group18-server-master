package ch.uzh.ifi.seal.soprafs18.web.rest;

import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {

   private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

   public HttpStatus getHttpStatus() {
       return httpStatus;
   }

   public ResourceException(HttpStatus httpStatus, String message) {
       super(message);
       this.httpStatus = httpStatus;
   }
}