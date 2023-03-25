package org.isel.jingle.mock;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {
    public static final String FILE_PATH = ".";

    public static void saveFileToSystem(String uri, String body) {
        try {
            System.out.println("Saving file to uri- '" + uri + "'...");

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.mkdir();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + "/" + uri));
            writer.write(body);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
