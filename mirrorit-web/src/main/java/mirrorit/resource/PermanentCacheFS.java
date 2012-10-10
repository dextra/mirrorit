package mirrorit.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class PermanentCacheFS {

	private static final PermanentCacheFS ME = new PermanentCacheFS();

	private DownloadQueue queue = new DownloadQueue();

	private List<Downloader> downloaders = new ArrayList<Downloader>();

	public static PermanentCacheFS instance() {
		return ME;
	}

	public synchronized Future<Resource> get(String url) {
		Resource resource = Repository.instance().find(url);
		if(resource != null) {
			return new CachedResource(resource);
		}
		
		Download download = queue.add(url);
		return download;
	}

	public void startDownloads() {
		for (int i = 0; i < 10; i++) {
			Downloader downloader = new Downloader(i, queue);
			this.downloaders.add(downloader);
		}

		for (Downloader downloader : downloaders) {
			downloader.start();
		}
	}

}
