using System;
using Jsonzai.Converters;

namespace Jsonzai.Test.Convertors {

    public class JsonToDateTime : IConverter {

        public object Convert(string jsonString) {
            return DateTime.Parse(jsonString);
        }
    }
}
