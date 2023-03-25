using System;

namespace RepositoryInterfaces.DataObjects
{
    public interface ILogin
    {
        string UserName { get; }
        DateTime Expires { get; set; }
    }
}
