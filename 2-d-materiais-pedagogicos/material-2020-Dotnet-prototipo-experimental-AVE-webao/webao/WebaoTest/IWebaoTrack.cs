using Webao.Test.Dto;

namespace Webao.Test
{
    public interface IWebaoTrack
    {
        DtoGeoTopTracks GeoGetTopTracks(string country);
    }
}