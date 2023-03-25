using System;

namespace HtmlEmit
{
    public class HTMLUtils
    {

        public static string GetHtml(string name, object value)
        {
            return "<li class='list-group-item'><strong>" + name + "</strong>: " + value + "</li>";
        }

        public static string GetHtmlToLink(string name, object value,string htmlAs)
        {
            if (htmlAs.Contains("{value}"))
            {
                htmlAs = htmlAs.Replace("{value}", value + "");
            }
            if (htmlAs.Contains("{name}"))
            {
                htmlAs = htmlAs.Replace("{name}", name);
            }
            return htmlAs;
        }

        public static string GetHtmlHeader(string name)
        {
            return "<th>" + name + "</th>";
        }

        public static string GetHtmlArrayToLink(object value, string name, string htmlAs)
        {
            return GetHtmlToLink(name,value,htmlAs);
        }

        public static string GetHtmlArray(object value)
        {
            return "<td>" + value + "</td>";
        }
    }
}
