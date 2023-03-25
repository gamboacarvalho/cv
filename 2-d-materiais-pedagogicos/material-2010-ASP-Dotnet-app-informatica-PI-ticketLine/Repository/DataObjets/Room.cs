using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    /// <summary>
    /// Summary description for Room
    /// </summary>
    public class Room:IRoom
    {
        public Room(string id, int[] layout, int rows, int seats)
        {
            Id = id;
            Layout = layout;
            Rows = rows;
            Seats = seats;
        }

        public string Id { get; private set; }

        public int Seats { get; private set; }

        public int Rows { get; private set; }

        public int[] Layout { get; set; }
    }
}