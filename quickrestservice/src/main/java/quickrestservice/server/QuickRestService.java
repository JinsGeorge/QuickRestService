package quickrestservice.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

public class QuickRestService {	
	
	private static final Log log = LogFactory.getLog(QuickRestService.class);
	public static void run(String[] args)
	{
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.info("Starting QuickRestService ....");
		try {
			ServiceManager serviceManager = new ServiceManager();
			serviceManager.setup();
		}
		catch(Throwable ex)
		{
			log.error("Startup failed" , ex);
			throw new IllegalStateException(ex);
		}
		
		stopWatch.stop();
		log.info("Started QuickRestService in "+stopWatch.getTotalTimeMillis() + " milliseconds");
		
	}

}
