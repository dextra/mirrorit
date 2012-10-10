package mirrorit.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

import com.googlecode.mycontainer.commons.io.IOUtil;

public class StorageTest {

	@Test
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
		assertEquals(4, in1.available());
		assertFileContent("text2", in1);
		assertEquals(0, in1.available());
		in2 = file.open();
		assertFileContent("text1 text2", in2);
		in2.close();

		file.close();
		assertTrue(file.exists());
		assertTrue(file.isFinished());
		assertTrue(file == storage.get("abc"));
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

}
