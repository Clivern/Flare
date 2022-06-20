package flare.servlet;

import java.io.File;
import java.io.FileWriter;

import static flare.Flare.after;
import static flare.Flare.before;
import static flare.Flare.externalStaticFileLocation;
import static flare.Flare.get;
import static flare.Flare.halt;
import static flare.Flare.post;
import static flare.Flare.staticFileLocation;

public class MyApp implements FlareApplication {

    public static final String EXTERNAL_FILE = "externalFileServlet.html";

    static File tmpExternalFile;

    @Override
    public synchronized void init() {
        try {
            externalStaticFileLocation(System.getProperty("java.io.tmpdir"));
            staticFileLocation("/public");

            tmpExternalFile = new File(System.getProperty("java.io.tmpdir"), EXTERNAL_FILE);
            FileWriter writer = new FileWriter(tmpExternalFile);
            writer.write("Content of external file");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        before("/protected/*", (request, response) -> {
            halt(401, "Go Away!");
        });

        get("/hi", (request, response) -> {
            return "Hello World!";
        });

        get("/:param", (request, response) -> {
            return "echo: " + request.params(":param");
        });

        get("/", (request, response) -> {
            return "Hello Root!";
        });

        post("/poster", (request, response) -> {
            String body = request.body();
            response.status(201); // created
            return "Body was: " + body;
        });

        after("/hi", (request, response) -> {
            response.header("after", "foobar");
        });

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

}
