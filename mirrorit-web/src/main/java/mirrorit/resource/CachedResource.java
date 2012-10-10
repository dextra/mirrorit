package mirrorit.resource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CachedResource implements Future<Resource> {

	private final Resource resource;

	public CachedResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new RuntimeException("not supported");
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Resource get() throws InterruptedException, ExecutionException {
		return this.resource;
	}

	@Override
	public Resource get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return get();
	}

}
