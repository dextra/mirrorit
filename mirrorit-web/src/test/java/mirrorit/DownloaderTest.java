package mirrorit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.mycontainer.commons.http.Request;
import com.googlecode.mycontainer.commons.http.Response;

public class DownloaderTest extends AbstractTestCase {

	@Test
	public void testDownloadQueryError() {
		Response resp = s.execute(Request
				.create("GET",
						"/maven2/com/googlecode/mycontainer/mycontainer-kernel/1.2.46/mycontainer-kernel-1.2.46.pom")
				.param("xxx", "yyy").header("Host", "repo2.maven.org"));
		assertEquals(500, resp.code());
	}

	@Test
	public void testDownloadMethodError() {
		Response resp = s.execute(Request
				.create("POST",
						"/maven2/com/googlecode/mycontainer/mycontainer-kernel/1.2.46/mycontainer-kernel-1.2.46.pom")
				.contentJson("{}").header("Host", "repo2.maven.org"));
		assertEquals(405, resp.code());
	}
}
