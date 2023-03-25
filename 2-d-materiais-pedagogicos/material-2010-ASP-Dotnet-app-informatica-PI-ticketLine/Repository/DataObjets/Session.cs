using System;
using System.Collections.Generic;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    /// <summary>
    /// Summary description for Session
    /// </summary>
    public class Session:ISession
    {
        private static int _id;

        public Session(int year, int month, int day, int hour, IRoom room, IShow show, double price)
        {
            Id = _id++;
            Year = year;
            Month = month;
            Day = day;
            Hour = hour;
            Room = room;
            Show = show;
            UsedSeats = new List<int>();
            TicketPrice = price;
        }

        public int Id { get; private set; }

        public string Name
        {
            get
            {
                return Date;
            }
        }

        public int Year { get; private set; }

        public int Month { get; private set; }

        public int Day { get; private set; }

        public int Hour { get; private set; }

        public string Date
        {
            get
            {
                return String.Format("{0}/{1}/{2} {3}:00", Year, Month, Day, Hour);
            }
        }

        public IRoom Room { get; private set; }

        public IShow Show { get; private set; }

        public IList<int> UsedSeats { get; private set; }

        public double TicketPrice { get; private set; }
    }
}