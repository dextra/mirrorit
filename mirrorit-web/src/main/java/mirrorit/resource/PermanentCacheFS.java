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

	public Future<Resource> get(String url) {
		Download download = queue(url);
		return download;
	}

	private Download queue(String url) {
		return queue.add(url);
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
