using Webao.Attributes;
using Webao.Test.Dto;

namespace Webao.Test
{
    [BaseUrl("http://ws.audioscrobbler.com/2.0/")]
    [AddParameter("format", "json")]
    [AddParameter("api_key", "038cde478fb0eff567330587e8e981a4")]
    public class WebaoArtist : AbstractAccessObject, IWebaoArtist
    {
        public WebaoArtist(IRequest req) : base(req) { }

        [Get("?method=artist.getinfo&artist={name}")]
        public DtoArtist GetInfo(string name) => (DtoArtist)Request(name); 
        

        [Get("?method=artist.search&artist={name}")]
        public DtoSearch Search(string name) => (DtoSearch)Request(name);       
    }
}
