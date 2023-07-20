package com.dahuaboke.handler.service;

import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dahua
 * @time 2023/7/17 10:44
 */
@Component
public class FileService {

    @Autowired
    private ObjectMapper objectMapper;
    private List<Map<String, JsonFileObject>> jsonFileObjectList = new ArrayList();

    public FileService(SpringProperties springProperties) {
        String[] datafileName = springProperties.getDatafileName();
        loadFile(datafileName);
    }

    public void loadFile(String[] filePaths) {
        Arrays.stream(filePaths).forEach(f -> {
            loadFile(f);
        });
    }

    public void loadFile(String filePath) {
        Map<String, JsonFileObject> jsonMap = new HashMap();
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);
        List<JsonFileObject> jsons = null;
        try {
            jsons = objectMapper.readValue(inputStream, new TypeReference<List<JsonFileObject>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsons.forEach(j -> {
            String url = j.getUri();
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            jsonMap.put(url, j);
        });
        jsonFileObjectList.add(jsonMap);
    }

    public JsonFileObject getObjByUri(String uri) {
        AtomicReference<JsonFileObject> jsonFileObject = null;
        jsonFileObjectList.forEach(l -> {
            jsonFileObject.set(l.get(uri));
        });
        return jsonFileObject.get();
    }
}
