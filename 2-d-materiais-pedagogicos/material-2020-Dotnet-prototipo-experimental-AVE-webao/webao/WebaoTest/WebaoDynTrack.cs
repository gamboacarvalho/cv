using Webao.Attributes;
using Webao.Test.Dto;

namespace Webao.Test
{
    [BaseUrl("http://ws.audioscrobbler.com/2.0/")]
    [AddParameterAttribute("format", "json")]
    [AddParameterAttribute("api_key", "038cde478fb0eff567330587e8e981a4")]

    public interface WebaoDynTrack : IWebaoTrack
    {

        [Get("?method=geo.gettoptracks&country={country}")]
        DtoGeoTopTracks GeoGetTopTracks(string country);
    }
}