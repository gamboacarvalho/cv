package org.isel.jingle.mock;

import org.isel.jingle.asyncReq.AsyncRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class MockAsyncRequest implements AsyncRequest {
    @Override
    public CompletableFuture<String> getBody(String path) {
        try {
            URL resource = ClassLoader.getSystemResource(MockUriFormatter.formatUri(path));

            BufferedReader reader = Files.newBufferedReader(Paths.get(resource.toURI()));
            //BufferedReader reader = Files.newBufferedReader(Paths.get(FileSaver.FILE_PATH + "/" + MockUriFormatter.formatUri(path)));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return CompletableFuture.completedFuture(sb.toString());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("");
        }
    }
}
