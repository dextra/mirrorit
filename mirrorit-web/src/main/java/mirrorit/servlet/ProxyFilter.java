package mirrorit.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mirrorit.resource.Resource;
import mirrorit.resource.ResourceFactory;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class ProxyFilter implements Filter {

	private ResourceFactory rf;

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		rf = new ResourceFactory();
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

		Resource resource = rf.get(url);
		String mediaType = resource.getMediaType();
		String encode = resource.getEncode();
		String length = resource.getLength();

		if (mediaType == null || length == null) {
			throw new RuntimeException("mediaType and length is required: " + url);
		}

		resp.setContentType(mediaType);
		if (encode != null) {
			resp.setCharacterEncoding(encode);
		}
		resp.setHeader("Content-Length", length);

		InputStream in = resource.openStream();
		IOUtil.copyAll(in, resp.getOutputStream());
	}

	private String getTargetUrl(HttpServletRequest req) {
		String ret = req.getRequestURL().toString();
		if (ret.startsWith("http://localhost") || ret.startsWith("http://127.0.0.1")) {
			return null;
		}
		return ret;
	}

	@Override
	public void destroy() {

	}

}
