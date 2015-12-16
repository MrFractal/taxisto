package ru.trendtech.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.trendtech.common.mobileexchange.model.driver.RegisterDriverResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by petr on 11.11.2014.
 */

public class ExceptionHandlingController extends RuntimeException{

    private String exceptionMsg;

    public ExceptionHandlingController(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionMsg(){
        return this.exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}