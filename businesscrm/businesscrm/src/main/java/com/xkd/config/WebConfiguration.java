package com.xkd.config;

import com.xkd.filter.LoginFilter;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * Created by dell on 2018/4/12.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {



    @Bean
    public LoginFilter getFilter(){
       return new LoginFilter();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor( getFilter()).addPathPatterns("/*/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
                 registry.addResourceHandler("swagger-ui.html")
                 .addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
                super.addResourceHandlers(registry);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
         String os = System.getProperty("os.name");
         String filePath=null;
        if (os.toUpperCase().contains("WIN")){
            filePath="F:/temporary";
        }else {
            filePath="/tmp/temporary";
        }
        File file=new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }
        factory.setLocation(filePath);
        return factory.createMultipartConfig();
    }




}
