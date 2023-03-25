using System;
using WebStressTool.Proxy;

namespace WebStressTool
{
    internal class RequestItemEventArgs: EventArgs
    {
        RequestItem requestItem = null;

        public RequestItemEventArgs(RequestItem requestItem){ this.requestItem = requestItem; }

        public RequestItem RequestItem { get { return requestItem; } }
    }
}
