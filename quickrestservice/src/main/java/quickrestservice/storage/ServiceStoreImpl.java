package quickrestservice.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;

import quickrestservice.models.RestServiceOperation;
import quickrestservice.models.RestServiceRequestResponse;

@SuppressWarnings("restriction")
public class ServiceStoreImpl  implements ServiceStore{
	
	private HashMap<String, OperationStore> serviceStore;
	
	public ServiceStoreImpl()
	{
		this.serviceStore = new HashMap<>();
	}
	
	public void addOperationStore(String key, OperationStore os) {
		getServiceStore().put(key, os);
	}
	
	
	public Set<String> getURIs() {
		return getServiceStore().keySet();
	}
	public OperationStore getOperationStore(String key) {
		return getServiceStore().get(key);
	}
	
	public boolean operationExists(String key)
	{
		if(getServiceStore().containsKey(key))
			return true;
		else
			return false;
	}

	/**
	 * @return the serviceStore
	 */
	public HashMap<String, OperationStore> getServiceStore() {
		return serviceStore;
	}

	/**
	 * @param serviceStore the serviceStore to set
	 */
	public void setServiceStore(HashMap<String, OperationStore> serviceStore) {
		this.serviceStore = serviceStore;
	}

	public RestServiceRequestResponse getRequestResponse(HttpExchange he) {
		URI uri = he.getRequestURI();
		String path = uri.getPath();
		if(operationExists(path))
		{
			 OperationStore ops = getOperationStore(path);
			 String method = he.getRequestMethod().toLowerCase();
			 if(ops.methodExists(method))
			 {
				TransactionStore ts = ops.getTransactionStore(method);
				if(method.equals("get"))
				{
					String key = DigestUtils.sha256Hex(method+path);
					return ts.get(key);
				}
				else
				{
					InputStream inputStream = he.getRequestBody();
					String incomingRequest = "";
					try {
						incomingRequest = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					} catch (IOException e) {						
						e.printStackTrace();
					}
					String newVal = incomingRequest.trim().replaceAll("\r\n", "").replaceAll("\\s+", "");
					String key = DigestUtils.sha256Hex(newVal);
					return ts.get(key);
				}
			 }
			 else
			 {
				 return RestServiceOperation.methodNotAllowed();
			 }
			 
		}
		else
		{
			return RestServiceOperation.notFound();
		}
	}
	
	public void addServiceToStorage(RestServiceOperation restServiceOperation)
	{
		if(operationExists(restServiceOperation.getPath()))
		{
			OperationStore os =  getOperationStore(restServiceOperation.getPath());
			os.addTransaction(restServiceOperation);
		}
		else
		{
			OperationStore os = new OperationStore();
			os.addTransaction(restServiceOperation);
			addOperationStore(restServiceOperation.getPath(), os);
		}
	}

	
}
