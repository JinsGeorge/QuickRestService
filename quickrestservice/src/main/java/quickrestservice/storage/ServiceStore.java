package quickrestservice.storage;

import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

import quickrestservice.models.RestServiceRequestResponse;

@SuppressWarnings("restriction")
public interface ServiceStore {
	
	public RestServiceRequestResponse getRequestResponse(HttpExchange he);

	public Set<String> getURIs();

}
