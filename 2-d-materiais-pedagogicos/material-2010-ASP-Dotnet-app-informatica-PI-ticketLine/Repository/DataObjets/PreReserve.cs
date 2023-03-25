using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    public class PreReserve:IPreReserve
    {
        public int Session { get; private set; }

        public IList<int> Seats { get; private set; }

        public PreReserve(int session)
        {
            Session = session;
            Seats = new List<int>();
        }
    }
}
