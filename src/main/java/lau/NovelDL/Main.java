package lau.NovelDL;

import javax.xml.ws.http.HTTPException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

public class Main {
    public static String firstPageURL;
    public static String domain;
    public static String charset;
    public static File file;

    public static String replacer(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(
                " __   _   _____   _     _   _____   _       _____   _      \n" +
                        "|  \\ | | /  _  \\ | |   / / | ____| | |     |  _  \\ | |     \n" +
                        "|   \\| | | | | | | |  / /  | |__   | |     | | | | | |     \n" +
                        "| |\\   | | | | | | | / /   |  __|  | |     | | | | | |     \n" +
                        "| | \\  | | |_| | | |/ /    | |___  | |___  | |_| | | |___  \n" +
                        "|_|  \\_| \\_____/ |___/     |_____| |_____| |_____/ |_____| "
        );
        UpdateCheck.updateCheck();
        System.out.print("Enter the first page URL of the novel: ");
        firstPageURL = KotlinFunctionsKt.readln().trim();
        try {
            domain = KotlinFunctionsKt.regexFind("(http|https)://(www.)?(\\w+(\\.)?)+", firstPageURL);
        } catch (NullPointerException e) {
            System.out.println("Wrong URL! The URL should can be matched by regex (http|https)://(www.)?(\\w+(\\.)?)+");
            return;
        }
        System.out.print("Enter the file name (*.txt): ");
        file = new File(KotlinFunctionsKt.readln().trim() + ".txt");
        FileOutputStream fileOutputStream;
        try {
            if (file.createNewFile()) System.out.println("Successful created the file.");
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write("[!]This file was generated by NovelDL!\n\n".getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.out.println("The new file that created is not writable for some reasons, so stop.");
            return;
        } catch (IOException e) {
            System.out.println("File already exists, please provide a new file name.");
            return;
        }

        long dumpCount = 1; // I am a cautious person, so I used long type lol
        String currentPageURL = firstPageURL;
        String listId = null;
        long startTime = System.currentTimeMillis() / 1000;

        try { // First dumping
            String s = replacer(Weber.getWebString(currentPageURL, "UTF-8"));
            charset = NovelInfosKt.getCharset(s);
            if (!charset.equals("UTF.8"))
                s = replacer(Weber.getWebString(currentPageURL, charset));
            String chapter = NovelInfosKt.getChapterTitle(s);
            fileOutputStream.write(chapter.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write(NovelHandlerKt.readNovel(s).getBytes(StandardCharsets.UTF_8));
            currentPageURL = NovelInfosKt.getUrlFromHrefValue(s, "下一章");
            listId = NovelInfosKt.getListIdFromUrl(currentPageURL);
            currentPageURL = domain + currentPageURL;
            System.out.println(dumpCount++ + " Successfully dumped " + chapter);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (HTTPException e) {
            System.out.println(dumpCount + " FAILED:Target website responded Code " + e.getStatusCode());
        } catch (NullPointerException e) {
            System.out.println("It doesn't seem to be a novel. Do your IP got banned?");
            return;
        }

        while (true) {
            try {
                String s = replacer(Weber.getWebString(currentPageURL, charset));
                String chapter = NovelInfosKt.getChapterTitle(s);
                fileOutputStream.write(chapter.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write(NovelHandlerKt.readNovel(s).getBytes(StandardCharsets.UTF_8));
                currentPageURL = NovelInfosKt.getUrlFromHrefValue(s, "下一章");
                System.out.println(dumpCount++ + " Successfully dumped " + chapter);
                if (currentPageURL.equals(listId)) break;
                currentPageURL = domain + currentPageURL;
                if (dumpCount % 5 == 0) Thread.sleep(2000);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (HTTPException e) {
                System.out.println(dumpCount + " FAILED:Target website responded Code " + e.getStatusCode());
            } catch (NullPointerException e) {
                System.out.println(dumpCount + " FAILED:It doesn't seem to be a novel. REDUMPING IN 10 SECONDS...");
                Thread.sleep(10000);
            }
        }

        try {
            fileOutputStream.close();
            System.out.println("==========");
            System.out.println("DOWNLOAD FINISHED IN " + ((System.currentTimeMillis() / 1000) - startTime) + " SECONDS");
            System.out.println("Dumped Counts: " + --dumpCount);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\nError when close the file.");
        }
    }
}