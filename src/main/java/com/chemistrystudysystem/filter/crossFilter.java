package com.chemistrystudysystem.filter;/**
 * @Auther: hmj
 * @Description:
 * @Date: 2019/1/31 14:23
 * @Version:1.0
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @ClassName:
 * @Description:
 * @Auther: hmj
 * @Date: 2019/1/31 14:23
 * @Version:1.0
 */
@Configuration
public class crossFilter extends WebMvcConfigurationSupport{
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST")
                .allowedHeaders("X-Auth-Token","Content-Type")
                .allowedHeaders("X-Auth-Token","Content-Type","X-CURRENT-USER-ID")
                .allowCredentials(true)
                .maxAge(180);
    }
}
