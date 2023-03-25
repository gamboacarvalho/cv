using System;
using System.Collections.Generic;
using System.Linq;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for SessionMapperMemory
    /// </summary>
    public class SessionMapperMemory:ISessionMapper
    {
        private readonly Dictionary<int, ISession> _hash;

        public SessionMapperMemory()
        {
            _hash = new Dictionary<int, ISession>();
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

        public ISession Get(int id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<ISession> GetRange(int from, int to)
        {
            lock (_hash)
            {
                ISession[] reserves = new Session[to - from + 1];
                for (int i = from; i <= to; i++)
                {
                    if (_hash.ContainsKey(i))
                        reserves[i] = _hash[i];
                }
                return reserves;
            }
        }

        public int Add(ISession item)
        {
            lock (_hash)
            {
                _hash[item.Id] = item;
                return item.Id;
            }
        }

        public bool Update(int id, ISession info)
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

        public ISession[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }

        public bool LockSeats(int id, IEnumerable<int> seats)
        {
            lock (_hash)
            {
                ISession session = Get(id);
                bool sucess = seats.Count() != 0 && session.UsedSeats.Intersect(seats).Count() == 0;
                if (sucess)
                {
                    foreach (int i in seats)
                    {
                        session.UsedSeats.Add(i);
                    }
                }

                return sucess;
            }
        }

        public void UnlockSeats(int id, IEnumerable<int> seats)
        {
            lock (_hash)
            {
                ISession session = Get(id);
                foreach (int seat in seats)
                {
                    session.UsedSeats.Remove(seat);
                }
            }
        }
    }
}