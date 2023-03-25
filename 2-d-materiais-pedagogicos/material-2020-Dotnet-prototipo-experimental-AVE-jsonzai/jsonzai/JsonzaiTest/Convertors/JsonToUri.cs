using System;
using Jsonzai.Converters;

namespace Jsonzai.Test.Convertors {

    class JsonToUri : IConverter {

        public object Convert(string jsonString) {
            return new Uri(jsonString);
        }
    }
}
