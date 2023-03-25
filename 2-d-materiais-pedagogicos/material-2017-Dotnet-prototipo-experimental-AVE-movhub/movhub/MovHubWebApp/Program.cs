using Nancy;
using Nancy.Hosting.Self;
using System;
using MovHubDb;
using MovHubDb.Model;

namespace MovHubWebApp
{
    class Program
    {
        static void StartWebApp()
        {
            var uri = new Uri("http://localhost:3000");
            HostConfiguration hostConfigs = new HostConfiguration();
            hostConfigs.UrlReservations.CreateAutomatically = true;

            using (var host = new NancyHost(uri, new DefaultNancyBootstrapper(), hostConfigs))
            {
                host.Start();

                Console.WriteLine("Your application is running on " + uri);
                Console.WriteLine("Press any [Enter] to close the host.");
                Console.ReadLine();
            }
        }

        static void TestMovieDbWebApi()
        {
            TheMovieDbClient db = new TheMovieDbClient();
            MovieSearchItem[] res = db.Search("war games", 1);
            Console.WriteLine(res);
            Console.WriteLine(db.MovieDetails(860));
        }

        static void Main(string[] args)
        {
            TestMovieDbWebApi();
            StartWebApp();
        }
    }
}
