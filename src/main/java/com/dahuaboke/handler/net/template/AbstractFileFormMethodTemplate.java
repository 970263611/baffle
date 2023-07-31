package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.BaffleConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/27 17:59
 */
public abstract class AbstractFileFormMethodTemplate extends AbstractMethodTemplate {

    public AbstractFileFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    protected MultipartBody.Builder buildMultipartBody(Map<String, String> headers, String body) {
        String contentType = headers.get(headerName().toLowerCase());
        String boundary = null;
        for (String s : contentType.split(";")) {
            if (s.contains(BaffleConst.FORM_SPLIT)) {
                boundary = s.split(BaffleConst.FORM_SPLIT)[1];
            }
        }
        if (boundary == null) {
            throw new IllegalArgumentException(BaffleConst.EXCEPTION_ERROR_PARAM_MESSAGE);
        }
        String[] split = body.split(boundary);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        for (int a = 1; a < split.length - 1; a++) {
            String str = split[a];
            String param = str.substring(2, str.length() - 4);
            String[] p = param.split("\r\n\r\n");
            String head = p[0];
            if (head.contains(BaffleConst.STR_FILENAME)) {
                String[] headAry = head.split("\r\n");
                String[] names = headAry[0].split(";");
                String name = null, filename = null;
                for (String s : names) {
                    if (s.contains(BaffleConst.STR_FILENAME)) {
                        filename = s.split(BaffleConst.STR_FILENAME + "=")[1];
                        filename = filename.substring(1, filename.length() - 1);
                    } else if (s.contains(BaffleConst.STR_NAME)) {
                        name = s.split(BaffleConst.STR_NAME + "=")[1];
                        name = name.substring(1, name.length() - 1);
                    }
                }
                String fileContentType = headAry[1];
                File dir = new File(BaffleConst.BAFFLE_BASE_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(BaffleConst.BAFFLE_BASE_DIR + filename);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    String content = param.substring(p[0].length() + 4);
                    fos.write(content.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                }
                RequestBody fileBody = RequestBody.create(MediaType.parse(fileContentType), file);
                multipartBody.addFormDataPart(name, filename, fileBody);
            } else {
                String name = head.split("=")[1];
                name = name.substring(1, name.length() - 1);
                multipartBody.addFormDataPart(name, p[1]);
            }
        }
        return multipartBody;
    }
}
