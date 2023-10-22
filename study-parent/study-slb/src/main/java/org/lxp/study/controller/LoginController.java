package org.lxp.study.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lxp.study.model.UserBase;
import org.lxp.study.service.UserService;
import org.lxp.study.util.IPAddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
  private static final String USER = "user";
  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
  @Inject
  private UserService userService;
  @Inject
  private HttpSession session;

  @RequestMapping(value = "/", method = GET)
  public ModelAndView index(HttpServletRequest request) {
    LOG.debug("into index page");
    ModelAndView mav = new ModelAndView("/WEB-INF/page/index.jsp");
    mav.addObject("remoteAddr", IPAddressUtil.getClientIpAddr(request));
    mav.addObject("localAddr", request.getLocalAddr());
    mav.addObject("localName", request.getLocalName());
    mav.addObject("localPort", request.getLocalPort());
    mav.addObject("user", session.getAttribute(USER));
    return mav;
  }

  @RequestMapping(value = "/login", method = GET)
  public ModelAndView login() {
    LOG.debug("into login page");
    ModelAndView mav = new ModelAndView("/WEB-INF/page/login.jsp");
    session.removeAttribute(USER);
    return mav;
  }

  @RequestMapping(value = "/doLogin", method = POST)
  public String doLogin(@RequestParam String account, @RequestParam String password) {
    LOG.debug("do login");
    UserBase user = userService.login(account, password);
    session.setAttribute(USER, user);
    return String.format("redirect:%s", user == null ? "/login" : "/");
  }
}
