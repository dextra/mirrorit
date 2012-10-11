package mirrorit.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class MirrorItUtil {

	public static String readAllAsString(StorageFile file, String enc) {
		try {
			byte[] data = readAll(file);
			return new String(data, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] readAll(StorageFile file) {
		InputStream in = null;
		try {
			in = file.open();
			return IOUtil.readAll(in);
		} finally {
			IOUtil.close(in);
		}
	}

	public static InputStream openStream(HttpEntity entity) {
		try {
			return entity.getContent();
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
