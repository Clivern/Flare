package flare.examples.transformer;

import flare.ResponseTransformer;

import static flare.Flare.get;
import static flare.Flare.defaultResponseTransformer;

public class DefaultTransformerExample {

    public static void main(String args[]) {

        defaultResponseTransformer(json);

        get("/hello", "application/json", (request, response) -> {
            return new MyMessage("Hello World");
        });

        get("/hello2", "application/json", (request, response) -> {
            return new MyMessage("Hello World");
        }, model -> "custom transformer");
    }

    private static final ResponseTransformer json = new JsonTransformer();

}
