using Webao.Attributes;
using Webao.Test.Dto;

namespace Webao.Test
{
    [BaseUrl("http://ws.audioscrobbler.com/2.0/")]
    [AddParameter("format", "json")]
    [AddParameter("api_key", "038cde478fb0eff567330587e8e981a4")]
    public interface WebaoDynArtist : IWebaoArtist
    {
        [Get("?method=artist.getinfo&artist={name}")]
        DtoArtist GetInfo(string name);


        [Get("?method=artist.search&artist={name}")]
        DtoSearch Search(string name);
    }
}
