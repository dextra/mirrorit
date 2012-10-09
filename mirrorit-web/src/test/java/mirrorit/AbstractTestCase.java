package mirrorit;

import org.junit.After;
import org.junit.Before;

import com.googlecode.mycontainer.commons.http.HttpClientRequestService;
import com.googlecode.mycontainer.commons.http.RequestAdapter;
import com.googlecode.mycontainer.commons.http.RequestService;

public class AbstractTestCase {

	protected TestHelper helper;

	protected RequestService s;

	protected RequestAdapter a;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.bootMycontainer();

		s = new HttpClientRequestService("http://localhost:8580");
		a = new RequestAdapter(s);
	}

	@After
	public void tearDown() {
		if (helper != null) {
			helper.shutdownMycontainer();
		}
	}

	public static long now() {
		try {
			Thread.sleep(2l);
			long ret = System.currentTimeMillis();
			Thread.sleep(2l);
			return ret;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
