package quickrestservice.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;

public class Payload {
	
	private String body;
	private HttpHeaders headers;
	

	public Payload() {
		this.headers = new HttpHeaders();
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
    public boolean hasBody()
    {
    	return body!=null?true:false;
    }
    
    public boolean hasHeaders()
    {
    	return headers.isEmpty()?false:true;
    }
    
    /**
	 * @return the body in byte array
	 */
	public byte[] getBytes() {
		return body.getBytes();
	}
	
	public String getShaHex()
	{
		String val = getBody().trim();
		String newVal = val.replaceAll("\r\n", "").replaceAll("\\s+", "");
		return DigestUtils.sha256Hex(newVal);
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders httpHeaders) {
		this.headers = httpHeaders;
	}

}
