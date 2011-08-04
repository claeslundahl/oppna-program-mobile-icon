package se.vgregion.test;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 4/8-11
 * Time: 11:40
 */
public class RandomBean {
    public String getRandom() {
        Random r = new Random();
        return (r.nextInt(100) * -1)+"";
    }
}
