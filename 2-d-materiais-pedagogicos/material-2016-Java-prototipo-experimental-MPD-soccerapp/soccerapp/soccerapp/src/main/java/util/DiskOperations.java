package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DiskOperations {

    public static Optional<String> readFile(String fileLocation) throws URISyntaxException, IOException {
        Path p = Paths.get(fileLocation);
        try {
            try (BufferedReader reader = Files.newBufferedReader(p)) {
                return Optional.of(reader.lines().collect(Collectors.joining()));
            }
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public static void writeToFileAsync(String fileName, String pageHtml) {
        String path = fileName.replaceAll("/", "-");
        CompletableFuture.runAsync(() -> {
            File file = new File("src/main/resources/data/" + path + ".html");
            try(FileWriter writer = new FileWriter(file, true)){
                file.createNewFile();
                writer.append(pageHtml);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static File getFile (String fileName) {
        String path = fileName.replaceAll("/", "-");
        String fileLocation = "src/main/resources/data/%s.html";
        return new File(String.format(fileLocation, path));
    }
}
