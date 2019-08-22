package quickrestservice.storage;

import java.util.HashMap;

import quickrestservice.models.RestServiceOperation;

public class OperationStore {
	
	private HashMap<String, TransactionStore> operationStore;
	
	public OperationStore()
	{
		this.operationStore = new HashMap<>();
	}
	
	public void addTransactions(String method, TransactionStore ts) {
		getOperationStore().put(method.toLowerCase(), ts);
	}
	
	public TransactionStore getTransactionStore(String key)
	{
		return getOperationStore().get(key);
	}

	public HashMap<String, TransactionStore> getOperationStore() {
		return operationStore;
	}

	public void setOperationStore(HashMap<String, TransactionStore> operationStore) {
		this.operationStore = operationStore;
	}

	public boolean methodExists(String lowerCase) {
		
		if( getOperationStore().containsKey(lowerCase))
		 	return true;
		else
			return false;
		
	}

	public void addTransaction(RestServiceOperation restServiceOperation) {
		
		if(methodExists(restServiceOperation.getMethod()))
		{
			TransactionStore ts = getTransactionStore(restServiceOperation.getMethod());
			ts.addTransaction(restServiceOperation);
		}
		else
		{
			TransactionStore ts = new TransactionStore();
			ts.addTransaction(restServiceOperation);
			addTransactions(restServiceOperation.getMethod(), ts);		
		}
		
	}

	public void removeTransaction(RestServiceOperation restServiceOperation) {
		if(methodExists(restServiceOperation.getMethod()))
		{
			TransactionStore ts = getTransactionStore(restServiceOperation.getMethod());
			ts.removeTransaction(restServiceOperation);
		}
		
	}
	
	

}
