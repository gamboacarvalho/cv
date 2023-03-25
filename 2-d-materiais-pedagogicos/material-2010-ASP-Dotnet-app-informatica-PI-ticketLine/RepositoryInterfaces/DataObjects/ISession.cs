using System.Collections.Generic;

namespace RepositoryInterfaces.DataObjects
{
    /// <summary>
    /// Summary description for Session
    /// </summary>
    public interface ISession
    {
        int Id { get; }

        string Name { get; }

        int Year { get; }

        int Month { get; }

        int Day { get; }

        int Hour { get; }

        string Date { get; }

        IRoom Room { get; }

        IShow Show { get; }

        IList<int> UsedSeats { get; }

        double TicketPrice { get; }
    }
}