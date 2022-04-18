//Guomin Huang @11/10/2021
//
package io.devplus.aziothubdigitaltwinsconnector.webapp;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThymeleafLayoutInterceptor implements HandlerInterceptor {
    private static final String DEFAULT_VIEW_LAYOUT = "shared/layouts/default";
    private static final String DEFAULT_VIEW_NAME_KEY = "view";
    private static final String DEFAULT_VIEW_TITLE_KEY = "title";
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        if (null == modelAndView || !modelAndView.hasView()) {
            return;
        }

        String viewName = modelAndView.getViewName();
        if (viewName.startsWith("redirect:") || viewName.startsWith("forward:")) {
            return;
        }

        if (!modelAndView.getModelMap().containsAttribute("title")) {
            //Add default title for this view
            modelAndView.addObject(DEFAULT_VIEW_TITLE_KEY, "Azure IoT Hub and Digital Twins Connector");
        }
        modelAndView.addObject(DEFAULT_VIEW_NAME_KEY, viewName);
        modelAndView.setViewName(DEFAULT_VIEW_LAYOUT);
    }
}
