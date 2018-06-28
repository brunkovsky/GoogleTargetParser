package org.antalis;

import java.io.IOException;

public interface Writer {
    void init(String filename) throws IOException;
    void write(String path, String title, String className, String description) throws IOException;
    void close()  throws IOException;
}
