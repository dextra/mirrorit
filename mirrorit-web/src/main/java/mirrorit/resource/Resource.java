package mirrorit.resource;

import javax.servlet.ServletOutputStream;

import com.googlecode.mycontainer.commons.file.ContentTypeUtil;

public class Resource {

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

	public String getEncode() {
		return null;
	}

	public String getMediaType() {
		return ContentTypeUtil.getContentTypeByPath(path);
	}

	public String getLength() {
		return null;
	}

	public void writeTo(ServletOutputStream outputStream) {

	}

}
