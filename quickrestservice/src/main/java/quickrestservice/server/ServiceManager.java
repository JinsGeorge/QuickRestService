package quickrestservice.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.net.httpserver.HttpServer;

import quickrestservice.config.RestServiceConfigReader;

@SuppressWarnings("restriction")
public class ServiceManager {

	private HttpServer httpServer;
	private static final Log log = LogFactory.getLog(ServiceManager.class);

	public void setup() {
		try {
			setHttpServer(HttpServer.create());
			loadOperations();
			getHttpServer().start();
		} catch (Throwable tx) {
			log.error("failed to start server", tx);
			throw new RuntimeException(tx);
		}

	}

	/**
	 * provide rest operations to the server and assign request handler
	 * 
	 * @throws IOException
	 */
	public void loadOperations() throws IOException {
		RestServiceConfigReader config = RestServiceConfigReader.load();
		log.info("Setting server on port " + config.getPort());
		getHttpServer().bind(new InetSocketAddress(config.getPort()), 1);

		for (String uri : config.getURIs()) {
			getHttpServer().createContext(uri, config.getRequestHandler());
		}

	}

	/**
	 * @return the httpServer
	 */
	public HttpServer getHttpServer() {
		return httpServer;
	}

	/**
	 * @param httpServer the httpServer to set
	 */
	public void setHttpServer(HttpServer httpServer) {
		this.httpServer = httpServer;
	}

}
