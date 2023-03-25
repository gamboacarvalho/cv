using System;
using System.Collections.Generic;
using System.Linq;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for RoomMapperMemory
    /// </summary>
    public class RoomMapperMemory:IRoomMapper
    {
        private readonly Dictionary<string , IRoom> _hash;

        public RoomMapperMemory()
        {
            _hash = new Dictionary<string, IRoom>();
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

        public IRoom Get(string id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<IRoom> GetRange(string from, string to)
        {
            throw new NotSupportedException();
        }

        public string Add(IRoom item)
        {
            lock (_hash)
            {
                _hash[item.Id] = item;
                return item.Id;
            }
        }

        public bool Update(string id, IRoom info)
        {
            throw new NotImplementedException();
        }

        public bool Remove(string id)
        {
            lock (_hash)
            {
                return _hash.Remove(id);
            }
        }

        public IRoom[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }
    }
}