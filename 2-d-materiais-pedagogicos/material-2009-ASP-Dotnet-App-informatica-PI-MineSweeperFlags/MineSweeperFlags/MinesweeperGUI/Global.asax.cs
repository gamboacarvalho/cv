using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;


namespace MinesweeperGUI
{
    public class Global : System.Web.HttpApplication
    {

        protected void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");


            Route route = new Route("{controller}/{action}", new MvcRouteHandler());
            route.Defaults = new RouteValueDictionary(new { controller = "Game", action = "Start" });
            
            routes.Add(route); 
        }


        protected void Application_Start(object sender, EventArgs e)
        {
            RegisterRoutes(RouteTable.Routes);
        }
    }
}