package ru.trendtech.utils.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

public class XMLSerializator {
    private static final Serializer serializer = new Persister();

    private static Logger LOGGER = LoggerFactory.getLogger(XMLSerializator.class);

//    public static void serializeToFile(String fileName, Object obj) throws IOException {
//        FileUtils.saveStringToFile(fileName, serialize(obj));
//    }

//    public static <T> T deserializeFromFileQuit(String fileName, Class<? extends T> className) {
//        T result = null;
//        try {
//            result = deserialize(className, FileUtils.loadFileAsString(fileName));
//        } catch (IOException e) {
//            LOGGER.warn("Problem on deserialize object of clazz = " + className + " from file = " + fileName + ". Problem skipped and NULL returned");
//        }
//        return result;
//    }

//    public static <T> T deserializeFromFile(String fileName, Class<? extends T> className) throws IOException {
//        return deserialize(className, FileUtils.loadFileAsString(fileName));
//    }

    public static <T> T deserialize(Class<? extends T> className, String obj) throws SerializationException {
        try {
            return serializer.read(className, obj);
        } catch (Exception e) {
            throw new SerializationException("Can't deserialize object", className.getName(), obj, e);
        }
    }

    public static String serialize(Object obj) {
        String result = null;
        try {
            StringWriter writer = new StringWriter();
            serializer.write(obj, writer);
            result = writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException("Invalid object format. Check object declaration !!", e);
        }
        return result;
    }
}