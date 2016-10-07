package com.springframework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextProvider {

	@Autowired
	public static AnnotationConfigApplicationContext context;

	private SpringContextProvider() {

	}

}
