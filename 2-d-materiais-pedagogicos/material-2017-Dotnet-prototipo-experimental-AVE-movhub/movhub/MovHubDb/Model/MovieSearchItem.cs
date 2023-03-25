using Newtonsoft.Json;
using HtmlReflect.MyCustomAttributes;

namespace MovHubDb.Model
{
    public class MovieSearchItem
    {
        [HtmlAs("<td><a href='/movies/{value}'>{value}</a></td>")]
        public int Id { get; set; }
        public string Title { get; set; }
        [JsonProperty("release_date")]
        public string ReleaseDate { get; set; }
        [JsonProperty("vote_average")]
        public double VoteAverage { get; set; }
    }
}