using System;
using Jsonzai.Attributes;
using Jsonzai.Test.Convertors;

namespace Jsonzai.Test.Model {
    public class RgbColorAnnotated {
        [JsonProperty("First")]  public short Red { get; set; }
        [JsonProperty("Second")] public short Green { get; set; }
        [JsonProperty("Third")]  public short Blue { get; set; }

        public string HexCode {
            get {
                return "" + Red.ToString("X") + Green.ToString("X") + Blue.ToString("X");
            }
            set {
                Red   = short.Parse((string)value.Substring(0, 2), System.Globalization.NumberStyles.HexNumber);
                Green = short.Parse((string)value.Substring(2, 2), System.Globalization.NumberStyles.HexNumber);
                Blue  = short.Parse((string)value.Substring(4, 2), System.Globalization.NumberStyles.HexNumber);
            }
        }

        public RgbColorAnnotated() {
        }
        public RgbColorAnnotated(short red, short green, short blue) {
            Red   = red;
            Blue  = blue;
            Green = green;
        }
    }
}
