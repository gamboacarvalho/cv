using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    public class SessionMapperFactory:ISessionMapperFactory
    {
        private readonly ISessionMapper _sessionMapper = new SessionMapperMemory();

        public ISessionMapper GetMapper()
        {
            return _sessionMapper;
        }
    }
}
