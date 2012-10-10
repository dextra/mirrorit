package mirrorit.resource;

import java.io.Closeable;

import javax.servlet.ServletOutputStream;

import com.googlecode.mycontainer.commons.file.ContentTypeUtil;

public abstract class Resource implements Closeable {

	private String host;
	private Integer port;
	private String path;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		if (port != null && port < 0) {
			port = null;
		}
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public abstract String getEncode();

	public String getMediaType() {
		return ContentTypeUtil.getContentTypeByPath(path);
	}

	public abstract String getLength();

	public void writeTo(ServletOutputStream outputStream) {
		
	}

}
