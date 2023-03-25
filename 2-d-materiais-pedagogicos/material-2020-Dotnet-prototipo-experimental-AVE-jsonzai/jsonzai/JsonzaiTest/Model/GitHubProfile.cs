using System;
using Jsonzai.Attributes;
using Jsonzai.Test.Convertors;

namespace Jsonzai.Test.Model {
    public class GitHubProfile {
        public string Id { get; set; }
        public string Name { get; set; }
        [JsonConvert(typeof(JsonToDateTime))]public DateTime BirthDate { get; set; }
        [JsonConvert(typeof(JsonToUri))] public Uri Uri { get; set; }

        public GitHubProfile() {
        }

        public GitHubProfile(string id, string name, DateTime birthDate, Uri uri) {
            Id = id;
            Name = name;
            BirthDate = birthDate;
            Uri = uri;
        }
    }
}
