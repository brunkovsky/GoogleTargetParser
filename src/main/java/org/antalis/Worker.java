package org.antalis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

class Worker {
    private Writer excelWriter;
//    private Writer txtWriter;
    private String mainDirectory;
    private static final String PREFIX = "trc_";
    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    Worker(String mainDirectory) {
        this.mainDirectory = mainDirectory;
        String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date());
        excelWriter = new ExcelWriter(mainDirectory + timeStamp + ".xlsx");
//        txtWriter = new TxtWriter(mainDirectory + timeStamp + ".txt");
    }

    void execute() throws IOException {
        processFilesFromFolder(new File(mainDirectory));
        excelWriter.close();
//        txtWriter.close();
        logger.info("done");
    }

    private void processFilesFromFolder(File folder) throws IOException {
        File[] folderEntries = folder.listFiles();
        if (folderEntries == null) {
            throw new IOException("Error: wrong path!");
        }
        for (int i = folderEntries.length - 1; i >= 0 ; i--) {
            if (folderEntries[i].isDirectory()) {
                processFilesFromFolder(folderEntries[i]);
                continue;
            }
            logger.info(folderEntries[i] + " is processing...");
            manageResult(folderEntries[i]);
        }
    }

    private void manageResult(File entry) throws IOException {
        if (entry.getName().endsWith(".html")) {
            Document doc = Jsoup.parse(entry, "utf-8");
            Elements trc_ = doc.select("[class^=" + PREFIX + "]");
            String title = doc.title();
            for (Element element : trc_) {
                String path = entry.getPath();
                String className = element.className().split(" ")[0];
                String description = element.text();
                excelWriter.write(path, title, className, description);
//            txtWriter.write(path, title, className, description);
            }
        }
    }
}
