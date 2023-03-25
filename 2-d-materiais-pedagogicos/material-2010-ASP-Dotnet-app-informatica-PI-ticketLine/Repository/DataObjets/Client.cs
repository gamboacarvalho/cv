using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    /// <summary>
    /// Summary description for client
    /// </summary>
    public class Client : User
    {
        public Client(string password)
        {
            Password = password.GetHashCode();
        }

        public Client(string uName, string password, string email, string name)
            : base(uName, password, email, name, new[] { "client" })
        {
            
        }
    }
}