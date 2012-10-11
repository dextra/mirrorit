package mirrorit.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import mirrorit.config.Config;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class StorageTest {

	public static class ReaderThread extends Thread {

		private final StorageFile file;
		private Exception exception;

		public ReaderThread(StorageFile file) {
			this.file = file;
		}

		@Override
		public void run() {
			try {
				InputStream in = file.open();
				try {
					byte[] bytes = IOUtil.readAll(in);
					String msg = new String(bytes);
					if (!msg.equals("text1text2text3")) {
						throw new RuntimeException("expected: text1text2text3, but was: " + msg);
					}
				} finally {
					IOUtil.close(in);
				}
			} catch (Exception e) {
				this.exception = e;
			}
		}

		public Exception getException() {
			return exception;
		}

	}

	@Before
	public void setUp() {
		Config.instance().setWaitForFileTimeout(1000l);
	}

	@Test
	@Ignore
	public void testStorage() throws Exception {
		Storage storage = Storage.instance();
		storage.deleteAll();

		StorageFile file = storage.get("abc/text.txt");
		assertFalse(file.exists());
		assertTrue(file == storage.get("abc/text.txt"));

		file.create();
		assertTrue(file.exists());
		assertFalse(file.isFinished());
		InputStream in1 = file.open();
		assertEquals(0, in1.available());

		file.append("text1 ".getBytes());
		assertTrue(file.exists());
		assertFalse(file.isFinished());
		assertTrue(file == storage.get("abc/text.txt"));
		assertFileContent("text1 ", in1);
		assertEquals(0, in1.available());
		InputStream in2 = file.open();
		assertFileContent("text1 ", in2);
		in2.close();

		file.append("text2".getBytes());
		assertTrue(file.exists());
		assertFalse(file.isFinished());
		assertTrue(file == storage.get("abc/text.txt"));
		assertEquals(5, in1.available());
		assertFileContent("text2", in1);
		assertEquals(0, in1.available());
		in2 = file.open();
		assertFileContent("text1 text2", in2);
		in2.close();

		file.close();
		assertTrue(file.exists());
		assertTrue(file.isFinished());
		assertTrue(file == storage.get("abc/text.txt"));
		assertTrue(in1.read() < 0);
		in1.close();
		in2 = file.open();
		assertEquals("text1 text2", new String(IOUtil.readAll(in2)));
		in2.close();
	}

	private void assertFileContent(String expected, InputStream in) throws Exception {
		int available = in.available();
		assertEquals(expected.length(), available);
		byte[] buffer = new byte[available];
		int read = in.read(buffer);
		assertEquals(expected, new String(buffer));
		assertEquals(expected.length(), read);
	}

	@Test
	public void testStorageTreads() throws Exception {
		Storage storage = Storage.instance();
		storage.deleteAll();

		StorageFile file = storage.get("abc/text.txt");
		file.create();

		ReaderThread t1 = new ReaderThread(file);
		ReaderThread t2 = new ReaderThread(file);

		t1.start();
		t2.start();

		Thread.sleep(100l);

		file.append("text1".getBytes());

		Thread.sleep(100l);

		ReaderThread t3 = new ReaderThread(file);

		t3.start();

		file.append("text2".getBytes());
		Thread.sleep(100l);
		file.append("text3".getBytes());

		file.close();

		t1.join();
		t2.join();
		t3.join();

		assertException(t1.getException());
		assertException(t2.getException());
		assertException(t3.getException());
	}

	private void assertException(Exception exception) {
		if (exception != null) {
			throw new RuntimeException(exception);
		}
	}

}
