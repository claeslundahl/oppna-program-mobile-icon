package se.vgregion.mobileicon.counter;

import org.apache.camel.*;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 4/8-11
 * Time: 12:11
 */
public class NotesCalendarCounterBean {
    private int period = 1;

    @Autowired
    private ProducerTemplate template;

    public String getCount(final String userId, CamelContext context) throws URISyntaxException, IOException {
        System.out.println("NotesCalendar: " + userId);
        if (userId == null || "".equals(userId)) return "";


        Calendar now = Calendar.getInstance();
        RestletEndpoint endpoint = context.getEndpoint("restlet://http://aida.vgregion.se", RestletEndpoint.class);
        endpoint.setUriPattern("/calendar.nsf/getinfo?openagent&userid=" + userId + "&year=" + getYear(now)
                + "&month=" + getMonth(now) + "&day=" + getDay(now) + "&period=" + getPeriod());

        System.out.println("1");
        URI uri = new URI("http", "aida.vgregion.se", "/calendar.nsf/getinfo", "openagent&userid=" + userId + "&year=" + getYear(now)
                + "&month=" + getMonth(now) + "&day=" + getDay(now) + "&period=" + getPeriod(), "");

        URL url = uri.toURL();

        String response = IOUtils.toString(url.openStream());
        System.out.println("URL Response: " + response);

        Exchange exchange = call(endpoint);


        if (exchange.getException() != null) {
            exchange.getException().printStackTrace();
            System.out.println("2");
            exchange = call(endpoint);

            if (exchange.getException() != null) {
                exchange.getException().printStackTrace();
                System.out.println("3");
                exchange = call(endpoint);
            }
        }

        Object reply = exchange.getOut().getBody();

        System.out.println(reply);

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
        Exchange exchange = template.send(endpoint, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.setPattern(ExchangePattern.InOut);
                exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
                exchange.getIn().setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");
                System.out.println(endpoint.getUriPattern());
            }
        });
        return exchange;
    }

    private int getYear(Calendar date) {
        return date.get(Calendar.YEAR);
    }

    private int getMonth(Calendar date) {
        return date.get(Calendar.MONTH)+1;
    }

    private int getDay(Calendar date) {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
