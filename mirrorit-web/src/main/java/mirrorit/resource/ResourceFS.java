package mirrorit.resource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResourceFS {

	private static final ResourceFS ME = new ResourceFS();

	public Resource get(String url) {
		try {
			PermanentCacheFS cache = PermanentCacheFS.instance();
			Future<Resource> f = cache.get(url);
			Resource resource = f.get();
			return resource;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}

	}

	public static ResourceFS instance() {
		return ME;
	}

}
