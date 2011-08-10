package se.vgregion.mobileicon.counter;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 8/8-11
 * Time: 14:55
 */
public class NotesEmailCounterBean {
    private String siteKey;

    @Autowired
    private ProducerTemplate template;

    public String getCount(final String userId, CamelContext context) throws IOException, URISyntaxException {
        System.out.println("NotesEmail: " + userId);
        if (userId == null) return "";

        final String sitePassword = getSitePassword(userId, siteKey);
        if (StringUtils.isBlank(sitePassword)) return "";

        URI uri = new URI("http", "aida.vgregion.se", "/calendar.nsf/unreadcount", "openagent&userid=" + userId, "");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);

        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userId, sitePassword));
        httpClient.setCredentialsProvider(credsProvider);

        BasicHttpContext localcontext = new BasicHttpContext();

        // Generate BASIC scheme object and stick it to the local
        // execution context
        BasicScheme basicAuth = new BasicScheme();
        localcontext.setAttribute("preemptive-auth", basicAuth);

        // Add as the first request interceptor
        httpClient.addRequestInterceptor(new PreemptiveAuth(), 0);

        HttpResponse httpResponse = httpClient.execute(httpGet, localcontext);
        String reply = IOUtils.toString(httpResponse.getEntity().getContent());

        if (reply.contains("DOCTYPE")) return "";

        return reply.toString();
    }

    private String getSitePassword(String userId, String siteKey) {
        // TODO: lookup user password
        return "";
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    static class PreemptiveAuth implements HttpRequestInterceptor {
        public void process(final HttpRequest request, final HttpContext context) throws HttpException,
                IOException {

            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme avaialble yet, try to initialize it
            // preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
                            targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }
        }
    }

}
