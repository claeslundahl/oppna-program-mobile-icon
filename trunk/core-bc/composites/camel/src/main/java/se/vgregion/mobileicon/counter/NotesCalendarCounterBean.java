package se.vgregion.mobileicon.counter;

import org.apache.camel.*;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 4/8-11
 * Time: 12:11
 */
public class NotesCalendarCounterBean {
    private int period = 1;

    private static Logger logger = LoggerFactory.getLogger(NotesCalendarCounterBean.class);

    @Autowired
    private ProducerTemplate template;

    public String getCount(final String userId, CamelContext context) throws URISyntaxException, IOException {
        if (userId == null || "".equals(userId)) return "";

        Calendar now = Calendar.getInstance();

        URI uri = new URI("http", "aida.vgregion.se", "/calendar.nsf/getinfo", "openagent&userid=" + userId + "&year="
                + getYear(now) + "&month=" + getMonth(now) + "&day=" + getDay(now) + "&period=" + getPeriod(), "");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        String reply = IOUtils.toString(httpResponse.getEntity().getContent());

        try {
            String status = XPathBuilder.xpath("/calendarItems/status/text()").evaluate(context, reply,
                    java.lang.String.class);

            if ("PROCESSED".equals(status)) {
                String res = XPathBuilder.xpath("/calendarItems/total/text()").evaluate(context, reply,
                        java.lang.String.class);
                return res;
            }

        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }

        return "-";
    }

    private int getYear(Calendar date) {
        return date.get(Calendar.YEAR);
    }

    private int getMonth(Calendar date) {
        return date.get(Calendar.MONTH) + 1;
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
