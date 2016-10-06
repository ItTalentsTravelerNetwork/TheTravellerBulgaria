package com.springframework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextProvider {

	
	private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigurationFile.class);

	
	private SpringContextProvider() {}
	
	
	public synchronized static AnnotationConfigApplicationContext getContext() {
		return context;
	}
	
	
}
