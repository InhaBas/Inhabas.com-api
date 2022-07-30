package com.inhabas.api.config.sshTunneling;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

import static java.lang.System.exit;

/**
 * 로컬 개발용 ssh 터널링 설정. profile 이 local 일때만 유효
 */
@Profile("local")
@ConfigurationProperties(prefix = "ssh")
@Setter @Getter
public class SshConfig {

    private String remoteHost;
    private String user;
    private String password;
    private String privateKey;
    private int sshPort;
    private int internalForwardingPort;

    private Session session;

    public void closeSSH() {
        session.disconnect();
    }

    public void init() {

        Logger logger = LoggerFactory.getLogger(SshConfig.class);

        try {
            logger.info("{}@{}:{}:{} with privateKey",user, remoteHost, sshPort, internalForwardingPort);

            logger.info("start ssh tunneling..");
            JSch jSch = new JSch();

            logger.info("creating ssh session");
            jSch.addIdentity(privateKey);
            session = jSch.getSession(user, remoteHost, sshPort);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            logger.info("complete creating ssh session");

            logger.info("start connecting ssh connection");
            session.connect();
            logger.info("success connecting ssh connection ");

            logger.info("start forwarding");
            session.setPortForwardingL(0, remoteHost, internalForwardingPort);
            logger.info("successfully connected to database");

        } catch (Exception e){
            logger.error("fail to make ssh tunneling");
            e.printStackTrace();
            exit(1);
        }
    }
}

