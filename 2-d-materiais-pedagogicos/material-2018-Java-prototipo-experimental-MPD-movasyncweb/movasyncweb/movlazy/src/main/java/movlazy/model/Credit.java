package movlazy.model;

import java.util.function.Supplier;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Credit {
    private final int id;
    private final int movieId;
    private final String character;
    private final String department;
    private final String job;
    private final String name;
    private final Supplier<Person> actor;


    public Credit(int id, int movieId, String character, String department, String job, String name, Supplier<Person> actor) {
        this.id = id;
        this.movieId = movieId;
        this.character = character;
        this.department = department;
        this.job = job;
        this.name = name;
        this.actor = actor;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public int getMovieId() {
        return movieId;
    }

    public Person getActor() {
        return actor.get();
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", character='" + character + '\'' +
                ", name='" + name + '\'' +
                ", getPersonCreditsCast=" + actor +
                '}';
    }
}
