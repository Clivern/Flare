package flare;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flare.util.FlareTestUtil;

import static flare.Flare.after;
import static flare.Flare.before;
import static flare.Flare.post;

public class BodyAvailabilityTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyAvailabilityTest.class);

    private static final String BODY_CONTENT = "the body content";
    
    private static FlareTestUtil testUtil;

    private final int HTTP_OK = 200;
    
    private static String beforeBody = null;
    private static String routeBody = null;
    private static String afterBody = null;

    @AfterClass
    public static void tearDown() {
        Flare.stop();

        beforeBody = null;
        routeBody = null;
        afterBody = null;
    }

    @BeforeClass
    public static void setup() {
        LOGGER.debug("setup()");

        testUtil = new FlareTestUtil(4567);

        beforeBody = null;
        routeBody = null;
        afterBody = null;

        before("/hello", (req, res) -> {
            LOGGER.debug("before-req.body() = " + req.body());
            beforeBody = req.body();
        });

        post("/hello", (req, res) -> {
            LOGGER.debug("get-req.body() = " + req.body());
            routeBody = req.body();
            return req.body();
        });

        after("/hello", (req, res) -> {
            LOGGER.debug("after-before-req.body() = " + req.body());
            afterBody = req.body();
        });

        Flare.awaitInitialization();
    }

    @Test
    public void testPost() throws Exception {
        FlareTestUtil.UrlResponse response = testUtil.doMethod("POST", "/hello", BODY_CONTENT);
        LOGGER.info(response.body);
        Assert.assertEquals(HTTP_OK, response.status);
        Assert.assertTrue(response.body.contains(BODY_CONTENT));

        Assert.assertEquals(BODY_CONTENT, beforeBody);
        Assert.assertEquals(BODY_CONTENT, routeBody);
        Assert.assertEquals(BODY_CONTENT, afterBody);
    }
}