using System;
using System.Collections.Generic;
using System.Linq;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for UserMapperMemory
    /// </summary>
    public class LoginMapperMemory:ILoginMapper
    {
        private readonly Dictionary<int , ILogin> _hash;

        public LoginMapperMemory()
        {
            _hash = new Dictionary<int, ILogin>();
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

        public ILogin Get(int id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<ILogin> GetRange(int from, int to)
        {
            throw new NotSupportedException();
        }

        public int Add(ILogin item)
        {
            lock (_hash)
            {
                Random random = new Random();
                while (true)
                {
                    int id = random.Next(int.MaxValue);
                    if (!_hash.ContainsKey(id))
                    {
                        _hash.Add(id, item);
                        return id;
                    }
                }
            }
        }

        public bool Update(int id, ILogin info)
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

        public ILogin[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }
    }
}