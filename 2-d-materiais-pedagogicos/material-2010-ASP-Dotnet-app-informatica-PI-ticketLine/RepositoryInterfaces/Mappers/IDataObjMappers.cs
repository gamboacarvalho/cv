using System;
using System.Collections.Generic;
using RepositoryInterfaces.DataObjects;

namespace RepositoryInterfaces.Mappers
{
    /// <summary>
    /// Summary description for ISessionMapper
    /// </summary>
    public interface ISessionMapper:IEntityMapper<ISession, int>
    {
        bool LockSeats(int id, IEnumerable<int> seats);
        void UnlockSeats(int id, IEnumerable<int> seats);
    }

    public interface IRoomMapper:IEntityMapper<IRoom, string >
    {
        
    }

    public interface IReserveMapper:IEntityMapper<IReserve, int>
    {
        IEnumerable<IReserve> GetReservesByUser(string username);
    }

    public interface IShowMapper:IEntityMapper<IShow, int>
    {
        bool RateMovie(int id, IRating rating);
    }

    public interface IUserMapper:IEntityMapper<IUser, string >
    {
        bool IsValid(string username, string password);
    }

    public interface ILoginMapper:IEntityMapper<ILogin, int>
    {
        
    }

    public interface ICommentMapper
    {
        int Size { get; }
        IEnumerable<IComment> GetByShow(int session);
        void Add(IComment item);
        IEnumerable<IComment> GetAll();
    }
}