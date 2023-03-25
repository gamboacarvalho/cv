package movasync.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Person {
    private final int id;
    private final String profilePath;
    private final String name;
    private CompletableFuture<Stream<SearchItem>> movies;
    private final Supplier<CompletableFuture<Stream<SearchItem>>> auxMovies;
    private final String placeOfBirth;
    private final String biography;

    public Person(int id, String name, String placeOfBirth, String biography, Supplier<CompletableFuture<Stream<SearchItem>>> movies,String profilePath) {
        this.id = id;
        this.name = name;
        this.movies = null;
        this.auxMovies = movies;
        this.placeOfBirth = placeOfBirth;
        this.biography = biography;
        this.profilePath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2".concat(profilePath);
    }

    public int getId() {
        return id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getBiography() {
        return biography;
    }

    public CompletableFuture<Stream<SearchItem>> getMovies() {
        if(movies == null){
            movies = auxMovies.get();
        }
        return movies;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", profilePath='" + profilePath + '\'' +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
