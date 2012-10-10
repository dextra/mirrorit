package mirrorit.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mirrorit.MirroritHttpCodeException;
import mirrorit.resource.PermanentCacheFS;
import mirrorit.resource.Resource;
import mirrorit.resource.ResourceFS;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class ProxyFilter implements Filter {

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		PermanentCacheFS.instance().startDownloads();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String url = getTargetUrl(req);
		if (url == null) {
			chain.doFilter(request, response);
			return;
		}

		Resource resource = ResourceFS.instance().get(url);
		String mediaType = resource.getMediaType();
		String encode = resource.getEncode();
		String length = resource.getLength();

		if (mediaType == null) {
			throw new RuntimeException("mediaType is required: " + url);
		}

		resp.setContentType(mediaType);
		if (encode != null) {
			resp.setCharacterEncoding(encode);
		}
		if (length != null) {
			resp.setHeader("Content-Length", length);
		}

		resource.writeTo(resp.getOutputStream());
	}

	private String getTargetUrl(HttpServletRequest req) {
		String ret = req.getRequestURL().toString();
		if (!req.getMethod().toUpperCase().equals("GET")) {
			throw new MirroritHttpCodeException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		String query = req.getQueryString();
		if (query == null) {
			query = "";
		}
		if (query.trim().length() > 0) {
			throw new RuntimeException("we will not proxy/cache url with query string: " + ret + "?" + query);
		}
		System.out.println("query: " + query);
		if (ret.startsWith("http://localhost") || ret.startsWith("http://127.0.0.1")) {
			return null;
		}
		return ret;
	}

	@Override
	public void destroy() {

	}

}
