package mirrorit.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

public class DownloadQueue {

	private final List<Download> queue = new ArrayList<Download>();

	private final Map<String, Download> map = new HashMap<String, Download>();

	public synchronized Download add(String url) {
		HttpGet get = new HttpGet(url);
		URI uri = get.getURI();
		url = uri.toString();
		if (!"http".equals(uri.getScheme())) {
			throw new RuntimeException("we will proxy/cache just http protocol: " + url);
		}

		Download download = map.get(url);
		if (download == null) {
			download = new Download(get);
			queue.add(download);
			map.put(url, download);
			notifyAll();
		}
		return download;
	}

	public synchronized Download consume() {
		try {
			while (true) {
				if (!queue.isEmpty()) {
					Download ret = queue.remove(0);
					return ret;
				} else {
					wait();
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
