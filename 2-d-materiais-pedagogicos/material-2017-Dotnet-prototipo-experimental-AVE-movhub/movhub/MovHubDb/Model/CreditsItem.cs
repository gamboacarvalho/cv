using Newtonsoft.Json;
using HtmlReflect.MyCustomAttributes;

namespace MovHubDb.Model
{
    public class CreditsItem
    {
        [HtmlAs("<td><a href='/person/{value}/movies'>{value}</a></td>")]
        public int Id { get; set; }
        public string Character { get; set; }
        public string Name { get; set; }
    }
}