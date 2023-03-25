using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Clima
{
    public class WeatherInfo
    {
        public DateTime Date { get;  }
        public int TempC { get; }
        public double PrecipMM { get; }
        public String Desc { get; }

        public WeatherInfo(DateTime date, int tempC, double precipMM, string desc)
        {
            this.Date = date;
            this.TempC = tempC;
            this.PrecipMM = precipMM;
            this.Desc = desc;
        }

        public override String ToString()
        {
            return "WeatherInfo{" +
                "date=" + Date +
                ", tempC=" + TempC +
                ", precipMM=" + PrecipMM +
                ", desc='" + Desc + '\'' +
                '}';
        }

    }
}
