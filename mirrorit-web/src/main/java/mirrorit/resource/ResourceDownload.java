package mirrorit.resource;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;

public class ResourceDownload extends Resource {

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public String getEncode() {
		return null;
	}

	@Override
	public String getLength() {
		return null;
	}

	public void start(HttpGet get) {
		
	}

}
