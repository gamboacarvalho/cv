
namespace RepositoryInterfaces.DataObjects
{
    /// <summary>
    /// Summary description for Room
    /// </summary>
    public interface IRoom
    {
        string Id { get; }

        int Seats { get; }

        int Rows { get; }

        int[] Layout { get; }
    }
}