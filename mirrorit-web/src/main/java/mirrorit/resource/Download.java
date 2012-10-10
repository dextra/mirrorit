package mirrorit.resource;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.methods.HttpGet;

public class Download implements Future<Resource> {

	private final HttpGet request;

	private File file;

	public Download(HttpGet request) {
		this.request = request;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new RuntimeException("not supported");
	}

	@Override
	public boolean isCancelled() {
		throw new RuntimeException("not supported");
	}

	@Override
	public boolean isDone() {
		throw new RuntimeException("not supported");
	}

	@Override
	public Resource get() throws InterruptedException, ExecutionException {
		try {
			return get(60, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized Resource get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
			TimeoutException {
		if (file == null) {
			wait(unit.toMillis(timeout));
		}
		return null;
	}

	public HttpGet getRequest() {
		return request;
	}

	@Override
	public String toString() {
		return request == null ? "null" : request.getURI().toString();
	}

	public synchronized void ready(File file) {
		this.file = file;
		notifyAll();
	}

}
