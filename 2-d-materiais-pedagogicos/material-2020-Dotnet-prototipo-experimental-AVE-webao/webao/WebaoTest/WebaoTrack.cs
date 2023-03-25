using Webao.Attributes;
using Webao.Test.Dto;

namespace Webao.Test
{
    [BaseUrl("http://ws.audioscrobbler.com/2.0/")]
    [AddParameterAttribute("format", "json")]
    [AddParameterAttribute("api_key", "038cde478fb0eff567330587e8e981a4")]

    public class WebaoTrack : AbstractAccessObject, IWebaoTrack
    {
        public WebaoTrack(IRequest req) : base(req)
        {
        }

        [Get("?method=geo.gettoptracks&country={country}")]
        public DtoGeoTopTracks GeoGetTopTracks(string country) {
            return (DtoGeoTopTracks) Request(country);
        }
    }
}