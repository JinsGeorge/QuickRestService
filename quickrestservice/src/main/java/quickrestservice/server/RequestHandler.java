package quickrestservice.server;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import quickrestservice.models.RestServiceRequestResponse;
import quickrestservice.storage.ServiceStore;
import quickrestservice.storage.ServiceStoreImpl;

@SuppressWarnings("restriction")
public class RequestHandler implements HttpHandler{
	
	private static final Log log = LogFactory.getLog(RequestHandler.class);
	RestServiceRequestResponse response;
	
	ServiceStore storage;

	/**
	 * @return the storage
	 */
	public ServiceStore getStorage() {
		return storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(ServiceStoreImpl storage) {
		this.storage = storage;
	}

	/**
	 * @return the response
	 */
	public RestServiceRequestResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(RestServiceRequestResponse response) {
		this.response = response;
	}

	public void handle(HttpExchange he) throws IOException {
	
		log.info("Received request " );
		setResponse(storage.getRequestResponse(he));
		if(getResponse().hasHeaders())
			he.getResponseHeaders().putAll(getResponse().getHeaders());
		he.sendResponseHeaders(getResponse().getStatus(), getResponse().getBody().length());
		OutputStream os = he.getResponseBody();
        os.write(getResponse().getBytes());
        os.close();	
        log.info("Completed request " );
		
	}

}
