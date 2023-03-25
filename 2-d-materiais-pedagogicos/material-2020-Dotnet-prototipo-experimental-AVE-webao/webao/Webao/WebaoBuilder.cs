using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Webao.Attributes;

namespace Webao
{
    public class WebaoBuilder
    {
        public static AbstractAccessObject Build(Type webao, IRequest req) {
            object[] attrs = webao.GetCustomAttributes(typeof(BaseUrlAttribute), true);
            if (attrs != null && attrs.Length > 0) {
                string host = ((BaseUrlAttribute)attrs[0]).host;
                req.BaseUrl(host);
            }
            attrs = webao.GetCustomAttributes(typeof(AddParameterAttribute), true);
            foreach (var at in attrs)
            {
                AddParameterAttribute a = (AddParameterAttribute)at;
                req.AddParameter(a.name, a.val);
            }

            return (AbstractAccessObject) Activator.CreateInstance(webao, req);
        }
    }
}
