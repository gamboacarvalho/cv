
namespace RepositoryInterfaces.DataObjects
{
    /// <summary>
    /// Summary description for IReserve
    /// </summary>
    public interface IReserve
    {
        int Id { get; }

        int[] Seats { get; }

        string Username { get; }

        ISession Session { get; }
    }
}