package com.aaa.mybatisplus.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 配置Jackson实体
 */


/**
 * 使用官方自带的json格式类库，fastjson因为content type问题时不时控制台报错、无法直接返回二进制等问题
 */
@Component
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * 处理数组类型的null值
     */
    public static class NullArrayJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeStartArray();
                jgen.writeEndArray();
            }
        }
    }
    public static class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jgen.writeString(StringUtils.EMPTY);
        }
    }

    public static class NullPrimitiveSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            if (value==null) {
                jgen.writeNull();
            }
        }
    }

    public static class NullObjectJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeNull();
               /**
                * => "city": {}
                jgen.writeStartObject();
                jgen.writeEndObject();
                */
            }
        }
    }

    public static class MyBeanSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            //循环所有的beanPropertyWriter
            for (Object beanProperty : beanProperties) {
                BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                //判断字段的类型，如果是array，list，set则注册nullSerializer
                if (isArrayType(writer)) {
                    //给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(new NullArrayJsonSerializer());
                } else if (isStringType(writer)) {
                    writer.assignNullSerializer(new NullStringJsonSerializer());
                } else if (isPrimitiveType(writer)) {
                    writer.assignNullSerializer(new NullPrimitiveSerializer());
                } else if (isObjectType(writer)) {
                    writer.assignNullSerializer(new NullObjectJsonSerializer());
                }
            }
            return beanProperties;
        }

        /**
         * 是否是数组
         */
        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是String
         */
        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || clazz == String.class;
        }
        /**
         * 是否是 八种基本类型 包装类
         */
        private boolean isPrimitiveType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isPrimitive();
        }
        /**
         * 判断是否是对象类型
         */
        private boolean isObjectType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();

            return !clazz.isPrimitive() && !clazz.equals(String.class) &&  Object.class.isAssignableFrom(clazz);
        }



    }

    JacksonHttpMessageConverter() {
        getObjectMapper().setSerializerFactory(getObjectMapper().getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }

}
