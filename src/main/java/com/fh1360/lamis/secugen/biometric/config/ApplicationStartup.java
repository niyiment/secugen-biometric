
package com.fh1360.lamis.secugen.biometric.config;

import java.util.logging.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    
    private static final Logger logger = Logger.getLogger(ApplicationStartup.class.getName());
    
    boolean alreadySetup = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {

        if (alreadySetup) {
            return;
        }
        
        logger.info("Application Starting...");
        alreadySetup = true;        
    }
    
}
