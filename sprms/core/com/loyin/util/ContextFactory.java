package com.jyd.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ContextFactory {

	  private static WebApplicationContext context = null;

	  private static ServletContext servletContext = null;

	  private static ContextLoader contextLoader = new ContextLoader();

	  public static WebApplicationContext getContext()
	  {
	    if (context == null)
	    {
	      if (contextLoader == null) contextLoader = new ContextLoader();
	      if (servletContext == null) {
	        throw new RuntimeException("servletContext must be set first!");
	      }
	      context = ContextLoader.getCurrentWebApplicationContext();
	      if (context == null)
	        context = contextLoader.initWebApplicationContext(servletContext);
	    }
	    return context;
	  }

	  public static void destroyContext()
	  {
	    if (contextLoader != null)
	    {
	      contextLoader.closeWebApplicationContext(servletContext);
	      context = null;
	    }
	  }

	  public static void setServletContext(ServletContext servletContext1)
	  {
	   servletContext = servletContext1;
	  }

	  public static ServletContext getServletContext()
	  {
	    return servletContext;
	  }
}
