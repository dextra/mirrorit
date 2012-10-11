package mirrorit.storage;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import mirrorit.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class StorageFile implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(StorageFile.class);

	private File file;
	private File tmp;

	public StorageFile(File file) {
		this.file = file;
		this.tmp = new File(file.getPath() + ".mirrorit");
	}

	public synchronized boolean exists() {
		return file.exists() || tmp.exists();
	}

	public synchronized InputStream open() {
		try {
			waitForComplete();
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private synchronized void waitForComplete() {
		try {
			if (!isFinished()) {
				LOG.info("Waiting for file: " + file);
				wait(Config.instance().getWaitForFileTimeout());
				boolean finished = isFinished();
				LOG.info("Notify, finished: " + finished + " " + file);
				if (!finished) {
					throw new RuntimeException("timeout: " + file.getName());
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized boolean isFinished() {
		return file.exists();
	}

	public synchronized void close() {
		LOG.info("File close: " + file);
		tmp.renameTo(file);
		notifyAll();
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

	public synchronized void recovery() {
		if (exists() && !isFinished()) {
			delete();
		}
	}

	@Override
	public String toString() {
		return "" + file;
	}

	public File getFile() {
		return file;
	}

	public synchronized void append(byte[] buffer) {
		append(buffer, 0, buffer.length);
	}

	public synchronized void append(byte[] buffer, int o, int l) {
		FileOutputStream out = null;
		try {
			if (!tmp.getParentFile().exists()) {
				tmp.getParentFile().mkdirs();
			}
			out = new FileOutputStream(tmp, true);
			out.write(buffer, o, l);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtil.close(out);
		}
	}

}
