package org.antalis;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class Main {
    private static String PREFIX = "trc_";
    private static Writer excelWriter;
    private static Writer txtWriter;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply path to fetch");
        String mainDirectory = args[0];
        String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        excelWriter = new ExcelWriter();
        txtWriter = new TxtWriter();

        excelWriter.init(mainDirectory + timeStamp + ".xlsx");
        txtWriter.init(mainDirectory + timeStamp + ".txt");

        processFilesFromFolder(new File(mainDirectory));

        excelWriter.close();
        txtWriter.close();

        logger.info("done");
    }

    private static void processFilesFromFolder(File folder) throws IOException {
        File[] folderEntries = folder.listFiles();
        assert folderEntries != null;

        for (int i = folderEntries.length - 1; i >= 0 ; i--) {
            if (folderEntries[i].isDirectory()) {
                processFilesFromFolder(folderEntries[i]);
                continue;
            }
            logger.info(folderEntries[i] + " is processing...");
            manageResult(folderEntries[i]);
        }
    }

    private static void manageResult(File entry) throws IOException {
        Document doc = Jsoup.parse(entry, "utf-8");
        Elements trc_ = doc.select("[class^=" + PREFIX + "]");
        String title = doc.title();
        for (Element element: trc_) {
            String path = entry.getPath().split("/sites/")[1].split(".html")[0];
            String className = element.className().split(" ")[0];
            String description = element.text();
            excelWriter.write(path, title, className, description);
            txtWriter.write(path, title, className, description);
        }
    }
}
