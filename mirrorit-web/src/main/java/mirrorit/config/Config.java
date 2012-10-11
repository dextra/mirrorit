package mirrorit.config;

public class Config {

	private static final Config ME = new Config();

	public static Config instance() {
		return ME;
	}

	private String storage = "/tmp/mirrorit/repo";

	private Long waitForFileTimeout = 60000l;

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Long getWaitForFileTimeout() {
		return waitForFileTimeout;
	}

	public void setWaitForFileTimeout(Long waitForFileTimeout) {
		this.waitForFileTimeout = waitForFileTimeout;
	}

}
