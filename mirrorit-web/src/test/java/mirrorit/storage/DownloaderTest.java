package mirrorit.storage;

import static org.junit.Assert.assertEquals;
import mirrorit.AbstractTestCase;

import org.junit.Test;

public class DownloaderTest extends AbstractTestCase {

	@Test
	public void testDownload() throws Exception {
		DownloadManager dm = DownloadManager.instance();
		StorageFile file = dm.download("http://localhost:8580/test/myfile1.txt");

		// Thread.sleep(3000l);

		assertEquals("my file 1 txt", MirrorItUtil.readAllAsString(file, "utf-8").trim());
	}

}
