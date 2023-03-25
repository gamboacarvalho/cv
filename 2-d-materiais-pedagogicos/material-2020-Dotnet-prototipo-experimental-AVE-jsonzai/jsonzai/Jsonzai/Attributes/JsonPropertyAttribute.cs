using System;

namespace Jsonzai.Attributes {

    public class JsonPropertyAttribute : Attribute {

        public string Name { get; set; }

        public JsonPropertyAttribute(string name) {
            Name = name;
        }
    }
}
