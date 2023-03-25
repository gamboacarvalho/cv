using System;
using System.Collections.Generic;

namespace Webao
{
    public class MockRequest : IRequest
    {
        private readonly HttpRequest req = new HttpRequest();
        private readonly Dictionary<string, object> cache = new Dictionary<string, object>();

        public IRequest BaseUrl(string host) {
            req.BaseUrl(host);
            return this;
        }

        public IRequest AddParameter(string arg, string val)
        {
            req.AddParameter(arg, val);
            return this;
        }
        public object Get(string path, Type targetType)
        {
            object val;
            if (cache.TryGetValue(path, out val) == false) {
                val = req.Get(path, targetType);
                cache.Add(path, val);
            }
            return val;
        }
    }
}
