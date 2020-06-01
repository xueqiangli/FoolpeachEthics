package com.skyzone.foolpeachethics.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skyzone on 2/17/2017.
 */

public class ArgsGenerator {

    public static Map<String, String> sendArgs() {
        return new HashMap<String, String>() {{
            put("text", "jax_demo");
        }};
    }
}
