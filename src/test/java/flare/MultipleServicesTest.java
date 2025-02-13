/*
 * Copyright 2016 - Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package flare;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import flare.route.HttpMethod;
import flare.routematch.RouteMatch;
import flare.util.FlareTestUtil;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static flare.Service.ignite;

/**
 * Created by Per Wendel on 2016-02-18.
 */
public class MultipleServicesTest {

    private static Service first;
    private static Service second;

    private static FlareTestUtil firstClient;
    private static FlareTestUtil secondClient;

    @BeforeClass
    public static void setup() throws Exception {
        firstClient = new FlareTestUtil(4567);
        secondClient = new FlareTestUtil(1234);

        first = igniteFirstService();
        second = igniteSecondService();

        first.awaitInitialization();
        second.awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        first.stop();
        second.stop();
    }

    @Test
    public void testGetHello() throws Exception {
        FlareTestUtil.UrlResponse response = firstClient.doMethod("GET", "/hello", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("Hello World!", response.body);
    }

    @Test
    public void testGetRedirectedHi() throws Exception {
        FlareTestUtil.UrlResponse response = secondClient.doMethod("GET", "/hi", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("Hello World!", response.body);
    }

    @Test
    public void testGetUniqueForSecondWithFirst() throws Exception {
        FlareTestUtil.UrlResponse response = firstClient.doMethod("GET", "/uniqueforsecond", null);
        Assert.assertEquals(404, response.status);
    }

    @Test
    public void testGetUniqueForSecondWithSecond() throws Exception {
        FlareTestUtil.UrlResponse response = secondClient.doMethod("GET", "/uniqueforsecond", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("Bompton", response.body);
    }

    @Test
    public void testStaticFileCssStyleCssWithFirst() throws Exception {
        FlareTestUtil.UrlResponse response = firstClient.doMethod("GET", "/css/style.css", null);
        Assert.assertEquals(404, response.status);
    }

    @Test
    public void testStaticFileCssStyleCssWithSecond() throws Exception {
        FlareTestUtil.UrlResponse response = secondClient.doMethod("GET", "/css/style.css", null);
        Assert.assertEquals(200, response.status);
        Assert.assertEquals("Content of css file", response.body);
    }

    @Test
    public void testGetAllRoutesFromBothServices(){
        for(RouteMatch routeMatch : first.routes()){
            Assert.assertEquals(routeMatch.getAcceptType(), "*/*");
            Assert.assertEquals(routeMatch.getHttpMethod(), HttpMethod.get);
            Assert.assertEquals(routeMatch.getMatchUri(), "/hello");
            Assert.assertEquals(routeMatch.getRequestURI(), "ALL_ROUTES");
            Assert.assertThat(routeMatch.getTarget(), instanceOf(RouteImpl.class));
        }

        for(RouteMatch routeMatch : second.routes()){
            Assert.assertEquals(routeMatch.getAcceptType(), "*/*");
            Assert.assertThat(routeMatch.getHttpMethod(), instanceOf(HttpMethod.class));
            boolean isUriOnList = ("/hello/hi/uniqueforsecond").contains(routeMatch.getMatchUri());
            Assert.assertTrue(isUriOnList);
            Assert.assertEquals(routeMatch.getRequestURI(), "ALL_ROUTES");
            Assert.assertThat(routeMatch.getTarget(), instanceOf(RouteImpl.class));
        }
    }

    private static Service igniteFirstService() {

        Service http = ignite(); // I give the variable the name 'http' for the code to make sense when adding routes.

        http.get("/hello", (q, a) -> "Hello World!");

        return http;
    }

    private static Service igniteSecondService() {

        Service http = ignite()
                .port(1234)
                .staticFileLocation("/public")
                .threadPool(40);

        http.get("/hello", (q, a) -> "Hello World!");
        http.get("/uniqueforsecond", (q, a) -> "Bompton");

        http.redirect.any("/hi", "/hello");

        return http;
    }


}
