package com.themuler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Hello world!
 */
public class App {

    private static final Logger logger = LogManager.getLogger("application");

    public static void main(String[] args) {

        String value = "{\"id\":1,\"name\":\"Leanne Graham\",\"username\":\"Bret\",\"email\":\"Sincere@april.biz\",\"address\":{\"street\":\"Kulas Light\",\"suite\":\"Apt. 556\",\"city\":\"Gwenborough\",\"zipcode\":\"92998-3874\",\"geo\":{\"lat\":\"-37.3159\",\"lng\":\"81.1496\"}},\"phone\":\"1-770-736-8031 x56442\",\"website\":\"hildegard.org\",\"company\":{\"name\":\"Romaguera-Crona\",\"catchPhrase\":\"Multi-layered client-server neural-net\",\"bs\":\"harness real-time e-markets\"}}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setLenient().create();
        Object o = gson.fromJson(value, new TypeToken<Map<String, Object>>() {
        }.getType());
        logger.info(o);
        logger.info("Hello World");

    }
}
