package quickrestservice.storage;

import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

import quickrestservice.models.RestServiceOperation;
import quickrestservice.models.RestServiceRequestResponse;

@SuppressWarnings("restriction")
public interface ServiceStore {
	
	public RestServiceRequestResponse get(HttpExchange he);
	
	public void add(RestServiceOperation restServiceOperation);
	
	public void delete(RestServiceOperation restServiceOperation);

	public Set<String> getURIs();

}
