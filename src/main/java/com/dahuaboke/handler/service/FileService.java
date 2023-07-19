package com.dahuaboke.handler.service;

import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/17 10:44
 */
@Component
public class FileService {

    private Map<String, JsonFileObject> jsonMap = new HashMap();

    public FileService() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
        try {
            List<JsonFileObject> jsons = objectMapper.readValue(inputStream, new TypeReference<List<JsonFileObject>>() {
            });
            jsons.forEach(j -> {
                String url = j.getUri();
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                jsonMap.put(url, j);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonFileObject getObjByUri(String uri) {
        return jsonMap.get(uri);
    }
}
