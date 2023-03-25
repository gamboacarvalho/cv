using System;
using MovHubDb.Model;
using HtmlEmit;
using HtmlReflect;

namespace BenchMark
{
    class BenchMarksApp{
        static readonly MovieSearchItem[] movies = getMovieSearchItemArray();
        static readonly HtmlEmited emiter = new HtmlEmited();
        static readonly Htmlect reflect = new Htmlect();

        static void Main(){

            NBench.Bench(BenchMarksApp.BenchEmit, "HtmlEmit");
            NBench.Bench(BenchMarksApp.BenchReflect, "HtmlReflect");
            Console.ReadKey(true);

        }

        private static void BenchEmit(){
            emiter.ToHtml(movies);
        }

        private static void BenchReflect()
        {
            reflect.ToHtml(movies);
        }

        private static MovieSearchItem[] getMovieSearchItemArray()
        {
            MovieSearchItem movie = new MovieSearchItem();
            movie.Id = 1500;
            movie.ReleaseDate = "2016-01-10";
            movie.Title = "Curral de Moinas";
            movie.VoteAverage = 9.9f;
            MovieSearchItem[] movs = new MovieSearchItem[500];
            for (int i = 0; i < 500; i++)
            {
                movs[i] = movie;
            }

            return movs;
        }

    }
}