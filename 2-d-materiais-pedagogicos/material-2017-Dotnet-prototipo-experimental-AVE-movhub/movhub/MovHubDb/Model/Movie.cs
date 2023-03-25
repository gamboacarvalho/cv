using System;
using Newtonsoft.Json;
using HtmlReflect.MyCustomAttributes;

namespace MovHubDb.Model

{
    public class Movie
    {
        [HtmlIgnore]
        public int Id { get; set; }
        [JsonProperty("original_title")]
        public string OriginalTitle{ set; get; }        
        public string Tagline { set; get; }
        [HtmlAs("<li class='list-group-item'><a href='/movies/{value}/credits'>Cast</a></li>")]
        public String Credits { get { return Id.ToString(); } }
        [HtmlIgnore]
        public double Popularity { get; set; }
        public long Budget { get; set; }
        [JsonProperty("vote_average")]
        public double VoteAverage { set; get; }
        [JsonProperty("release_date")]
        public string ReleaseDate { set; get; }
        [HtmlAs("<div class='card-body bg-light'><div><strong>{name}</strong>:</div>{value}</div>")]
        public String Overview { get; set; }
        [HtmlAs("<div style=position:absolute;top:0;right:0;><img width=50% src =http://image.tmdb.org/t/p/w185/{value}></div>")]
        [JsonProperty("poster_path")]
        public String MoviePhoto { get; set;}
    }
}