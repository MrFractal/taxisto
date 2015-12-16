package ru.trendtech.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.ExceptionJSONInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * File created by max on 21/06/2014 6:43.
 */

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<Object> handleInvalidRequest(Exception e, WebRequest request) {
        //HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //ExceptionJSONInfo error = new ExceptionJSONInfo();
        //error.setMessage(e.getMessage());
        //error.setUrl(request1.getRequestURL().toString());
        LOGGER.error("*********** Unhandled exception: ", e);

        ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
        errorCodeHelper.setErrorCode(-100);
        errorCodeHelper.setErrorMessage("Произошла неизвестная ошибка"); // e.toString()

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, errorCodeHelper, headers, HttpStatus.UNPROCESSABLE_ENTITY, request); // UNPROCESSABLE_ENTITY, SEE_OTHER
    }



    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<Object> handleErrorCodeHelper(CustomException e, WebRequest request) {
        ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
        errorCodeHelper.setErrorCode(e.getCode());
        errorCodeHelper.setErrorMessage(e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LOGGER.error("########## CUSTOM exception: ", e);
        return handleExceptionInternal(e, errorCodeHelper, headers, HttpStatus.OK, request);
    }


}
