using System.Collections.Generic;
using RepositoryInterfaces.DataObjects;

namespace TicketsOnline.Models
{
    public class CartIndexModel
    {
        public int SessionId { get; set; }
        public int[] PreReserveSeats { get; set; }
        public string ShowName { get; set; }
        public string SessionName { get; set; }
        public double TicketPrice { get; set; }
    }

    public class CartReserveModel
    {
        public string ShowName { get; set; }
        public string SessionName { get; set; }
        public bool Reserved { get; set; }
    }

    public class AdminModel
    {
        public IList<IUser> Admins { get; set; }
        public IList<IUser> Clients { get; set; }

        public AdminModel()
        {
            Admins = new List<IUser>();
            Clients = new List<IUser>();
        }
    }
}