package com.inhabas.api.config.sshTunneling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SshApplicationListener implements ApplicationListener<ApplicationStartedEvent> {


    private final SshConfig sshConfig;
    private final Logger logger = LoggerFactory.getLogger(SshConfig.class);

    @Autowired
    public SshApplicationListener(SshConfig sshConfig) {
        this.sshConfig = sshConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("=======================================================================");
        sshConfig.init();
        logger.info("==========================Application Started==========================");
    }
}

// 아래처럼 설정 가능한데, 옛날방식. (톰캣과 분리되어 있을 때)
//@WebListener
//@RequiredArgsConstructor
//public class SshContextListener implements ServletContextListener {
//
//    private final SshConfig SshConfig;
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//
//        try {
//            SshConfig.init();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        SshConfig.closeSSH();
//    }
//}