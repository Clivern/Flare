package flare.examples.templateview;

import java.util.HashMap;
import java.util.Map;

import static flare.Flare.get;
import static flare.Flare.modelAndView;

public class FreeMarkerExample {

    public static void main(String args[]) {

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello FreeMarker World");

            // The hello.ftl file is located in directory:
            // src/test/resources/flare/examples/templateview/freemarker
            return modelAndView(attributes, "hello.ftl");
        }, new FreeMarkerTemplateEngine());

    }

}