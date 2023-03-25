package util;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;

/**
 * Class used for test purposes in order to guarantee that http request tests always pass.
 * So, we will basically do a request which will retrieve a TestDto( Dto with total_results and total_pages) in order to
 * verify the current number of results for a movie search instead of hardcode in http request tests.
 */

public class ApiRequest {

    private static String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key=3206de5c5e8ebe8b47eca707e469e394&query=";

    public static TestDto search (String name){
        return getBody(SEARCH_URL + name.replace(" ","+"));
    }


    public static TestDto getBody(String path) {
        Gson gson = new Gson();
        String json = null;

        try {
            InputStream inputStream = new URL(path).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            json = br.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return gson.fromJson(json, TestDto.class);
    }
}
