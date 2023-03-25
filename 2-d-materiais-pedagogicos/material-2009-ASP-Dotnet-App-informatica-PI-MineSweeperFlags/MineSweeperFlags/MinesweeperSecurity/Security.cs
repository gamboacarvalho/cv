using System;
using System.Web;
using System.Diagnostics;
using System.Threading;

namespace MinesweeperSecurity
{
    public class Security: IHttpModule
    {

        public Security() { }
        void context_BeginRequest(object sender, EventArgs e)
        {
            Console.WriteLine(Thread.CurrentThread.ManagedThreadId);
        }

        void OnAuthenticateRequest(object sender, EventArgs e)
        {
            if (sender == null) throw new ArgumentNullException("sender");
            HttpApplication context = (HttpApplication)sender;
        }

        void OnAuthorizeRequest(object sender, EventArgs e)
        {
            if (sender == null) throw new ArgumentNullException("sender");
            HttpApplication context = (HttpApplication)sender;
        }

        public void Init(HttpApplication ctx)
        {
            ctx.BeginRequest += new EventHandler(context_BeginRequest);

            ctx.AuthenticateRequest += new EventHandler(OnAuthenticateRequest);
            ctx.AuthorizeRequest    += new EventHandler(OnAuthorizeRequest);            
        }

        public void Dispose(){ /* do nothing */ }
    }
}
