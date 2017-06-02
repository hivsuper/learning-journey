package org.lxp.springboot.controller;

import org.lxp.springboot.vo.BaseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@ControllerAdvice
public class ExceptionController implements ErrorController {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
    private static final String ERROR_PATH = "/error";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    @ResponseBody
    @RequestMapping(value = ERROR_PATH)
    public ModelAndView handleError() {
        return resolveException(HttpStatus.NOT_FOUND);
    }

    /**
     * implement ErrorController to handle 404
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView resolveException(HttpRequestMethodNotSupportedException e) {
        return resolveException(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public <T extends TypeMismatchException> ModelAndView resolveException(T e) {
        return resolveException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public <T extends ServletRequestBindingException> ModelAndView resolveException(T e) {
        return resolveException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(Exception e) {
        return resolveException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView resolveException(final Exception e, final HttpStatus status) {
        LOG.error(e.getMessage(), e);
        return resolveException(status);
    }

    private ModelAndView resolveException(final HttpStatus status) {
        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        mav.setView(view);
        view.setObjectMapper(MAPPER);
        BaseVO baseVO = new BaseVO();
        baseVO.setResCode(status.value());
        baseVO.setResDes(status.getReasonPhrase());
        mav.addObject(baseVO);
        return mav;
    }
}
