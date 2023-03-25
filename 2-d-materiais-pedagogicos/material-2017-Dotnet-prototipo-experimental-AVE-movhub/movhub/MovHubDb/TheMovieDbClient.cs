using MovHubDb.Model;
using Newtonsoft.Json;
using System;
using System.Net;

namespace MovHubDb
{
    public class TheMovieDbClient
    {
        private readonly WebClient client = new WebClient();

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/search/movie?api_key=3206de5c5e8ebe8b47eca707e469e394&query=war%20games
        /// </summary>
        public MovieSearchItem[] Search(string title, int page)
        {
            MovieSearch movieSearch;
            try
            {
                string body = client.DownloadString("https://api.themoviedb.org/3/search/movie?api_key=3206de5c5e8ebe8b47eca707e469e394&query=" + title + "&page=" + page);
                movieSearch = (MovieSearch)JsonConvert.DeserializeObject(body, typeof(MovieSearch));

            }
            catch
            {
                return new MovieSearchItem[] { };
            }
             return movieSearch.results;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508?api_key=*****
        /// </summary>
        public Movie MovieDetails(int id) {
            Movie mov = new Movie();
            try
            {
                string body = client.DownloadString("https://api.themoviedb.org/3/movie/" + id + "?api_key=3206de5c5e8ebe8b47eca707e469e394");
                mov = (Movie)JsonConvert.DeserializeObject(body, typeof(Movie));
            }
            catch
            {
                return mov;
            }
            return mov;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508/credits?api_key=*****
        /// </summary>
        public CreditsItem[] MovieCredits(int id) {
            Credits credits;
            try
            {
                string body = client.DownloadString("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=3206de5c5e8ebe8b47eca707e469e394");
                credits = (Credits)JsonConvert.DeserializeObject(body, typeof(Credits));

            }
            catch
            {
                return new CreditsItem[] { };
            }
            return credits.cast;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489?api_key=3206de5c5e8ebe8b47eca707e469e394
        /// </summary>
        public Person PersonDetais(int actorId)
        {
            Person person = new Person();
            try
            {
                string body = client.DownloadString("https://api.themoviedb.org/3/person/" + actorId + "?api_key=3206de5c5e8ebe8b47eca707e469e394");
                person = (Person)JsonConvert.DeserializeObject(body, typeof(Person));
            }
            catch
            {
                return person;
            }
            return person;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489/movie_credits?api_key=3206de5c5e8ebe8b47eca707e469e394
        /// </summary>
        public MovieSearchItem[] PersonMovies(int actorId) {
            PersonCredits pcredits;
            try
            {
                string body = client.DownloadString("https://api.themoviedb.org/3/person/" + actorId + "/movie_credits?api_key=3206de5c5e8ebe8b47eca707e469e394");
                pcredits = (PersonCredits)JsonConvert.DeserializeObject(body, typeof(PersonCredits));
            }
            catch
            {
                return  new MovieSearchItem[] { };
            }
            return pcredits.cast;
        }
    }
}
