package ru.trendtech.utils.xml;

import java.io.IOException;

public class SerializationException extends IOException {
    private final String className;

    private final String content;

    public SerializationException(String message, String className, String content, Throwable cause) {
        super(message, cause);
        this.className = className;
        this.content = content;
    }

    public String getClassName() {
        return className;
    }

    public String getContent() {
        return content;
    }
}
