package mirrorit.resource;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class RemoteRepositoryFS {

	public static final RemoteRepositoryFS ME = new RemoteRepositoryFS();

	public static RemoteRepositoryFS instance() {
		return ME;
	}

	public Resource get(String url) {

		HttpGet get = new HttpGet(url);
		URI uri = get.getURI();
		if (!"http".equals(uri.getScheme())) {
			throw new RuntimeException("we will proxy/cache just http protocol: " + url);
		}

		System.out.println(uri.getHost());
		System.out.println(uri.getPort());
		System.out.println(uri.getPath());
		System.out.println(uri.toString());
		// Resource resource = download(get);
		return null;

		//
		// try {
		// HttpClient client = getClient();
		// HttpResponse resp = client.execute(get);
		//
		//
		// System.out.println(resp.getStatusLine().getStatusCode());
		// System.out.println(resp.getStatusLine().getReasonPhrase());
		//
		// HttpEntity entity = resp.getEntity();
		// InputStream in = entity.getContent();
		// IOUtil.close(in);
		// get.abort();
		//
		// return null;
		// } catch (ClientProtocolException e) {
		// throw new RuntimeException(e);
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }
	}

	private HttpClient getClient() {
		DefaultHttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
		return client;
	}

}
