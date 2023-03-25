using System;

namespace Jsonzai.Converters {
    public class Converter<R> : IConverter {

        private Func<string, R> conv;

        public Converter(Func<string, R> conv) {
            this.conv = conv;
        }

        public object Convert(string jsonString) {
            return conv(jsonString);
        }
    }
}
