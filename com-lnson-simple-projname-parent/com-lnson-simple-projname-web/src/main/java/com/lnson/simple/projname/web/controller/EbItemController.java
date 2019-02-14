package com.lnson.simple.projname.web.controller;

import com.lnson.simple.projname.service.EbBrandService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "item")
public class EbItemController {

    @Autowired
    private EbBrandService ebBrandService;

    private final static Logger logger = LogManager.getLogger(EbItemController.class);

    @RequestMapping(value = "/index.do", method = {RequestMethod.GET})
    public ModelAndView indexAction() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/api.do", method = {RequestMethod.GET})
    public ModelAndView apiAction() {
        return new ModelAndView("api");
    }

}
