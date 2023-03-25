using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Webao.Test
{
    [TestClass]
    public class AccessObjectTestReflect : AccessObjectTest
    {
        public AccessObjectTestReflect() 
            : base(
                   (WebaoArtist) WebaoBuilder.Build(typeof(WebaoArtist), req),
                   (WebaoTrack) WebaoBuilder.Build(typeof(WebaoTrack), req))
        {
        }
    }
}
