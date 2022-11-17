package com.sparrow.spring.starter;

import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewUtils {
    public static ModelAndView  redirect(String url){
      return new ModelAndView("redirect:/transit?"+url);
    }
}
