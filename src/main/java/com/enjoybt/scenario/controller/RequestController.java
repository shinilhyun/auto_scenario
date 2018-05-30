package com.enjoybt.scenario.controller;

import com.enjoybt.scenario.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 0. Project  : 화산재해대응시스템
 * 1. Package : com.enjoybt.scenario.controller
 * 2. Comment : 
 * 3. Auth  : aiden_shin
 * 4. Date  : 2018-05-24 오후 9:20
 * 5. History : 
 * 이름     : 일자          : 근거자료   : 변경내용
 * ------------------------------------------------------
 * aiden_shin : 2018-05-24 :            : 신규 개발.
 *
 */
@Controller
public class RequestController {

    private static Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    RequestService requestService;

    @RequestMapping(value = "/")
    public String home(){
        logger.info("home");
        return "home";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView model = new ModelAndView();
        model.setViewName("list");

        return model;
    }

    @RequestMapping(value = "/run", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void run(){
        logger.info("run");

        requestService.requestScenario();
    }
}
