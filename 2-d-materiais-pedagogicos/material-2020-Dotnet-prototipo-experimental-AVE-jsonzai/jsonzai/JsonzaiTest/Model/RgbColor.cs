namespace Jsonzai.Test.Model {
    public class RgbColor {
        public short Red { get; set; }
        public short Green { get; set; }
        public short Blue { get; set; }
        public string HexCode {
            get {
                return "#" + Red.ToString("X") + Green.ToString("X") + Blue.ToString("X");
            }
            set {
                Red   = short.Parse((string)value.Substring(1, 2), System.Globalization.NumberStyles.HexNumber);
                Green = short.Parse((string)value.Substring(3, 2), System.Globalization.NumberStyles.HexNumber);
                Blue  = short.Parse((string)value.Substring(5, 2), System.Globalization.NumberStyles.HexNumber);
            }
        }

        public RgbColor() {
        }

        public RgbColor(short red, short green, short blue) {
            Red   = red;
            Blue  = blue;
            Green = green;
        }
    }
}
