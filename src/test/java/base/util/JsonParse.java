package base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.internal.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;


public final class JsonParse {
    private static final Configuration conf;

    static {
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider();

//            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

//            @Override
            public MappingProvider mappingProvider() {
                return new JacksonMappingProvider();
            }

//            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        conf = Configuration.defaultConfiguration();
    }

    public static Boolean hasKey(String key, String jsonString) {
        JsonNode rootNode = getJsonRootNode(jsonString);
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            if (field.getKey().equals(key)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static List<String> getValues(String key, String jsonString) {
        List<String> values = new ArrayList<String>();
        JsonNode rootNode = getJsonRootNode(jsonString);
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            if (field.getKey().equals(key)) {
                values.add(field.getValue().asText());
            }
        }
        return values;
    }

    public static JsonNode getJsonChildNode(String fieldName, String jsonString) {
        JsonNode rootNode = getJsonRootNode(jsonString);
        return rootNode.findValue(fieldName);
    }

    private static JsonNode getJsonRootNode(String jsonString) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootNode;
    }

    public static Object getJsonXPathResult(String jsonPath, String jsonString) {
        return JsonPath.using(conf).parse(jsonString).read(jsonPath);
    }
}
