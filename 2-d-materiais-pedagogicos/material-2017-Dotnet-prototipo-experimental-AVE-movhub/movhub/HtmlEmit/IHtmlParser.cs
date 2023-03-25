using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HtmlEmit
{
    public interface IHtmlParser
    {
        string ParseToHtml(object target);
    }

    public abstract class HtmlDetails : IHtmlParser
    {
        public abstract string ParseToHtml(object target);
    }

    public abstract class HtmlTable<T> : IHtmlParser
    {
        public string ParseToHtml(object obj)
        {
            IEnumerable<T> target = (IEnumerable<T>)obj;
            string ret = ParseToHtmlHeaders();
            foreach(object t in target)
            {
                ret += ParseToHtmlArr(t);
            }
            return ret += "</tbody>" +
                      "</table>";
        }
        public abstract string ParseToHtmlArr(object obj);
        public abstract string ParseToHtmlHeaders();
    }
}