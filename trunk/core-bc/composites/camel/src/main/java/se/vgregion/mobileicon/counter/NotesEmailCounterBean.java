package se.vgregion.mobileicon.counter;

import org.apache.camel.*;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.component.restlet.RestletConstants;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    public String getCount(final String userId, CamelContext context) {
        System.out.println("NotesEmail: " + userId);
        if (userId == null) return "";

        final String sitePassword = getSitePassword(userId, siteKey);
        if (StringUtils.isBlank(sitePassword)) return "";

        RestletEndpoint ep = context.getEndpoint("restlet://http://aida.vgregion.se/calendar" +
            ".nsf/unreadcount?openagent&userid="+userId, RestletEndpoint.class);


        System.out.println("1");
        Exchange exchange = call(ep, userId, sitePassword);

        if (exchange.getException() != null) {
            System.out.println("2");
            call(ep, userId, sitePassword);

            if (exchange.getException() != null) {
                System.out.println("3");
                call(ep, userId, sitePassword);
            }
        }

        Object reply = exchange.getOut().getBody();
        System.out.println("result: "+reply.toString());
        if (XPathBuilder.xpath("/html").matches(context, reply)) return "";

        return reply.toString();
    }

    private Exchange call(final RestletEndpoint endpoint, final String userId, final String sitePassword) {
        return template.send(endpoint,
            new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    exchange.setPattern(ExchangePattern.InOut);
                    Message inMessage = exchange.getIn();
                    inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
                    inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");

                    inMessage.setHeader(RestletConstants.RESTLET_LOGIN, userId);
                    inMessage.setHeader(RestletConstants.RESTLET_PASSWORD, sitePassword);
                    inMessage.setBody(" ");
                    System.out.println(endpoint.getUriPattern());
                }
            });
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
}
