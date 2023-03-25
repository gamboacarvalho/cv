using System;
using System.Collections.Generic;
using System.Linq;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for ReserveMapper
    /// </summary>
    public class ReserveMapperMemory:IReserveMapper
    {
        private readonly Dictionary<int, IReserve> _hash;

        public ReserveMapperMemory()
        {
            _hash = new Dictionary<int, IReserve>();
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

        public IReserve Get(int id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<IReserve> GetRange(int from, int to)
        {
            lock (_hash)
            {
                IReserve[] reserves = new Reserve[to - from + 1];
                for (int i = from; i <= to; i++)
                {
                    if (_hash.ContainsKey(i))
                        reserves[i] = _hash[i];
                }
                return reserves;
            }
        }

        public int Add(IReserve item)
        {
            lock (_hash)
            {
                _hash[item.Id] = item;
                return item.Id;
            }
        }

        public bool Update(int id, IReserve info)
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

        public IReserve[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }

        public IEnumerable<IReserve> GetReservesByUser(string username)
        {
            lock (_hash)
            {
                return _hash.Values.Where(r => r.Username.Equals(username)); 
            }
        }
    }
}