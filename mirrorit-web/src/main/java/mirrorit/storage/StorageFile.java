package mirrorit.storage;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class StorageFile implements Closeable {

	private File file;
	private File tmp;

	public StorageFile(File file) {
		this.file = file;
		this.tmp = new File(file.getPath() + ".mirrorit");
	}

	public boolean exists() {
		return file.exists();
	}

	public InputStream open() {
		return null;
	}

	public synchronized void append(byte[] bytes) {
		FileOutputStream out = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			out = new FileOutputStream(file, true);
			out.write(bytes);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtil.close(out);
		}
	}

	public boolean isFinished() {
		return false;
	}

	public void close() {

	}

	public synchronized void create() {
		try {
			delete();
			tmp.getParentFile().mkdirs();
			tmp.createNewFile();
			if (!tmp.exists()) {
				throw new RuntimeException("error on create: " + file);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void delete() {
		if (file.exists() && !file.delete()) {
			throw new RuntimeException("error on delete: " + file);
		}
		if (tmp.exists() && !tmp.delete()) {
			throw new RuntimeException("error on delete: " + file);
		}
	}

}