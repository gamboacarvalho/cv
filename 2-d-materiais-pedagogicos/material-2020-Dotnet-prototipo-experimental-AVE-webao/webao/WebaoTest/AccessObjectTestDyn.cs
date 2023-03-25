using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using WebaoDyn;

namespace Webao.Test
{
    [TestClass]
    public class AccessObjectTestDyn : AccessObjectTest
    {
        public AccessObjectTestDyn() : base(
            (IWebaoArtist) WebaoDynBuilder.Build(typeof(WebaoDynArtist), req),
            (IWebaoTrack) WebaoDynBuilder.Build(typeof(WebaoDynTrack), req))
        {
        }

        [TestMethod]
        public void TestBuildWebaoDyn() 
        {
            // 1.
            //
            // WebaoDynBuilder.BuildWebao(typeof(WebaoDynArtist));
            //
            // 2.
            //
            IWebaoArtist webao = (IWebaoArtist) WebaoDynBuilder.Build(typeof(WebaoDynArtist), new HttpRequest());
            Dto.DtoSearch muse = webao.Search("Muse");
            Console.WriteLine(muse.Results.ArtistMatches.Artist[0].Name);

        }
    }
}
