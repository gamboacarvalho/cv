using System;
using System.Reflection;
using System.Collections.Generic;
using HtmlReflect.MyCustomAttributes;


namespace HtmlReflect
{
    interface IGetter
    {
        string GetHtml(object target);
        string GetHtmlHeader();
        string GetHtmlArray(object target);
    }

    class GetHtmlNotIgnored : IGetter
    {
        PropertyInfo pi;

        public GetHtmlNotIgnored(PropertyInfo pi)
        {
            this.pi = pi;
        }

        public string GetHtml(object target)
        {
            return "<li class='list-group-item'><strong>" + pi.Name + "</strong>: " + pi.GetValue(target) + "</li>";
        }

        public string GetHtmlArray(object target)
        {
            return "<td>" + pi.GetValue(target) + "</td>";
        }

        public string GetHtmlHeader()
        {
            return "<th>" + pi.Name + "</th>";
        }
    }

    class GetHtmlAsAttribute : IGetter
    {
        PropertyInfo pi;

        public GetHtmlAsAttribute(PropertyInfo pi)
        {
            this.pi = pi;
        }

        public string GetHtml(object target)
        {
            string aux = ((HtmlAsAttribute)pi.GetCustomAttribute(typeof(HtmlAsAttribute))).template;
            if (aux.Contains("{value}"))
            {
                aux = aux.Replace("{value}", pi.GetValue(target) + "");
            }
            if (aux.Contains("{name}"))
            {
                aux = aux.Replace("{name}", pi.Name);
            }
            return aux;
        }

        public string GetHtmlArray(object target)
        {
            string aux = ((HtmlAsAttribute)pi.GetCustomAttribute(typeof(HtmlAsAttribute))).template;
            if (aux.Contains("{value}"))
            {
                aux = aux.Replace("{value}", pi.GetValue(target) + "");
            }
            if (aux.Contains("{name}"))
            {
                aux = aux.Replace("{name}", pi.Name);
            }
            return aux;
        }

        public string GetHtmlHeader()
        {
            return "<th>" + pi.Name + "</th>";
        }
    }

    public class Htmlect
    {

        static Dictionary<Type, List<IGetter>> dictionary = new Dictionary<Type, List<IGetter>>();

        public string ToHtml(object obj)
        {
            List<IGetter> list;
            Type type = obj.GetType();
            if (!dictionary.TryGetValue(type, out list)) list = addTypeToDictionary(type);
            string res = "<ul class='list-group'>";
            foreach (IGetter ig in list)
            {
                res += ig.GetHtml(obj);
            }
            return res += "</ul>";
        }


        public string ToHtml(object[] arr)
        {
            List<IGetter> list;
            Type type = arr.GetType().GetElementType();
            if (!dictionary.TryGetValue(type, out list)) list = addTypeToDictionary(type);

            string res = "<table class='table table-hover'>" +
                            "<thead><tr>";

            foreach (IGetter ig in list)
            {
                res += ig.GetHtmlHeader();
            }

            res += "</tr>" +
                 "</thead>" +
                 "<tbody>";

            foreach (object o in arr)
            {
                res += "<tr>";
                foreach (IGetter ig in list)
                {
                    res += ig.GetHtmlArray(o);
                }
                res += "</tr>";
            }

            return res += "</tbody>" +
                          "</table>";
        }

        private List<IGetter> addTypeToDictionary(Type type)
        {
            List<IGetter> list = new List<IGetter>();
            PropertyInfo[] propertyInfo = type.GetProperties();
            foreach (PropertyInfo p in propertyInfo)
            {

                if (p.IsDefined(typeof(HtmlAsAttribute)))
                {
                    list.Add(new GetHtmlAsAttribute(p));
                }
                else if (!p.IsDefined(typeof(HtmlIgnoreAttribute)))
                {
                    list.Add(new GetHtmlNotIgnored(p));
                }

            }
            dictionary.Add(type, list);
            return list;
        }
    }
}
