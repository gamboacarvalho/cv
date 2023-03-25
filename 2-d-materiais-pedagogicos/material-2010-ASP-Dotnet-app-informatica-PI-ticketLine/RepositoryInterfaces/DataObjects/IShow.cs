
using System;
using System.Collections.Generic;

namespace RepositoryInterfaces.DataObjects
{
    /// <summary>
    /// Summary description for Show
    /// </summary>
    public interface IShow
    {
        int Id { get; }

        string Name { get; }

        string Description { get; }

        int Rating { get; set; }

        IList<IRating> Ratings { get; }
    }

    public interface IRating
    {
        string UserName { get; }
        int Rating { get; }
    }
}