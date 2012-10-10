package mirrorit.resource;

public class ResourceFS {

	private static final ResourceFS ME = new ResourceFS();

	public Resource get(String url) {
		PermanentCacheFS cache = PermanentCacheFS.instance();
		Resource resource = cache.get(url);
		return resource;

	}

	public static ResourceFS instance() {
		return ME;
	}

}
