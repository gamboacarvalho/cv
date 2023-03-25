using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Web;
using System.IO;
using System.Xml;
using System.Runtime.CompilerServices;

namespace MineSweeperLog
{
    public class LogEngine
    {
        public static LogEngine Current;
        
        static LogEngine()
        {
            if (Current == null) Current = new LogEngine();
        }

        //LogEngine() { }
        
        public void LogApplication(object application)
        {
            //if (application == null) throw new ArgumentNullException("application");
            if (application == null) return;
            HttpApplication app = (HttpApplication)application;            
            Logger l = new Logger(app);
            IAsyncResult res = l.Delegate.BeginInvoke(l.CallBack, l.Delegate); // FMC ???? passa  o delegate como parametro ?????
        }
    }
}
