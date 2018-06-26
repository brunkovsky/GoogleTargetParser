package org.antalis;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static BufferedWriter bufferedWriter;

    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply path to fetch");
        String mainDirectory = args[0];
        File folder = new File(mainDirectory);
        String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File resultFile = new File(mainDirectory + timeStamp + ".txt");
        FileWriter fileWriter = new FileWriter(resultFile.getAbsoluteFile());
        bufferedWriter = new BufferedWriter(fileWriter);
        processFilesFromFolder(folder);
        bufferedWriter.flush();
        bufferedWriter.close();
        logger.info("done");
    }

    private static void processFilesFromFolder(File folder) throws IOException {
        File[] folderEntries = folder.listFiles();
        assert folderEntries != null;
        for (File entry: folderEntries) {
            if (entry.isDirectory()) {
                processFilesFromFolder(entry);
                continue;
            }
            manageResult(entry);
        }
    }

    private static void manageResult(File entry) throws IOException {
        Document doc = Jsoup.parse(entry, "utf-8");
        Elements trc_ = doc.select("[class^=trc_]");
        for (Element element: trc_) {
            String path = entry.getPath().split("/sites/")[1].split(".html")[0];
            String className = element.className().split(" ")[0];
            String description = element.text();
            String resultString = path + "\t" + className + "\t" + description;
            logger.info(resultString);
            bufferedWriter.write(resultString + "\r\n");
        }
    }
}
