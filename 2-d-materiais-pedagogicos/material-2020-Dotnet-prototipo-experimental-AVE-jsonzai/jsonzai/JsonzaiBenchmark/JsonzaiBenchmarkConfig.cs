using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BenchmarkDotNet.Attributes;
using BenchmarkDotNet.Configs;
using BenchmarkDotNet.Jobs;
using BenchmarkDotNet.Toolchains.InProcess.Emit;
using Jsonzai;
using JsonzaiBenchmark.Test.Model;

namespace JsonzaiBenchmark {

    public class JsonzaiBenchmarkConfig : ManualConfig {

        public JsonzaiBenchmarkConfig() {
            Add(Job.MediumRun
                   .WithLaunchCount(1)
                   .With(InProcessEmitToolchain.Instance)
                   .WithId("InProcess"));
        }
    }

    [RankColumn]
    [Config(typeof(JsonzaiBenchmarkConfig))]
    public class JsonzaiBench {

        static readonly string src = "{\"Name\": \"Ze Manel\", \"Nr\": 6512, \"Group\": 11, \"GithubId\": \"omaior\"}";

        [Benchmark]
        public void BenchJsonzaiReflect() {
            Student std = JsonParser.Parse<Student>(src);
        }

        [Benchmark]
        public void BenchJsonzaiEmit() {
            Student std = JsonParserEmit.Parse<Student>(src);
        }
    }
}
