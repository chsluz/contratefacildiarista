package br.com.exemplo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/paginas/*")
public class FiltroAltenticacao implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		Object user = hreq.getSession().getAttribute("usuario");
		if (user == null) {
			hres.sendRedirect("../login.jsf");
			return;
		}
		chain.doFilter(request, response);

	}

	public void init(FilterConfig filter) throws ServletException {
		// TODO Auto-generated method stub

	}

}
