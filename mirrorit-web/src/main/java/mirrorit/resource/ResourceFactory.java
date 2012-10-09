package mirrorit.resource;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;

public class ResourceFactory {

	public Resource get(String url) {
		HttpGet get = new HttpGet(url);
		Resource ret = new DownloadResource();

		URI uri = get.getURI();
		ret.setHost(uri.getHost());
		ret.setPort(uri.getPort());
		ret.setPath(uri.getPath());

		return ret;
	}

}
