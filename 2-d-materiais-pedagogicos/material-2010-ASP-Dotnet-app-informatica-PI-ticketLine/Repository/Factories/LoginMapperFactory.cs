using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    /// <summary>
    /// Summary description for UserMapperFactory
    /// </summary>
    public class LoginMapperFactory:ILoginMapperFactory
    {
        private readonly ILoginMapper _userMapper = new LoginMapperMemory();

        public ILoginMapper GetMapper()
        {
            return _userMapper;
        }
    }
}