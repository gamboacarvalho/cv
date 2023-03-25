package movlazy.model;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Person {
    private final int id;
    private final String name;
    private final Supplier<Stream<SearchItem>> movies;
    private final String placeOfBirth;
    private final String biography;

    public Person(int id, String name, String placeOfBirth, String biography, Supplier<Stream<SearchItem>> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
        this.placeOfBirth = placeOfBirth;
        this.biography = biography;
    }

    public int getId() {
        return id;
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

    public Supplier<Stream<SearchItem>> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }
}
