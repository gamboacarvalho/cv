using System;
using Jsonzai.Attributes;
using Jsonzai.Test.Convertors;

namespace Jsonzai.Test.Model {
    public class Work {
        public Work() { }
        public Work(int id, string desc, DateTime dateTime) {
            this.Id = id;
            this.Desc = desc;
            this.DTime = dateTime;
        }

        public int Id { get; set; }
        public string Desc { get; set; }
        [JsonConvert(typeof(JsonToDateTime))] public DateTime DTime { get; set; }

        [JsonConvert(typeof(JsonToDateTime))] [JsonProperty("OldDate")] public DateTime DTime2 { get; set; }
    }
}
