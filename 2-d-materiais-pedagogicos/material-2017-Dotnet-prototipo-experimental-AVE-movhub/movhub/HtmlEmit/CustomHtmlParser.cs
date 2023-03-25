using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HtmlEmit
{
    class ForTypeDetails<T> : IHtmlParser
    {
        static Func<T, string> transf;
        public ForTypeDetails(Func<T, string> func)
        {
            transf = func;
        }
        public string ParseToHtml(object target)
        {
            return transf((T)target);
        }
    }

    class ForTypeInTable<T> : IHtmlParser
    {
        static IEnumerable<string> headers;
        static Func<T, string> transf;
        public ForTypeInTable(IEnumerable<string> hd, Func<T, string> func)
        {
            headers = hd;
            transf = func;
        }
        public string ParseToHtml(object obj)
        {
            string ret = "<table class='table table-hover'>" +
                            "<thead>" +
                                "<tr>";
            foreach (string str in headers)
            {
                ret += "<th>" + str + "</th>";
            }
            ret += "</tr></thead><tbody>";
            IEnumerable<T> target = (IEnumerable<T>)obj;
            foreach (T t in target)
            {
                ret += transf(t);
            }
            return ret += "</tbody></table>";
        }
    }

    class ForSequenceOf<T> : IHtmlParser
    {
        static Func<IEnumerable<T>, string> transf;
        public ForSequenceOf(Func<IEnumerable<T>, string> func)
        {
            transf = func;
        }
        public string ParseToHtml(object obj)
        {
            IEnumerable<T> target;
            if (obj is IEnumerable<T>)
            {
                target = (IEnumerable<T>)obj;
            }
            else
            {
                target = new T[] { (T)obj };
            }
            return transf(target);
        }
    }
}
