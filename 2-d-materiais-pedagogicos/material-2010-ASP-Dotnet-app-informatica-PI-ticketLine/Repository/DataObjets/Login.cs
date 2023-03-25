using System;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    public class Login:ILogin
    {
        public string UserName { get; private set; }
        public DateTime Expires { get; set; }

        public Login(string username, DateTime expires)
        {
            UserName = username;
            Expires = expires;
        }
    }
}
