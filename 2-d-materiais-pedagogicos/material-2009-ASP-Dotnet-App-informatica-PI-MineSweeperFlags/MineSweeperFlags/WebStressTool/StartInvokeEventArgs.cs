using System;

namespace WebStressTool
{
    internal class StartInvokeEventArgs: EventArgs
    {
        int totalRequestFiles = 0;

        public StartInvokeEventArgs(int totalRequestFiles)
        {
            this.totalRequestFiles = totalRequestFiles;
        }

        public int TotalRequestFiles { get { return totalRequestFiles; } }

    }
}
