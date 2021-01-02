package lau.NovelDL;

import java.io.IOException;

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

public class UpdateCheck {
    public static String version = "1.0";

    public static void updateCheck() {
        System.out.println("Version " + version);
        String remoteVersion;
        try {
            remoteVersion = Weber.getWebString("https://raw.githubusercontent.com/layou233/NovelDL/master/src/main/resources/version.txt", "UTF-8");
        } catch (
                IOException e) {
            System.out.println("FAILED TO CHECK FOR UPDATES.\n" +
                    "You can check it yourself at https://github.com/layou233/NovelDL/releases\n");
            return;
        }
        if (version.equals(remoteVersion))
            System.out.println("Nice! You are using the latest version!\n");
        else System.out.println("THE NovelDL YOU ARE CURRENTLY USING HAS BEEN OUTDATED!\n" +
                "Check out newer versions at https://github.com/layou233/NovelDL/releases\n");
    }
}
