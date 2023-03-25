using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    /// <summary>
    /// Summary description for UserMapperFactory
    /// </summary>
    public class UserMapperFactory:IUserMapperFactory
    {
        private readonly IUserMapper _userMapper = new UserMapperMemory();

        public IUserMapper GetMapper()
        {
            return _userMapper;
        }
    }
}