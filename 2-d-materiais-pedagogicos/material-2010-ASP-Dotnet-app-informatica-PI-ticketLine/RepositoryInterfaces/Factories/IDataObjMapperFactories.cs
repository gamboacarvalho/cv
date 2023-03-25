using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace RepositoryInterfaces.Factories
{
    public interface IReserveMapperFactory:IEntityMapperFactory<IReserveMapper, IReserve, int>
    {
        
    }

    public interface IRoomMapperFactory:IEntityMapperFactory<IRoomMapper, IRoom, string >
    {
        
    }

    public interface ISessionMapperFactory:IEntityMapperFactory<ISessionMapper, ISession, int>
    {
        
    }

    public interface IShowMapperFactory:IEntityMapperFactory<IShowMapper, IShow, int>
    {
        
    }

    public interface IUserMapperFactory:IEntityMapperFactory<IUserMapper, IUser, string >
    {
        
    }

    public interface ILoginMapperFactory:IEntityMapperFactory<ILoginMapper, ILogin, int>
    {
        
    }

    public interface ICommentMapperFactory
    {
        ICommentMapper GetMapper(); 
    }
}