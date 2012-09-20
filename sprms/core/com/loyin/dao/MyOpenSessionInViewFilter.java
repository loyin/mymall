package com.jyd.dao;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
public class MyOpenSessionInViewFilter extends OncePerRequestFilter
{
	  public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";
	  private String sessionFactoryBeanName = "sessionFactory";

	  private boolean singleSession = true;

	  private FlushMode flushMode = FlushMode.AUTO;

	  public void setSessionFactoryBeanName(String sessionFactoryBeanName)
	  {
	    this.sessionFactoryBeanName = sessionFactoryBeanName;
	  }

	  protected String getSessionFactoryBeanName()
	  {
	    return this.sessionFactoryBeanName;
	  }

	  public void setSingleSession(boolean singleSession)
	  {
	    this.singleSession = singleSession;
	  }

	  protected boolean isSingleSession()
	  {
	    return this.singleSession;
	  }

	  public void setFlushMode(FlushMode flushMode)
	  {
	    this.flushMode = flushMode;
	  }

	  protected FlushMode getFlushMode()
	  {
	    return this.flushMode;
	  }

	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException
	  {
	    SessionFactory sessionFactory = lookupSessionFactory(request);
	    boolean participate = false;

	    if (isSingleSession())
	    {
	      if (TransactionSynchronizationManager.hasResource(sessionFactory))
	      {
	        participate = true;
	      }
	      else {
	        this.logger.debug("Opening single Hibernate Session in OpenSessionInViewFilter");
	        Session session = getSession(sessionFactory);
	        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	      }

	    }
	    else if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory))
	    {
	      participate = true;
	    }
	    else {
	      SessionFactoryUtils.initDeferredClose(sessionFactory);
	    }

	    try
	    {
	      filterChain.doFilter(request, response);
	    }
	    finally
	    {
	      if (!participate)
	        if (isSingleSession())
	        {
	        	//注释调这些以开启session 防止lazy问题
	          SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.unbindResource(sessionFactory);

	   //       this.logger.debug("Closing single Hibernate Session in OpenSessionInViewFilter");
	          closeSession(sessionHolder.getSession(), sessionFactory);
	        }
	        else
	        {
	      //    SessionFactoryUtils.processDeferredClose(sessionFactory);
	        }
	    }
	  }

	  protected SessionFactory lookupSessionFactory(HttpServletRequest request)
	  {
	    return lookupSessionFactory();
	  }

	  protected SessionFactory lookupSessionFactory()
	  {
	    if (this.logger.isDebugEnabled()) {
	      this.logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
	    }
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

	    return (SessionFactory)wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
	  }

	  protected Session getSession(SessionFactory sessionFactory)
	    throws DataAccessResourceFailureException
	  {
	    Session session = SessionFactoryUtils.getSession(sessionFactory, true);
	    FlushMode flushMode = getFlushMode();
	    if (flushMode != null) {
	      session.setFlushMode(flushMode);
	    }
	    return session;
	  }

	  protected void closeSession(Session session, SessionFactory sessionFactory)
	  {
	    SessionFactoryUtils.closeSession(session);
	  }
	}