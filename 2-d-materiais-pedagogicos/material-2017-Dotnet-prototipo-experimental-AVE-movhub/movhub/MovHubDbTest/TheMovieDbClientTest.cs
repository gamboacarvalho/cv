using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using MovHubDb.Model;

namespace MovHubDbTest
{
    [TestClass]
    public class TheMovieDbClientTest
    {
        [TestMethod]
        public void SearchTest()
        {
            TheMovieDbClient movDbClient = new TheMovieDbClient();
            MovieSearchItem[] res = movDbClient.Search("war games", 1);
            Assert.AreEqual(res.Length, 6); //total results
            Assert.AreEqual(res[0].Title, "War Games: The Dead Code");
            Assert.AreEqual(res[0].ReleaseDate, "2008-07-29");
            Assert.AreEqual(res[res.Length - 1].Title, "WarGames");
            Assert.AreEqual(res[res.Length - 1].ReleaseDate, "1983-06-03");
        }

        [TestMethod]
        public void MovieDetailsTest()
        {
            TheMovieDbClient movDbClient = new TheMovieDbClient();
            Movie mov = movDbClient.MovieDetails(500);
            Assert.AreEqual(mov.OriginalTitle, "Reservoir Dogs");
            Assert.AreEqual(mov.ReleaseDate, "1992-09-02");
            Assert.AreEqual(mov.Id,500);
        }

        [TestMethod]
        public void MovieCreditsTest()
        {
            TheMovieDbClient movDbClient = new TheMovieDbClient();
            CreditsItem[] res = movDbClient.MovieCredits(860);
            Assert.AreEqual(res[0].Id, 4756);
            Assert.AreEqual(res[0].Character, "David Lightman");
        }

        [TestMethod]
        public void PersonDetaisTest()
        {
            TheMovieDbClient movDbClient = new TheMovieDbClient();
            Person p = movDbClient.PersonDetais(500);
            Assert.AreEqual(p.Name, "Tom Cruise");
            Assert.AreEqual(p.Birthday, "1962-07-03");
        }

        [TestMethod]
        public void PersonMoviesTest()
        {
            TheMovieDbClient movDbClient = new TheMovieDbClient();
            MovieSearchItem[] res = movDbClient.PersonMovies(500);
            Assert.AreEqual(res[0].Id, 74);
            Assert.AreEqual(res[0].Title, "War of the Worlds");
            Assert.AreEqual(res[0].ReleaseDate, "2005-06-28");
            Assert.AreEqual(res[0].VoteAverage, 6.2);
        }
    }
}
