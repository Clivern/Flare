package flare;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public final void test_encode() {
        String in = "hello";
        String encode = Base64.encode(in);
        Assert.assertFalse(in.equals(encode));
    }

    @Test
    public final void test_decode() {
        String in = "hello";
        String encode = Base64.encode(in);
        String decode = Base64.decode(encode);

        Assert.assertTrue(in.equals(decode));
    }

    @Test
    public final void testEncodeNull() {
        String in = null;
        String encode = Base64.encode(in);
        Assert.assertNull(encode);
    }

    @Test
    public final void testDecodeNull() {
        String in = null;
        String decode = Base64.decode(in);
        Assert.assertNull(decode);
    }
}
