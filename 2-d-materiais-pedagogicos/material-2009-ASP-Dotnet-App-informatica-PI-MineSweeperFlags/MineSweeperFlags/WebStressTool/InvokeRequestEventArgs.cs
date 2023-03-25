using System;
using System.IO;

namespace WebStressTool
{
    internal class InvokeRequestEventArgs: EventArgs
    {
        FileInfo requestItem;

        public InvokeRequestEventArgs(FileInfo requestItem) { this.requestItem = requestItem;  }

        public FileInfo RequestItem { get { return requestItem; } }
    }
}
