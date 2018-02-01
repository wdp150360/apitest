package common;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private static final Map<String, String> ENV_AND_URL = new HashMap<String, String>() {
        {
        	put("test", "https://appgw-test.fanbalife.com/");
//        	put("pre", "https://appgw-test.fanbalife.com/");
        }
    };

    public static String getUrl(String env) {
        return ENV_AND_URL.get(env);
    }
    
}
