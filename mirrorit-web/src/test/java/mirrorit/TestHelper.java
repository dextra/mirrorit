package mirrorit;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.mycontainer.kernel.ShutdownCommand;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.FilterDesc;
import com.googlecode.mycontainer.web.WebServerDeployer;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class TestHelper {

	private static final Logger LOG = LoggerFactory.getLogger(TestHelper.class);

	private int port = 8580;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ContainerBuilder bootMycontainer() throws Exception {
		System.setProperty("mycontainer", "true");
		ContainerBuilder builder = new ContainerBuilder();
		builder.deployVMShutdownHook();

		WebServerDeployer server = builder.createDeployer(JettyServerDeployer.class);
		server.setName("WebServer");
		server.bindPort(port);

		ContextWebServer web = server.createContextWebServer();
		web.setContext("/");
		web.setResources("src/main/webapp");

		web.getFilters().add(new FilterDesc(LogFilter.class, "/*"));
		server.deploy();

		return builder;
	}

	public void shutdownMycontainer() {
		try {
			ShutdownCommand shutdown = new ShutdownCommand();
			shutdown.setContext(new InitialContext());
			shutdown.shutdown();
		} catch (Exception e) {
			LOG.error("error", e);
		}
	}

}
