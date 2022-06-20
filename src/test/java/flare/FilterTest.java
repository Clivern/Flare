package flare;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import flare.util.FlareTestUtil;
import flare.util.FlareTestUtil.UrlResponse;

import static flare.Flare.awaitInitialization;
import static flare.Flare.before;
import static flare.Flare.stop;

public class FilterTest {
    static FlareTestUtil testUtil;

    @AfterClass
    public static void tearDown() {
        stop();
    }

    @BeforeClass
    public static void setup() throws IOException {
        testUtil = new FlareTestUtil(4567);

        before("/justfilter", (q, a) -> System.out.println("Filter matched"));
        awaitInitialization();
    }

    @Test
    public void testJustFilter() throws Exception {
        UrlResponse response = testUtil.doMethod("GET", "/justfilter", null);

        System.out.println("response.status = " + response.status);
        Assert.assertEquals(404, response.status);
    }

}
