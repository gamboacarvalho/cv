using System;
using HtmlEmit;
using MovHubDb.Model;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using System.Collections.Generic;
using System.Linq;

namespace HtmlEmitTest
{
    [TestClass]
    public class EmitTest
    {
        private static readonly HtmlEmited htmlEmited = new HtmlEmited();

        [TestMethod]
        [ExpectedException(typeof(NullReferenceException))]
        public void NullTest()
        {
            IHtmlParser getter = Emit.EmitDetails(typeof(Movie));
            getter.ParseToHtml(null);
        }

        [TestMethod]
        public void EmitObjectTest()
        {
            TheMovieDbClient movieDb = new TheMovieDbClient();
            Movie mov = movieDb.MovieDetails(1018);

            string expected = "<ul class='list-group'><li class='list-group-item'><strong>OriginalTitle</strong>: Mulholland Drive</li>" +
                        "<li class='list-group-item'><strong>Tagline</strong>: An actress longing to be a star. A woman searching for herself. Both worlds will collide... on Mulholland Drive.</li>" +
                        "<li class='list-group-item'><a href='/movies/1018/credits'>Cast</a></li>" +
                        "<li class='list-group-item'><strong>Budget</strong>: 15000000</li>" +
                        "<li class='list-group-item'><strong>VoteAverage</strong>: 7.7</li>" +
                        "<li class='list-group-item'><strong>ReleaseDate</strong>: 2001-05-16</li>" +
                        "<div class='card-body bg-light'><div><strong>Overview</strong>:</div>After a car wreck on the winding Mulholland Drive renders a woman amnesic, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.</div>" +
                        "<div style=position:absolute;top:0;right:0;><img width=50% src =http://image.tmdb.org/t/p/w185//oKyY4TFaLjQTgyX8oRde82GinOw.jpg></div>" +
                        "</ul>";

            string actual = htmlEmited.ToHtml(mov) ;
            Assert.AreEqual( actual, expected);
        }

        [TestMethod]
        public void EmitArrayTest()
        {
            TheMovieDbClient theMovieDb = new TheMovieDbClient();
            MovieSearchItem[] movSearchItem = theMovieDb.Search("Mulholland Drive", 1);

            string expected = "<table class='table table-hover'>" +
                    "<thead>" +
                     "<tr><th>Id</th><th>Title</th><th>ReleaseDate</th><th>VoteAverage</th></tr>" +
                     "</thead>" +
                     "<tbody>" +
                     "<tr><td><a href='/movies/1018'>1018</a></td><td>Mulholland Drive</td><td>2001-05-16</td><td>7.7</td></tr>" +
                     "<tr><td><a href='/movies/205803'>205803</a></td><td>The Making of: Mulholland Drive</td><td>2004-04-05</td><td>7.3</td></tr>" +
                     "</tbody>" +
                     "</table>";

            string actual = htmlEmited.ToHtml(movSearchItem);
            Assert.AreEqual(expected, actual);

        }

        [TestMethod]
        public void ForTypeDetailsTest()
        {
            TheMovieDbClient movieDb = new TheMovieDbClient();
            CreditsItem cred = movieDb.MovieCredits(1018)[0];
            htmlEmited.ForTypeDetails<CreditsItem>(ct =>
            "<p>" +
                "<strong>" + ct.Name + "</strong>(" + ct.Id + ")" +
            "</p>");
            htmlEmited.ToHtml(cred);
            string expected = "<p><strong>" + cred.Name + "</strong>(" + cred.Id + ")</p>";
            Assert.AreEqual(expected, htmlEmited.ToHtml(cred));
        }

        [TestMethod]
        public void ForTypeInTableTest()
        {
            TheMovieDbClient movieDb = new TheMovieDbClient();
            MovieSearchItem[] movSearchItem = movieDb.Search("Mulholland Drive", 1);

            string expected = "<table class='table table-hover'>" +
                    "<thead>" +
                     "<tr><th>Id</th><th>Title</th></tr>" +
                     "</thead>" +
                     "<tbody>" +
                     "<tr><td>1018</td><td>Mulholland Drive</td></tr>" +
                     "<tr><td>205803</td><td>The Making of: Mulholland Drive</td></tr>" +
                     "</tbody>" +
                     "</table>";

            IEnumerable<string> headers = new string[] { "Id", "Title" };
            htmlEmited.ForTypeInTable<MovieSearchItem>(headers, mov =>
            {
                const string template = "<tr><td>{0}</td><td>{1}</td></tr>";
                return String.Format(template, mov.Id, mov.Title);
            });

            string actual = htmlEmited.ToHtml(movSearchItem);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void ForSequenceOfSingleTest()
        {
            TheMovieDbClient movieDb = new TheMovieDbClient();
            Movie mov = movieDb.MovieDetails(1018);
            string expected = "<h1>Movie Title</h1><ul><li>Mulholland Drive</li></ul>";
            htmlEmited.ForSequenceOf<Movie>(movie =>
            {
                string liTitles = movie.Aggregate("", (prev, mv) => prev + "<li>" + mv.OriginalTitle + "</li>");
                return "<h1>Movie Title</h1><ul>" + liTitles + "</ul>";
            });

            string actual = htmlEmited.ToHtml(mov);
            Assert.AreEqual(actual, expected);
        }

        [TestMethod]
        public void ForSequenceOfMultiTest()
        {
            TheMovieDbClient movieDb = new TheMovieDbClient();
            MovieSearchItem[] movSearchItem = movieDb.Search("Mulholland Drive", 1);
            string expected = "<h1>Movie Title</h1><ul><li>Mulholland Drive</li><li>The Making of: Mulholland Drive</li></ul>";

            htmlEmited.ForSequenceOf<MovieSearchItem>(movie =>
            {
                string liTitles = movie.Aggregate("", (prev, mv) => prev + "<li>" + mv.Title + "</li>");
                return "<h1>Movie Title</h1><ul>" + liTitles + "</ul>";
            });

            string actual = htmlEmited.ToHtml(movSearchItem);
            Assert.AreEqual(actual, expected);
        }

    }
}
