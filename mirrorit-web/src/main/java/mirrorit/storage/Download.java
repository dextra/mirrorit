package mirrorit.storage;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class Download implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(Download.class);

	private final HttpGet request;
	private StorageFile file;
	private boolean started = false;
	private Thread thread;

	public Download(String url) {
		request = new HttpGet(url);
	}

	@Override
	public String toString() {
		return "[request=" + (request == null ? "null" : request.getURI()) + ", file=" + file + "]";
	}

	private HttpClient getClient() {
		DefaultHttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
		return client;
	}

	public synchronized void start(StorageFile file) {
		if (started) {
			return;
		}
		this.file = file;
		started = true;
		this.thread = new Thread(this, "Downloader-" + file.getFile().getName());
		thread.setDaemon(true);
		file.create();
		this.thread.start();
	}

	@Override
	public void run() {
		InputStream in = null;
		try {
			LOG.info("Download: " + toString());
			HttpClient client = getClient();
			HttpResponse resp = client.execute(request);

			HttpEntity entity = resp.getEntity();
			in = MirrorItUtil.openStream(entity);
			int read = 0;
			byte[] buffer = new byte[1024 * 256];
			do {
				read = in.read(buffer);
				if (read > 0) {
					file.append(buffer, 0, read);
				}
			} while (read >= 0);
		} catch (Exception e) {
			LOG.error("Error while downloading", e);
		} finally {
			IOUtil.close(in);
			IOUtil.close(file);
		}
	}
}
