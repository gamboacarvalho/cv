using System;
using System.IO;

namespace WebStressTool.HttpClient
{
    internal class EndRequestArgs : EventArgs
    {
        StreamReader dataReader = null;
        StreamWriter dataWriter = null;
        RequestState state      = RequestState.Unknown;


        public EndRequestArgs(StreamReader dataReader, StreamWriter dataWriter, RequestState state)
        {
            this.state      = state;
            this.dataReader = dataReader;
            this.dataWriter = dataWriter;
        }

        public StreamReader DataReader { get { return dataReader; } }
        public StreamWriter DataWriter { get { return dataWriter; } }
        public RequestState State      { get { return state;      } }

    }
}
