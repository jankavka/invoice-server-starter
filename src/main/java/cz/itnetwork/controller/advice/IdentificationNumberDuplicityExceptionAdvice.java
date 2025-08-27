package cz.itnetwork.controller.advice;

import cz.itnetwork.dto.MyErrorResponse;
import cz.itnetwork.service.exceptions.IdentificationNumberDuplicityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IdentificationNumberDuplicityExceptionAdvice {


    @ExceptionHandler(IdentificationNumberDuplicityException.class)
    public ResponseEntity<MyErrorResponse> handleIdentificationNumberDuplicityAdvice(IdentificationNumberDuplicityException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(new MyErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
