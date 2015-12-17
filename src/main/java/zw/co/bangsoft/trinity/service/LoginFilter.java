package zw.co.bangsoft.trinity.service;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(urlPatterns = {"hello.xhtml"} )
public class LoginFilter implements Filter {

  @Inject private LoginBean loginBean;

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

	  System.out.println("filter is working..");

	  String path = ((HttpServletRequest) request).getRequestURI();

	  System.out.println("Requested URL = " + path);

//	  if (path.startsWith("/login")) {
//	      chain.doFilter(request, response); // Just continue chain.
//	  } else {
//	      // Do your business stuff here for all paths other than /login.
//	      if (loginBean.isLoggedIn()) {
//	          System.out.println("User is logged in..");
//	          chain.doFilter(request, response); // Just continue chain.
//	      } else {
//	          HttpServletResponse httpResponse = (HttpServletResponse) response;
//	          httpResponse.sendRedirect("login/auth.xhtml");
//	      }
//	  }

	  if (loginBean.isLoggedIn()) {
	      chain.doFilter(request, response); // Just continue chain.
	  } else {
        // Do your business stuff here for all paths other than /login.
	      HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendRedirect("/trinity-core/faces/login/auth.xhtml");
   //     request.getRequestDispatcher(path).forward(request, response);
    }
  }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
