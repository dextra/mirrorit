package mirrorit.download;

import static org.junit.Assert.assertEquals;
import mirrorit.AbstractTestCase;

import org.junit.Ignore;
import org.junit.Test;

public class DownloaderTest extends AbstractTestCase {

	@Test
	@Ignore
	public void testDownload() {
		Downloader downloader = new Downloader();

		Download download = downloader.download("http://localhost:" + helper.getPort() + "/test/myfile1.txt");
		assertEquals("x", download.getMediaType());
	}

}
