using System;
using System.Collections.Generic;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    /// <summary>
    /// Summary description for Show
    /// </summary>
    public class Show:IShow
    {
        private static int _id;

        public Show(string name, string desc)
        {
            Id = _id++;
            Name = name;
            Description = desc;
            Ratings = new List<IRating>();
        }

        public int Id { get; private set; }

        public string Name { get; private set; }

        public string Description { get; private set; }

        public int Rating { get; set; }

        public IList<IRating> Ratings { get; private set; }
    }

    public class UserRating : IRating
    {
        #region Implementation of IRating

        public string UserName { get; private set; }

        public int Rating { get; private set; }

        #endregion

        public UserRating(string userName, int rating)
        {
            UserName = userName;
            Rating = rating;
        }
    }
}