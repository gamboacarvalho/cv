using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Webao.Test.Dto;

namespace Webao.Test
{
   
    public abstract class AccessObjectTest
    {
        readonly IWebaoArtist artistWebao;
        readonly IWebaoTrack trackWebao;
        public static readonly MockRequest req = new MockRequest();

        public AccessObjectTest(IWebaoArtist artistWebao, IWebaoTrack trackWebao)
        {
            this.artistWebao = artistWebao;
            this.trackWebao = trackWebao;
        }

        [TestMethod]
        public void TestWebaoArtistGetInfo()
        {
            DtoArtist dto = artistWebao.GetInfo("muse");
            Assert.AreEqual("Muse", dto.Artist.Name);
            Assert.AreEqual("fd857293-5ab8-40de-b29e-55a69d4e4d0f", dto.Artist.Mbid);
            Assert.AreEqual("https://www.last.fm/music/Muse", dto.Artist.Url);
            Assert.AreNotEqual(0, dto.Artist.Stats.Listeners);
            Assert.AreNotEqual(0, dto.Artist.Stats.Playcount);
        }

        [TestMethod]
        public void TestWebaoArtistSearch()
        {
            List<Artist> artists = artistWebao.Search("black").Results.ArtistMatches.Artist;
            Assert.AreEqual("Black Sabbath", artists[1].Name);
            Assert.AreEqual("Black Eyed Peas", artists[2].Name);
        }

        [TestMethod]
        public void TestWebaoTrackGeoGetTopTracks()
        {
            List<Track> tracks = trackWebao.GeoGetTopTracks("australia").Tracks.Track;
            Assert.AreEqual("The Less I Know the Better", tracks[0].Name);
            Assert.AreEqual("Mr. Brightside", tracks[1].Name);
            Assert.AreEqual("The Killers", tracks[1].Artist.Name);
        }
    }
}
