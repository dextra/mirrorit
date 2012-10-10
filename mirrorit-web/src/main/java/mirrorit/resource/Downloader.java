package mirrorit.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class Downloader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);

	private final int id;

	private final Thread thread;

	private final DownloadQueue queue;

	public Downloader(int i, DownloadQueue queue) {
		this.id = i;
		this.queue = queue;
		this.thread = new Thread(this, "downloader-" + id);
		this.thread.setDaemon(true);
	}

	public void start() {
		this.thread.start();
	}

	@Override
	public void run() {
		LOG.info("Downloader started: " + Thread.currentThread().getName());
		while (true) {
			Download download = queue.consume();
			LOG.info("Download: " + download);
			HttpGet req = download.getRequest();
			HttpClient client = getClient();
			FileOutputStream out = null;
			File tmp = Repository.instance().createTempFile(req.getURI());
			try {
				out = new FileOutputStream(tmp);
				HttpResponse resp = client.execute(req);
				HttpEntity entity = resp.getEntity();
				entity.writeTo(out);
				LOG.info("Finished");
				download.ready(tmp);
				tmp.renameTo(new File(tmp.getParent(), tmp.getName().replaceAll("\\.mirrorit$", "")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				req.abort();
				IOUtil.close(out);
			}
		}
	}

	private HttpClient getClient() {
		DefaultHttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
		return client;
	}

}
