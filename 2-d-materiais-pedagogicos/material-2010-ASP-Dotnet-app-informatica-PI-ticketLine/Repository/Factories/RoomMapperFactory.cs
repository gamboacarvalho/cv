using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    /// <summary>
    /// Summary description for RoomMapperFactory
    /// </summary>
    public class RoomMapperFactory:IRoomMapperFactory
    {
        private readonly IRoomMapper _roomMapper = new RoomMapperMemory();

        public IRoomMapper GetMapper()
        {
            return _roomMapper;
        }
    }
}