package ru.trendtech.controllers.mobile;

import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * Created by petr on 12.01.2015.
 */

@ControllerAdvice
public class ExceptionHandlerController extends Exception{
//    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
//
//    @ExceptionHandler(value = Exception.class)
//    public @ResponseBody ErrorCodeHelper handleAllException(HttpServletRequest request, Exception ex) {
//        logger.info("GLOBAL");
//        ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
//         errorCodeHelper.setErrorCode(-1);
//         errorCodeHelper.setErrorMessage("mess - "+ex.getMessage());
//          return errorCodeHelper;
//
//    }
//
//
//    @ExceptionHandler(GlobalException.class)
//    @ResponseBody
//    public ErrorCodeHelper handleError(HttpServletRequest req, GlobalException ex){
//          logger.info("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB= "+ex);
//          ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
//          errorCodeHelper.setErrorCode(ex.getErrorCode());
//          errorCodeHelper.setErrorMessage(ex.getErrorMessage());
//            return errorCodeHelper;
//    }

}
