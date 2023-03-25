using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    public class Admin:User
    {
        public Admin(string uName, string password, string email, string name)
            : base(uName, password, email, name, new[] { "admin" })
        {
            
        }
    }
}
