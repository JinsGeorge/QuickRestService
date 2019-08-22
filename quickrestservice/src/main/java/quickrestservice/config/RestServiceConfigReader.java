package quickrestservice.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.TextStringBuilder;

import quickrestservice.models.RestServiceOperation;
import quickrestservice.models.RestServiceRequest;
import quickrestservice.models.RestServiceRequestResponse;
import quickrestservice.server.RequestHandler;
import quickrestservice.storage.ServiceStoreImpl;

public class RestServiceConfigReader {

	private int port;
	private String basePath;
	private static final Log log = LogFactory.getLog(RestServiceConfigReader.class);
	private static final int POLL_INTERVAL = 5000;

	private RequestHandler requestHandler;
	private String sectionProcessing;
	
	File dataDirectory = new File(Configs.DIRECTORY);

	public static RestServiceConfigReader load() throws IOException {
		RestServiceConfigReader config = new RestServiceConfigReader();
		config.loadConfig();
		config.loadOperations();
		config.setMonitor();
		return config;
	}


	public RequestHandler getRequestHandler() {
		return requestHandler;
	}

	/**
	 * @param requestHandler the requestHandler to set
	 */
	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		log.info("Setting port to " + port);
		this.port = port;
	}


	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	private void loadConfig() throws IOException {
		File file = new File(Configs.CONFIG_FILE);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null)
			if (line.contains(Configs.PORT)) {
				String portConfig = readProperty(line);
				setPort(Integer.valueOf(portConfig.trim()));
			} else if (line.contains(Configs.BASE_PATH)) {
				String basePathConfig = readProperty(line);
				setBasePath(basePathConfig.trim());
			}

		br.close();

		if (getPort() == 0) {
			throw new IOException("Could not read port number from config.txt");
		}

	}

	private void loadOperations() throws IOException {

		setRequestHandler(new RequestHandler());
		ServiceStoreImpl storage = new ServiceStoreImpl();
		File[] templates = dataDirectory.listFiles();

		if (null == templates || templates.length == 0) {
			log.warn("No service templates found to configure");
			return;
		}

		for (File template : templates) {
			if (template.isFile() && template.getName().endsWith(Configs.TEMPLATE)) {
				storage.add(readRestServiceOperation(template));
			}
		}

		getRequestHandler().setStorage(storage);
	}
	
	private RestServiceOperation readRestServiceOperation(File template) throws IOException
	{
		BufferedReader br = null;
		RestServiceOperation operation = new RestServiceOperation();
		try {
			InputStream inputStream = new FileInputStream(template.getPath());
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			RestServiceRequestResponse response = new RestServiceRequestResponse();
			RestServiceRequest request = new RestServiceRequest();
			
			String line;
			TextStringBuilder data = new TextStringBuilder();

			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith(Configs.COMMENTS))
					continue;
				if (line.contains(Configs.REQUEST_METHOD)) {
					String method = readProperty(line);
					if (!Configs.VALID_OPERATIONS.contains(method)) {
						throw new Exception("Invalid method " + method + " in template " + template.getName());
					}
					operation.setMethod(method);
				} else if (line.contains(Configs.REQUEST_URI)) {
					String uri = readProperty(line);
					operation.setPath(perpareUri(getBasePath(), uri));
				} else if (line.contains(Configs.RESPONSE_STATUS)) {
					String status = readProperty(line);
					response.setStatus(status);
				} else if (line.contains(Configs.REQUEST_HEADER_START)) {
					setSectionProcessing(Configs.REQUEST_HEADER);
				} else if (line.contains(Configs.REQUEST_HEADER_END)) {
					setSectionProcessing(Configs.NONE);
				} else if (line.contains(Configs.REQUEST_START)) {
					setSectionProcessing(Configs.REQUEST_BODY);
				} else if (line.contains(Configs.REQUEST_END)) {
					request.setBody(data.toString());
					data.clear();
					setSectionProcessing(Configs.NONE);
				} else if (line.contains(Configs.RESPONSE_HEADER_START)) {
					setSectionProcessing(Configs.RESPONSE_HEADER);
				} else if (line.contains(Configs.RESPONSE_HEADER_END)) {
					setSectionProcessing(Configs.NONE);
				} else if (line.contains(Configs.RESPONSE_START)) {
					setSectionProcessing(Configs.RESPONSE_BODY);
				} else if (line.contains(Configs.RESPONSE_END)) {
					response.setBody(data.toString());
					data.clear();
					setSectionProcessing(Configs.NONE);
				}
				
				else if (getSectionProcessing().equals(Configs.REQUEST_HEADER)) {
					String[] prop = readProp(line);
					if(prop.length==2)
						request.getHeaders().add(prop[0], prop[1]);
				}
				
				else if (getSectionProcessing().equals(Configs.RESPONSE_HEADER)) {
					String[] prop = readProp(line);
					if(prop.length==2)
						response.getHeaders().add(prop[0], prop[1]);
				}

				else if (getSectionProcessing().equals(Configs.REQUEST_BODY) ||getSectionProcessing().equals(Configs.RESPONSE_BODY) ) {
					data.appendln(line);
				}

			}

			operation.setRequest(request);
			operation.setResponse(response);

		} catch (Exception e) {
			log.error("Exception occured while reading templates", e);
			throw new IOException("error in file " + template.getName() + "  " + e.getMessage());
		} finally {
			br.close();

		}
		return operation;
	}

	private String perpareUri(String basePath2, String uri) {
		String finalString = "";
		if (basePath2 == null) {
			if (uri.startsWith("/"))
				finalString = uri;
			else
				finalString = "/" + uri;
		} else {
			if (basePath2.endsWith("/") )
			{
				if(uri.startsWith("/"))
					finalString = basePath2.substring(0, (basePath2.length() - 1)) + uri;
				else
					finalString = basePath2 +uri;
			}
			else
			{
				if(uri.startsWith("/"))
					finalString = basePath2 +uri;
				else
					finalString = basePath2 +"/"+ uri;
			}

		}

		if (!finalString.startsWith("/"))
			finalString = "/" + finalString;

		return finalString;
	}
	
	private void setMonitor() throws IOException {
		FileAlterationObserver observer = new FileAlterationObserver(dataDirectory.getAbsolutePath());
        FileAlterationMonitor monitor = new FileAlterationMonitor(POLL_INTERVAL);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                log.info("File: " + file.getName() + " created");
                
                RestServiceOperation restServiceOperation;
				try {
					restServiceOperation = readRestServiceOperation(file);
					getRequestHandler().getStorage().add(restServiceOperation);
				} catch (IOException e) {
					log.error("Failed to read template from " + file.getName() , e);
				}
                
            }

            @Override
            public void onFileDelete(File file) {
            	 log.info("File: " + file.getName() + " deleted");
            	 RestServiceOperation restServiceOperation;
  				try {
  					restServiceOperation = readRestServiceOperation(file);
  					getRequestHandler().getStorage().delete(restServiceOperation);
  				} catch (IOException e) {
  					log.error("Failed to read template from " + file.getName() , e);
  				}
            }

            @Override
            public void onFileChange(File file) {
            	 log.info("File: " + file.getName() + " changed");
            	 RestServiceOperation restServiceOperation;
 				try {
 					restServiceOperation = readRestServiceOperation(file);
 					getRequestHandler().getStorage().add(restServiceOperation);
 				} catch (IOException e) {
 					log.error("Failed to read template from " + file.getName() , e);
 				}
            }
        };
        observer.addListener(listener);
        monitor.addObserver(observer);
        try {
			monitor.start();
		} catch (Exception e) {
			log.error("Failure in starting repository monitor" , e);
		}
		
	}

	
	private String[] readProp(String line)
	{
		return line.split(Configs._PROP);
	}

	private String readProperty(String property) {
		if (null != property && property.contains(Configs._PROP))
			return property.trim().split(Configs._PROP)[1];
		else
			return "";
	}

	public String getSectionProcessing() {
		return sectionProcessing != null ? sectionProcessing : Configs.NONE;
	}

	public void setSectionProcessing(String sectionProcessing) {
		this.sectionProcessing = sectionProcessing;
	}

	public void resetSectionProcessing() {
		this.sectionProcessing = Configs.NONE;
	}
	
	public Set<String> getURIs() {
		return getRequestHandler().getStorage().getURIs();
	}

}
