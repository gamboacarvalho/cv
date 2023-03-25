using System;
using WebStressTool.HttpClient;

namespace WebStressTool
{
    internal class EndRequestEventArgs: EventArgs 
    {
        RequestState state = RequestState.Unknown;

        public EndRequestEventArgs( RequestState state )
        {
            this.state      = state;
        }

        public RequestState State { get { return state;      } }
    }
}
