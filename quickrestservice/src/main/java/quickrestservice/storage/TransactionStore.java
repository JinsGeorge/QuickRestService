package quickrestservice.storage;

import java.util.LinkedHashMap;

import quickrestservice.models.RestServiceOperation;
import quickrestservice.models.RestServiceRequestResponse;

public class TransactionStore {

	private LinkedHashMap<String, RestServiceOperation> transactions;

	public TransactionStore()
	{
		this.transactions = new LinkedHashMap<>();
	}

	public RestServiceRequestResponse get(String key)
	{
		RestServiceOperation operation = getTransactions().get(key);
		if(null!=operation)
		{
			return operation.getResponse();
		}
		else
		{
			return RestServiceOperation.notFound();
		}
	}

	public void addTransaction(RestServiceOperation operation)
	{
		getTransactions().put(operation.getId(), operation);
	}

	/**
	 * @return the transactions
	 */
	public LinkedHashMap<String, RestServiceOperation> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(LinkedHashMap<String, RestServiceOperation> transactions) {
		this.transactions = transactions;
	}

	public void removeTransaction(RestServiceOperation restServiceOperation) {
		getTransactions().remove(restServiceOperation.getId());	
	}


}
