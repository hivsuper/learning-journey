package org.lxp.springboot;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.lxp.springboot.swagger.SwaggerFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

@EnableTransactionManagement
/** disable auto web security **/
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@MapperScan("org.lxp.springboot.dao")
public class Bootstrap {
    @Value("${web.swagger.enabled}")
    private boolean swaggerAllowed;

    @Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
    public FilterChainProxy getFilterChainProxy() throws ServletException, Exception {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter("UTF-8", true);
        SwaggerFilter swaggerFilter = new SwaggerFilter(swaggerAllowed);

        List<SecurityFilterChain> listOfFilterChains = new ArrayList<>();
        listOfFilterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/swagger-ui.html"),
                swaggerFilter, encodingFilter));
        listOfFilterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/swagger-resources/**"),
                swaggerFilter, encodingFilter));
        listOfFilterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/v2/api-docs"), swaggerFilter,
                encodingFilter));
        listOfFilterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/webjars/**"), swaggerFilter,
                encodingFilter));
        listOfFilterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"), encodingFilter));
        return new FilterChainProxy(listOfFilterChains);
    }

    @Bean
    public FilterRegistrationBean securityFilterChainRegistration() {
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        delegatingFilterProxy.setTargetBeanName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(delegatingFilterProxy);
        registrationBean.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
