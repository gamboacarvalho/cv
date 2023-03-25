using System;

namespace Jsonzai.Attributes {

    public class JsonConvertAttribute : Attribute {

        public Type ConverterType { get; private set; }

        public JsonConvertAttribute(Type type) {
            ConverterType = type;
        }
    }
}
