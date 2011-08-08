package se.vgregion.test;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 8/8-11
 * Time: 09:09
 */
public class EchoBean {

    public String echo(String userId) {
        System.out.println("Echo: "+userId);
        return userId;
    }
}
