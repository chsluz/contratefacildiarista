package br.com.contratediarista.filter;

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

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;

@WebFilter("/paginas/*")
public class FiltroAltenticacao implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		Usuario user = (Usuario) hreq.getSession().getAttribute("usuario");
		if (user == null) {
			hres.sendRedirect("../login.jsf");
			return;
		} else {
			TipoUsuario tipoUsuario = user.getTipoUsuario();
			if (TipoUsuario.ADMINISTRADOR.equals(tipoUsuario)) {
				chain.doFilter(request, response);
			} else if (TipoUsuario.CONTRATANTE.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("/contratante/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("../acesso_negado.jsf");
				}
			} else if (TipoUsuario.PRESTADOR.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("/prestador/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("../acesso_negado.jsf");
				}
			} else {
				if (hreq.getRequestURI().contains("/moderador/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("../acesso_negado.jsf");
				}
			}
		}
	}

	public void init(FilterConfig filter) throws ServletException {

	}

}
