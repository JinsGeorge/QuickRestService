package quickrestservice.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;

/**
 * Components of a single Http Transaction
 * @author Jins George
 *
 */
public class RestServiceOperation {
	
	private String method;
	private String path;
	private RestServiceRequest request;
	private RestServiceRequestResponse response;
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the request
	 */
	public RestServiceRequest getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(RestServiceRequest request) {
		this.request = request;
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
	
	public String getId()
	{
		if(getRequest().hasBody())
			return getRequest().getShaHex();
		else
			return DigestUtils.sha256Hex(method+path);
	}

	
	public static RestServiceRequestResponse notFound() {
		RestServiceRequestResponse notFoundResponse = new RestServiceRequestResponse();
		notFoundResponse.setStatus(HttpStatus.NOT_FOUND.value());
		return notFoundResponse;
	}
	public static RestServiceRequestResponse methodNotAllowed() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
