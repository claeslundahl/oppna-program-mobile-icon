package se.vgregion.mobileicon.counter;

import org.apache.camel.*;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 4/8-11
 * Time: 12:11
 */
public class NotesCalendarCounterBean {

    @Autowired
    private ProducerTemplate template;

    public String getCount(final String userId, CamelContext context) {
        System.out.println("NotesCalendar: " + userId);
        if (userId == null) return "";

        RestletEndpoint ep = context.getEndpoint("restlet://http://aida.vgregion.se", RestletEndpoint.class);
        ep.setUriPattern("/calendar.nsf/getinfo?openagent&userid="+userId+"&year=2011&month=6&day=1&period=3");

        System.out.println("1");
        Exchange exchange = call(ep);

        if (exchange.getException() != null) {
            System.out.println("2");
            exchange = call(ep);

            if (exchange.getException() != null) {
                System.out.println("3");
                exchange = call(ep);
            }
        }

        Object reply = exchange.getOut().getBody();

        try {
            String status = XPathBuilder.xpath("/calendarItems/status/text()").evaluate(context, reply,
                    java.lang.String.class);
            System.out.println(status);

            if ("PROCESSED".equals(status)) {
                String res = XPathBuilder.xpath("/calendarItems/total/text()").evaluate(context, reply, java.lang.String.class);
                System.out.println("Result: " + res);
                return res;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "-";
    }

    private Exchange call(final RestletEndpoint endpoint) {
        return template.send(endpoint, new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    exchange.setPattern(ExchangePattern.InOut);
                    exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
                    exchange.getIn().setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");
                    System.out.println(endpoint.getUriPattern());
                }
            });
    }
}
