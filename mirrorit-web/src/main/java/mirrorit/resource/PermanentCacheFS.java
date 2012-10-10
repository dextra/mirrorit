package mirrorit.resource;

public class PermanentCacheFS {

	private static final PermanentCacheFS ME = new PermanentCacheFS();

	public static PermanentCacheFS instance() {
		return ME;
	}

	public Resource get(String url) {
		RemoteRepositoryFS fs = RemoteRepositoryFS.instance();
		Resource resource = fs.get(url);
		return resource;
	}

}
