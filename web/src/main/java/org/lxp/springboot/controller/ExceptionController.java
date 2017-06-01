package org.lxp.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@ControllerAdvice
public class ExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView resolveException(HttpRequestMethodNotSupportedException e) {
        return resolveException(e, "405");
    }

    @ExceptionHandler(TypeMismatchException.class)
    public <T extends TypeMismatchException> ModelAndView resolveException(T e) {
        return resolveException(e, "400");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public <T extends ServletRequestBindingException> ModelAndView resolveException(T e) {
        return resolveException(e, "400");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(Exception e) {
        return resolveException(e, "500");
    }

    private ModelAndView resolveException(final Exception e, final String errorCode) {
        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        mav.setView(view);
        view.setObjectMapper(MAPPER);
        mav.addObject(errorCode);
        LOG.error(e.getMessage(), e);
        return mav;
    }
}
