package mirrorit;

import org.junit.Test;

import com.googlecode.mycontainer.commons.http.Request;
import com.googlecode.mycontainer.commons.http.Response;

public class DownloaderTestManual extends AbstractTestCase {

	@Test
	public void testDownload() {
		Response resp = s.execute(Request.create("GET",
				"/maven2/com/googlecode/mycontainer/mycontainer-kernel/1.2.46/mycontainer-kernel-1.2.46.pom").header(
				"Host", "repo2.maven.org"));
		System.out.println(resp.code());
		System.out.println(resp.headers().first("Content-Length"));
		System.out.println(resp.content().mediaType());
		System.out.println(resp.content().charset());
		System.out.println(resp.content().text());
	}
}
