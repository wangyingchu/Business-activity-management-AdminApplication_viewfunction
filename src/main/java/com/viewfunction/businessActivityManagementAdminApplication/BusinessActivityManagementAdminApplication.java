package com.viewfunction.businessActivityManagementAdminApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class BusinessActivityManagementAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessActivityManagementAdminApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean testServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new com.vaadin.server.VaadinServlet());
        registration.addInitParameter("UI","com.viewfunction.vfbam.ui.VFBPM_AdminApplicationUI");
		registration.addUrlMappings("/*");
		return registration;
	}
}
