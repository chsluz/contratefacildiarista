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
import br.com.contratediarista.enuns.Ativo;
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
				hres.sendRedirect("../../paginas/publico/login.jsf");
			}
			return;
		} else {
			
			if(hreq.getRequestURI().contains("bloqueado")) {
				chain.doFilter(request, response);
				return;
			}
			else if(user.getAtivo() == Ativo.NAO) {
				chain.doFilter(request, response);
			}
			if(hreq.getRequestURI().equals("/ContrateDiarista/")) {
				hres.sendRedirect("paginas/publico/login.jsf");
				return;
			}
			TipoUsuario tipoUsuario = user.getTipoUsuario();
			if (TipoUsuario.ADMINISTRADOR.equals(tipoUsuario)) {
				chain.doFilter(request, response);
				return;
			} else if (TipoUsuario.CONTRATANTE.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("login")) {
					hres.sendRedirect("../../paginas/contratante/visualizar_vagas_prestador_aprovado.jsf");
				}
				if (hreq.getRequestURI().contains("/contratante/") || hreq.getRequestURI().contains("/publico/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("../../paginas/contratante/visualizar_vagas_prestador_aprovado.jsf");
				}
				return;
			} else if (TipoUsuario.PRESTADOR.equals(user.getTipoUsuario())) {
				if (hreq.getRequestURI().contains("login")) {
					hres.sendRedirect("../../paginas/prestador/visualizar_vagas_vinculadas.jsf");
				} else if (hreq.getRequestURI().contains("/prestador/") || hreq.getRequestURI().contains("/publico/")) {
					chain.doFilter(request, response);
				} else {
					hres.sendRedirect("../../paginas/prestador/visualizar_vagas_vinculadas.jsf");
				}
				return;
			} else {
				if(hreq.getRequestURI().contains("login")) {
					hres.sendRedirect("../../paginas/moderador/consultar_solicitacao.jsf");
				}
				
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
