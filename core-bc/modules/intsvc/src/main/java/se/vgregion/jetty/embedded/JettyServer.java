package se.vgregion.jetty.embedded;

import org.mortbay.jetty.Server;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: pabe
 * Date: 2011-08-02
 * Time: 09:58
 */
public class JettyServer {

    private Server server;

    public JettyServer() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans-jetty.xml");
        server = (Server) context.getBean("server");
    }

    public void startServer() throws Exception {
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }
}
