package br.com.hfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public class JsfSpringWebApplicationInitializer implements WebApplicationInitializer {

	private static final Logger log = LogManager.getLogger(JsfSpringWebApplicationInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

		// pacote raiz no qual o Spring irá procurar por suas classes.
		// Como temos somente um pacote, então já foi colocado diretamente o pacote em
		// que está classe aqui está inserida.
		//br.com.hfs		
		applicationContext.scan(JsfSpringWebApplicationInitializer.class.getPackage().getName());

		servletContext.addListener(new ContextLoaderListener(applicationContext));
		servletContext.addListener(new RequestContextListener());
		
		log.info("Init " + this.getClass().getName());		
	}

}