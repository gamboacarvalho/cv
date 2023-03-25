using System;

namespace HtmlReflect.MyCustomAttributes
{
    public class HtmlAsAttribute : Attribute
    {

        public string template { get; set; }

        public HtmlAsAttribute(string htmlTemplate)
        {
            template = htmlTemplate;
        }

    }
}
