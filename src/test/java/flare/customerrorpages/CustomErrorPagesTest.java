package flare.customerrorpages;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import flare.CustomErrorPages;
import flare.Flare;
import flare.util.FlareTestUtil;

import static flare.Flare.get;
import static flare.Flare.internalServerError;
import static flare.Flare.notFound;

public class CustomErrorPagesTest {

    private static final String CUSTOM_NOT_FOUND = "custom not found 404";
    private static final String CUSTOM_INTERNAL = "custom internal 500";
    private static final String HELLO_WORLD = "hello world!";
    public static final String APPLICATION_JSON = "application/json";
    private static final String QUERY_PARAM_KEY = "qparkey";

    static FlareTestUtil testUtil;

    @AfterClass
    public static void tearDown() {
        Flare.stop();
    }

    @BeforeClass
    public static void setup() throws IOException {
        testUtil = new FlareTestUtil(4567);

        get("/hello", (q, a) -> HELLO_WORLD);

        get("/raiseinternal", (q, a) -> {
            throw new Exception("");
        });

        notFound(CUSTOM_NOT_FOUND);

        internalServerError((request, response) -> {
            if (request.queryParams(QUERY_PARAM_KEY) != null) {
                throw new Exception();
            }
            response.type(APPLICATION_JSON);
            return CUSTOM_INTERNAL;
        });

        Flare.awaitInitialization();
    }

    @Test
    public void testGetHi() throws Exception {
        FlareTestUtil.UrlResponse response = testUtil.doMethod("GET", "/hello", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals(HELLO_WORLD, response.body);
    }

    @Test
    public void testCustomNotFound() throws Exception {
        FlareTestUtil.UrlResponse response = testUtil.doMethod("GET", "/othernotmapped", null);
        Assert.assertEquals(404, response.status);
        Assert.assertEquals(CUSTOM_NOT_FOUND, response.body);
    }

    @Test
    public void testCustomInternal() throws Exception {
        FlareTestUtil.UrlResponse response = testUtil.doMethod("GET", "/raiseinternal", null);
        Assert.assertEquals(500, response.status);
        Assert.assertEquals(APPLICATION_JSON, response.headers.get("Content-Type"));
        Assert.assertEquals(CUSTOM_INTERNAL, response.body);
    }

    @Test
    public void testCustomInternalFailingRoute() throws Exception {
        FlareTestUtil.UrlResponse response = testUtil.doMethod("GET", "/raiseinternal?" + QUERY_PARAM_KEY + "=sumthin", null);
        Assert.assertEquals(500, response.status);
        Assert.assertEquals(CustomErrorPages.INTERNAL_ERROR, response.body);
    }

}
