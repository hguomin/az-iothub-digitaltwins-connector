//Guomin Huang @11/10/2021
//
package com.azure.aziothubdigitaltwinsconnector.webapp.configuration;

import com.azure.aziothubdigitaltwinsconnector.webapp.ThymeleafLayoutInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThymeleafLayoutInterceptor())
                .addPathPatterns("/**");
    }
}
