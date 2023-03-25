using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    /// <summary>
    /// Summary description for ShowMapperFactory
    /// </summary>
    public class ShowMapperFactory:IShowMapperFactory
    {
        private readonly IShowMapper _showMapper = new ShowMapperMemory();

        public IShowMapper GetMapper()
        {
            return _showMapper;
        }
    }
}