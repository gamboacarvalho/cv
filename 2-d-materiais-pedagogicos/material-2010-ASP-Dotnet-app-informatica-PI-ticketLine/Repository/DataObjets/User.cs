using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    public class User:IUser
    {
        public User(){}

        public User(string uName, string password, string email, string name, string [] roles)
        {
            UserName = uName;
            Password = password.GetHashCode();
            Email = email;
            Name = name;
            Roles = roles;
        }

        public string UserName { get; private set; }

        public int Password { get; set; }

        public string Email { get; set; }
        public string Name { get; set; }
        public string[] Roles { get; private set; }


        public byte[] ImageData { get; set; }
        public string ImageMimeType { get; set; }
    }
}
