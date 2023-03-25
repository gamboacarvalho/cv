package movasync.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Movie {
    private final int id;
    private final String originalTitle;
    private final String tagline;
    private final String overview;
    private final double voteAverage;
    private final String releaseDate;
    private final String posterPath;
    private CompletableFuture<Stream<Credit>> cast;
    private final Supplier<CompletableFuture<Stream<Credit>>> auxCast;


    public Movie(
            int id,
            String originalTitle,
            String tagline,
            String overview,
            double voteAverage,
            String releaseDate,
            String posterPath,
            Supplier<CompletableFuture<Stream<Credit>>> cast)
    {
        this.id = id;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.posterPath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2".concat(posterPath);
        this.cast = null;
        this.auxCast = cast;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public CompletableFuture<Stream<Credit>> getCast() {
        if(cast == null){
            cast = auxCast.get();
        }
        return cast;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", voteAverage=" + voteAverage +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
