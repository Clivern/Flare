package flare;

import org.junit.Assert;
import org.junit.Test;

import flare.util.FlareTestUtil;

import static flare.Flare.awaitInitialization;
import static flare.Flare.get;
import static flare.Flare.unmap;

public class UnmapTest {

    FlareTestUtil testUtil = new FlareTestUtil(4567);

    @Test
    public void testUnmap() throws Exception {
        get("/tobeunmapped", (q, a) -> "tobeunmapped");
        awaitInitialization();

        FlareTestUtil.UrlResponse response = testUtil.doMethod("GET", "/tobeunmapped", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("tobeunmapped", response.body);

        unmap("/tobeunmapped");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        Assert.assertEquals(404, response.status);

        get("/tobeunmapped", (q, a) -> "tobeunmapped");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("tobeunmapped", response.body);

        unmap("/tobeunmapped", "get");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        Assert.assertEquals(404, response.status);
    }
}
