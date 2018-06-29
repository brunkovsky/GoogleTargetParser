package org.antalis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtWriter implements Writer {

    private BufferedWriter bufferedWriter;

    TxtWriter(String filename) {
        try {
            File resultFile = new File(filename);
            FileWriter fileWriter = new FileWriter(resultFile.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String path, String title, String className, String description) throws IOException {
        bufferedWriter.write(path + "\t" + title + "\t" + className + "\t" + description + "\r\n");
    }

    @Override
    public void close() throws IOException {
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
