using System;
using Webao;
using Webao.Test;
using Webao.Test.Dto;
using WebaoDyn;

namespace WebaoBench
{
    class Program
    {
        static MockRequest req = new MockRequest();
        static WebaoDynArtist webaoDyn = (WebaoDynArtist) WebaoDynBuilder.Build(typeof(WebaoDynArtist), req);
        static WebaoArtist webaoReflect = (WebaoArtist)WebaoBuilder.Build(typeof(WebaoArtist), req);

        static void Main(string[] args)
        {
            /**
             * Setup Mock
             */
            webaoReflect.GetInfo("Muse");
            webaoDyn.GetInfo("Muse");
            /**
             * Run Bench
             */
            NBench.Bench(BenchWebaoDyn, "Bench Dynamic");
            NBench.Bench(BenchWebaoReflect, "Bench Reflect");
        }
        static void BenchWebaoReflect() {
            webaoReflect.GetInfo("Muse");
        }
        static void BenchWebaoDyn()
        {
            webaoDyn.GetInfo("Muse");
        }

    }
}
