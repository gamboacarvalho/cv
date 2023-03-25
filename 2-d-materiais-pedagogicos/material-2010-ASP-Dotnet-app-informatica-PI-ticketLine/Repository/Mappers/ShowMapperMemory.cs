using System;
using System.Collections.Generic;
using System.Linq;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for IShowMapperMemory
    /// </summary>
    public class ShowMapperMemory : IShowMapper
    {
        private readonly Dictionary<int, IShow> _hash;

        public ShowMapperMemory()
        {
            _hash = new Dictionary<int, IShow>();
        }

        public int Size
        {
            get
            {
                lock (_hash)
                {
                    return _hash.Count;
                }
            }
        }

        public IShow Get(int id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<IShow> GetRange(int from, int to)
        {
            lock (_hash)
            {
                IShow[] reserves = new Show[to - from + 1];
                for (int i = from; i <= to; i++)
                {
                    if (_hash.ContainsKey(i))
                        reserves[i] = _hash[i];
                }
                return reserves;
            }
        }

        public int Add(IShow item)
        {
            lock (_hash)
            {
                _hash[item.Id] = item;
                return item.Id;
            }
        }

        public bool Update(int id, IShow info)
        {
            throw new NotImplementedException();
        }

        public bool Remove(int id)
        {
            lock (_hash)
            {
                return _hash.Remove(id);
            }
        }

        public IShow[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }

        public bool RateMovie(int id, IRating rating)
        {
            lock (_hash)
            {
                IShow show = _hash[id];
                bool mustRate = !show.Ratings.Any(r => r.UserName.Equals(rating.UserName));
                if (mustRate)
                {
                    show.Ratings.Add(rating);
                    double sum = show.Ratings.Sum(r => r.Rating);
                    sum = sum/show.Ratings.Count;

                    int finalRating = (int) sum;
                    if (sum - finalRating >= 0.5)
                        ++finalRating;

                    show.Rating = finalRating;
                }
                return mustRate;
            }
        }
    }
}