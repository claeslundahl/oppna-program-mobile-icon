package se.vgregion.jetty.embedded;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.net.URLConnection;

/**
 * User: pabe
 * Date: 2011-08-02
 * Time: 09:16
 */
public class JettyServerTest extends TestCase {

    public void testStartServer() throws Exception {

        JettyServer server = new JettyServer();
        server.startServer();

        URL url = new URL("http://localhost:8100/test-counter");
        URLConnection connection = url.openConnection();
        connection.connect();

    }

    public void testGetRandom() throws Exception {
        JettyServer server = new JettyServer();
        server.startServer();

        URL url = new URL("http://localhost:8100/test-counter");
        URLConnection connection = url.openConnection();
        connection.connect();

        int cnt = Integer.parseInt(IOUtils.toString(connection.getInputStream()));
        assertTrue(cnt < 100);
        assertTrue(cnt >= 0);
    }



}
