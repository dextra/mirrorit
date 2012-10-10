package mirrorit.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Storage {

	private static final Logger LOG = LoggerFactory.getLogger(Storage.class);

	private static final Storage ME = new Storage();

	private final Map<String, StorageFile> files = new HashMap<String, StorageFile>();

	private File base;

	public static Storage instance() {
		return ME;
	}

	public synchronized StorageFile get(String id) {
		StorageFile ret = files.get(id);
		if (ret == null) {
			File base = getBase();
			File file = new File(base, id);
			ret = new StorageFile(file);
			files.put(id, ret);
		}
		return ret;
	}

	private synchronized File getBase() {
		if (base == null) {
			File ret = new File("/tmp/repo");
			ret.mkdirs();
			if (!ret.exists() || !ret.isDirectory()) {
				throw new RuntimeException("base must be a directory: " + ret);
			}
			base = ret;
		}
		return base;
	}

	public void deleteAll() {
		try {
			File base = getBase();
			LOG.info("Delete all: " + base);
			FileUtils.deleteDirectory(base);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
