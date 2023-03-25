using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using HtmlReflect;
using MovHubDb.Model;

namespace HtmlReflectTest
{
    [TestClass]
    public class HtmlectTest
    {
        [TestMethod]
        public void ToHtmlTest()
        {
            Htmlect htmlect = new Htmlect();
            TheMovieDbClient theMovieDb = new TheMovieDbClient();
            Movie mov = theMovieDb.MovieDetails(1018);

            string exp = "<ul class='list-group'><li class='list-group-item'><strong>OriginalTitle</strong>: Mulholland Drive</li>" +
                        "<li class='list-group-item'><strong>Tagline</strong>: An actress longing to be a star. A woman searching for herself. Both worlds will collide... on Mulholland Drive.</li>" +
                        "<li class='list-group-item'><a href='/movies/1018/credits'>Cast</a></li>"+
                        "<li class='list-group-item'><strong>Budget</strong>: 15000000</li>"+
                        "<li class='list-group-item'><strong>VoteAverage</strong>: 7.7</li>" +
                        "<li class='list-group-item'><strong>ReleaseDate</strong>: 2001-05-16</li>" +
                        "<div class='card-body bg-light'><div><strong>Overview</strong>:</div>After a car wreck on the winding Mulholland Drive renders a woman amnesic, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.</div>" +
                        "<div style=position:absolute;top:0;right:0;><img width=50% src =http://image.tmdb.org/t/p/w185//oKyY4TFaLjQTgyX8oRde82GinOw.jpg></div>" + 
                        "</ul>";

            string actual = htmlect.ToHtml(mov);
            Assert.AreEqual(exp, actual);
        }

        [TestMethod]
        public void ToHtmlArrayTest(){

            Htmlect htmlect = new Htmlect();
            TheMovieDbClient theMovieDb = new TheMovieDbClient();
            MovieSearchItem [] movSearchItem = theMovieDb.Search("Mulholland Drive",1);

            string exp ="<table class='table table-hover'>" +
                    "<thead>"+
                     "<tr><th>Id</th><th>Title</th><th>ReleaseDate</th><th>VoteAverage</th></tr>"+
                     "</thead>"+
                     "<tbody>"+
                     "<tr><td><a href='/movies/1018'>1018</a></td><td>Mulholland Drive</td><td>2001-05-16</td><td>7.7</td></tr>" +
                     "<tr><td><a href='/movies/205803'>205803</a></td><td>The Making of: Mulholland Drive</td><td>2004-04-05</td><td>7.3</td></tr>" +
                     "</tbody>"+
                     "</table>";

            string actual = htmlect.ToHtml(movSearchItem);
            Assert.AreEqual(exp, actual);
        }
    }
}
