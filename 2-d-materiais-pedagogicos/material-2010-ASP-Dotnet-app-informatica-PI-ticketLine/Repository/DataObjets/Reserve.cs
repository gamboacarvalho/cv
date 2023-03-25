using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    /// <summary>
    /// Summary description for Reserve
    /// </summary>
    public class Reserve:IReserve
    {
        private static int _id;

        public Reserve(int[] seats, string username, ISession session)
        {
            Id = _id++;
            Seats = seats;
            Username = username;
            Session = session;
        }

        public int Id { get; private set; }

        public int[] Seats { get; private set; }

        public string Username { get; private set; }

        public ISession Session { get; private set; }
    }
}