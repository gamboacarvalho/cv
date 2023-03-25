using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    /// <summary>
    /// Summary description for ReserveMapperFactory
    /// </summary>
    public class ReserveMapperFactory:IReserveMapperFactory
    {
        private readonly IReserveMapper _reserveMapper = new ReserveMapperMemory();
        public IReserveMapper GetMapper()
        {
            return _reserveMapper;
        }
    }
}