package mirrorit.resource;

import java.io.File;
import java.net.URI;

public class Repository {

	private static final Repository ME = new Repository();

	public static Repository instance() {
		return ME;
	}

	public Resource find(String url) {
		return null;
	}



	public synchronized File createTempFile(URI uri) {
		int port = uri.getPort();
		if (port < 0) {
			port = 80;
		}
		File ret = new File("/tmp/repo/" + uri.getHost() + "/" + port, uri.getPath() + ".mirrorit");
		ret.getParentFile().mkdirs();
		return ret;
	}

}
