package com.learning.ToDo_rest.exception;

import com.learning.ToDo_rest.exception.exceptions.InternalErrorsException;
import com.learning.ToDo_rest.exception.exceptions.TaskNotFoundException;
import com.learning.ToDo_rest.logger.Log;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> hanbleTaskNotFoundException(TaskNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "No se ha encontrado la task ya que no existe, procure usar una existente");
        Log.logger.error(error.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    public ResponseEntity<ErrorResponse> handleInternalErrors(InternalErrorsException ex) {
        ErrorResponse error = new ErrorResponse("Internal error", ex.getMessage());
        Log.logger.error(error.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
