package com.ycbjie.library.http;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/6/6
 *     desc  : json工具类
 *     revise:
 * </pre>
 */
public class JsonUtils {


    private static Gson gson;

    /**
     * 第一种方式
     * @return              Gson对象
     */
    public static Gson getJson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    /**
     * 第二种方式
     * @return              Gson对象
     */
    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()// json宽松
                    .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                    .serializeNulls() //智能null
                    .setPrettyPrinting()// 调教格式
                    .disableHtmlEscaping() //默认是GSON把HTML转义的
                    .registerTypeAdapter(int.class, new JsonDeserializer<Integer>() {
                        //根治服务端int 返回""空字符串
                        @Override
                        public Integer deserialize(JsonElement json, Type typeOfT,
                                                   JsonDeserializationContext context)
                                throws JsonParseException {
                            //try catch不影响效率
                            try {
                                return json.getAsInt();
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        }
                    })
                    .create();
        }
        return gson;
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ParamNames {
        String value();
    }


}
