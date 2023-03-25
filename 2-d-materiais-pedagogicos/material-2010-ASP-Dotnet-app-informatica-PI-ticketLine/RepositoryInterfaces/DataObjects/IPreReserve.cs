using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RepositoryInterfaces.DataObjects
{
    public interface IPreReserve
    {
        int Session { get; }
        IList<int> Seats { get; }
    }
}
