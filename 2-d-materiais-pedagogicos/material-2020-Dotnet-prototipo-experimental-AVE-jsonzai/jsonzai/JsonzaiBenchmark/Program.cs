using BenchmarkDotNet.Running;

namespace JsonzaiBenchmark
{
    class Program
    {
        static void Main(string[] args)
        {
            BenchmarkRunner.Run<JsonzaiBench>();
        }
    }
}