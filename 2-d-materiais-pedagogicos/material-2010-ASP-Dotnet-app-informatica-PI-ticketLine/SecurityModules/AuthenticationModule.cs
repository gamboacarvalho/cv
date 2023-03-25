using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Principal;
using System.Text;
using System.Web;
using Repository;
using Repository.DataObjets;
using Repository.Mappers;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace SecurityModules
{
    public class AuthenticationModule : IHttpModule
    {
        private readonly ILoginMapper _loginMapper = DataRepository.LoginMapperFactory.GetMapper();
        private readonly IUserMapper _userMapper = DataRepository.UserMapperFactory.GetMapper();


        public void Init(HttpApplication context)
        {
            context.AuthenticateRequest += OnAuthenticateRequest;
        }

        private void OnAuthenticateRequest(object sender, EventArgs e)
        {
            HttpApplication application = sender as HttpApplication;
            HttpContext context = application.Context;

            HttpCookie cookie = context.Request.Cookies["login_cookie"];
            if (cookie == null)
                return;

            try
            {
                int id = int.Parse(cookie["sessionId"]);
                ILogin login = _loginMapper.Get(id);
                if (login.Expires.CompareTo(DateTime.Now) > 0)
                {
                    login.Expires = DateTime.Now.AddMinutes(30.0);

                    IUser user = _userMapper.Get(login.UserName);
                    context.User =
                        new GenericPrincipal(new GenericIdentity(user.UserName, "CustomAuthentication"),
                                             user.Roles);
                    return;
                }
                _loginMapper.Remove(id);
            }
            catch
            {
            }
        }

        public void Dispose()
        {
            // TODO: Nothing...
        }
    }
}
