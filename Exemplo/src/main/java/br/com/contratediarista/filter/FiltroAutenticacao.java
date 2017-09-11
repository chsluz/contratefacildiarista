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
public class FiltroAutenticacao implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		Usuario user = (Usuario) hreq.getSession().getAttribute("usuario");
		if (user == null) {
			if (hreq.getRequestURI().equals("/ContrateDiarista/")) {
				hres.sendRedirect("paginas/publico/pagina_inicial.jsf");
				return;
			}
			if (hreq.getRequestURI().contains("login") || hreq.getRequestURI().contains("cadastro_usuario")) {
				chain.doFilter(request, response);
			} else {
				hres.sendRedirect("login.jsf");
			}
			return;
		} else {
			TipoUsuario tipoUsuario = user.getTipoUsuario();
			if (hreq.getRequestURI().contains("login") || hreq.getRequestURI().contains("cadastro_usuario")) {
				hres.sendRedirect("pagina_inicial.jsf");
				return;
			} else if (hreq.getRequestURI().equals("/ContrateDiarista/")) {
				hres.sendRedirect("paginas/publico/pagina_inicial.jsf");
				return;
			} else if (hreq.getRequestURI().contains("pagina_inicial")) {
				chain.doFilter(request, response);
				return;
			} else if (TipoUsuario.ADMINISTRADOR.equals(tipoUsuario) || hreq.getRequestURI().contains("/publico/")) {
				chain.doFilter(request, response);
				return;
			} else if (TipoUsuario.CONTRATANTE.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("/contratante/") || hreq.getRequestURI().contains("/publico/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("acesso_negado.jsf");
				}
				return;
			} else if (TipoUsuario.PRESTADOR.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("/prestador/") || hreq.getRequestURI().contains("/publico/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("acesso_negado.jsf");
				}
				return;
			} else {
				if (hreq.getRequestURI().contains("/moderador/") || hreq.getRequestURI().contains("/publico/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("acesso_negado.jsf");
				}
				return;
			}
		}
	}

	@Override
	public void init(FilterConfig filter) throws ServletException {

	}

}
