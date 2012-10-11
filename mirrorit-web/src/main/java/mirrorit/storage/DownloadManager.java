package mirrorit.storage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

public class DownloadManager {

	private static final DownloadManager ME = new DownloadManager();

	private Map<String, Download> downloads = new HashMap<String, Download>();

	public static DownloadManager instance() {
		return ME;
	}

	public StorageFile download(String url) {
		HttpGet req = new HttpGet(url);
		URI uri = req.getURI();
		if (!"http".equals(uri.getScheme())) {
			throw new RuntimeException("protocol not supported: " + uri);
		}
		url = uri.toString();
		String id = createId(req);

		StorageFile file = Storage.instance().get(id);
		if (file.exists()) {
			return file;
		}

		Download download = getDownload(uri.toString());
		download.start(file);
		return file;
	}

	private String createId(HttpGet req) {
		URI uri = req.getURI();
		int port = uri.getPort();
		if (port < 0) {
			port = 80;
		}
		String name = "" + uri.getScheme() + "/" + uri.getHost() + "/" + port + uri.getPath();
		return name;
	}

	private synchronized Download getDownload(String url) {
		Download ret = downloads.get(url);
		if (ret == null) {
			ret = new Download(url);
			downloads.put(url, ret);
		}
		return ret;
	}

}
