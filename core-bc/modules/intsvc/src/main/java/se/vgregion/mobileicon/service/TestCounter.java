package se.vgregion.mobileicon.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Random;

/**
 * User: pabe
 * Date: 2011-08-02
 * Time: 10:06
 */

@Path("/test-counter")
public class TestCounter {

    @POST
    @Produces("application/json")
    public String getRandom() {
        Random random = new Random();
        return random.nextInt(100) + "";
    }

}
