package com.zhangry.demo.web.http.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 * Created by zhangry on 2017/3/28.
 */
public class MappingFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private SerializerFeature[] serializerFeature;

    public SerializerFeature[] getSerializerFeature() {
        return this.serializerFeature;
    }

    public void setSerializerFeature(SerializerFeature[] serializerFeature) {
        this.serializerFeature = serializerFeature;
    }

    public MappingFastJsonHttpMessageConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(this.convertStreamToString(inputMessage.getBody()), clazz);
    }

    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String jsonString = JSON.toJSONString(o, this.serializerFeature);
        OutputStream out = outputMessage.getBody();
        out.write(jsonString.getBytes(DEFAULT_CHARSET));
        out.flush();
    }

    public String convertStreamToString(InputStream is) throws IOException {
        if(is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                int n;
                while((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }

            return writer.toString();
        } else {
            return "";
        }
    }
}

