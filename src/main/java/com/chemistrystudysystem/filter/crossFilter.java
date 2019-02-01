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
        //配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径。
        corsRegistry.addMapping("/**")
                //允许所有的请求域名访问我们的跨域资源，可以固定单条或者多条内容
                .allowedOrigins("*")
                //允许所有的请求方法访问该跨域资源服务器，如：POST、GET、PUT、DELETE等。
                .allowedMethods("GET","POST")
                //允许所有的请求header访问，可以自定义设置任意请求头信息，如："X-YAUTH-TOKEN"
                .allowedHeaders("X-Auth-Token","Content-Type")
                .allowedHeaders("X-Auth-Token","Content-Type","X-CURRENT-USER-ID")
                //是否允许用户发送、处理 cookie
                .allowCredentials(true)
                //预检请求的有效期，单位为秒。有效期内，不会重复发送预检请求
                .maxAge(180);
    }
}
