using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Threading;
using System.Diagnostics;


namespace MineSweeperLog
{
    public class Log : IHttpModule
    {
        #region IHttpModule Members

        public void Dispose()
        {   
            // throw new NotImplementedException(); // comment by FMC
        }

        public void Init(HttpApplication app)
        {
            //app.BeginRequest += new EventHandler(app_BeginRequest);
            app.EndRequest += new EventHandler(app_EndRequest);         
        }
        void app_BeginRequest(object sender, EventArgs e)
        {
            if (sender == null) throw new ArgumentNullException("sender");
            LogEngine.Current.LogApplication(sender);
        }
        void app_EndRequest(object sender, EventArgs e)
        {
            if (sender == null) throw new ArgumentNullException("sender");
            LogEngine.Current.LogApplication(sender);            
        }
        #endregion        
    }
}
