package quickrestservice.models;

public class RestServiceRequestResponse  extends Payload{

	private Integer status;
	
	
	public RestServiceRequestResponse() {
		super();
	}


	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	public void setStatus(String status) {
		this.status = Integer.valueOf(status.trim());
	}


	
	
	

}
