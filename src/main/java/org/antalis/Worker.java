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
    private String domain;
    private static final String PREFIX = "trc_";
    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    Worker(String mainDirectory) {
        this.mainDirectory = mainDirectory;
        String[] split = mainDirectory.split("\\\\"); // todo check in Unix
        domain = split[split.length - 1];
        if (domain.endsWith(File.separator)) {
            domain = domain.substring(0, domain.length() - 1);
        }
        String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss").format(new Date());
        excelWriter = new ExcelWriter(domain + timeStamp + ".xlsx");
//        txtWriter = new TxtWriter(domain + timeStamp + ".txt");
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
            File fileToProceed = folderEntries[i];
            if (fileToProceed.isDirectory()) {
                processFilesFromFolder(fileToProceed);
                continue;
            }
            logger.info(fileToProceed + " is processing...");
            if (fileToProceed.getName().endsWith(".html")) {
                manageResult(fileToProceed);
            }
        }
    }

    private void manageResult(File entry) throws IOException {
        Document doc = Jsoup.parse(entry, "utf-8");
        Elements trcElement = doc.select("[class^=" + PREFIX + "]");
        String title = doc.title();
        for (Element element : trcElement) {
            String url = (entry.getPath().substring(mainDirectory.length())).replace("\\", "/").replace("@", "?");
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            String path = domain + url;
            path = path.substring(0, path.length() - 5); // remove last '.html'
            String className = element.className().split(" ")[0];
            String description = element.text();
            excelWriter.write(path, title, className, description);
//            txtWriter.write(path, title, className, description);
        }
    }
}
