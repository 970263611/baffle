package com.dahuaboke.handler.service;

import com.dahuaboke.model.BaffleConst;
import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dahua
 * @time 2023/7/17 10:44
 */
@Component
public class FileService {

    private List<Map<String, JsonFileObject>> jsonFileObjectList = new ArrayList();

    public FileService(SpringProperties springProperties, ObjectMapper objectMapper) {
        String[] datafileName = springProperties.getDataFilename();
        loadFile(datafileName, objectMapper);
    }

    public void loadFile(String[] filePaths, ObjectMapper objectMapper) {
        Arrays.stream(filePaths).forEach(f -> {
            loadFile(f, objectMapper);
        });
    }

    public void loadFile(String filePath, ObjectMapper objectMapper) {
        Map<String, JsonFileObject> jsonMap = new HashMap();
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);
        List<JsonFileObject> jsons;
        try {
            jsons = objectMapper.readValue(inputStream, new TypeReference<List<JsonFileObject>>() {
            });
        } catch (Exception e) {
            return;
        }
        jsons.forEach(j -> {
            String url = j.getUri();
            if (!url.startsWith(BaffleConst.SYMBOL_SLASH)) {
                url = BaffleConst.SYMBOL_SLASH + url;
            }
            jsonMap.put(url, j);
        });
        jsonFileObjectList.add(jsonMap);
    }

    public JsonFileObject getObjByUri(String uri) {
        AtomicReference<JsonFileObject> jsonFileObject = new AtomicReference();
        jsonFileObjectList.forEach(l -> {
            jsonFileObject.set(l.get(uri));
        });
        return jsonFileObject.get();
    }
}
