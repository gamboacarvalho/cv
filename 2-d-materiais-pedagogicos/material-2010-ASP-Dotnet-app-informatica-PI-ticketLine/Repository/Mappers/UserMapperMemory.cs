using System;
using System.Collections.Generic;
using System.Linq;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    /// <summary>
    /// Summary description for UserMapperMemory
    /// </summary>
    public class UserMapperMemory:IUserMapper
    {
        private readonly Dictionary<string , IUser> _hash;

        public UserMapperMemory()
        {
            _hash = new Dictionary<string, IUser>();
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

        public IUser Get(string id)
        {
            lock (_hash)
            {
                return _hash[id];
            }
        }

        public IEnumerable<IUser> GetRange(string from, string to)
        {
            throw new NotSupportedException();
        }

        public string Add(IUser item)
        {
            lock (_hash)
            {
                if (_hash.ContainsKey(item.UserName))
                    throw new InvalidOperationException("There are allready a user with that username. Choose a new one");
                _hash[item.UserName] = item;
                return item.UserName;
            }
        }

        public bool Update(string id, IUser info)
        {
            lock (_hash)
            {
                try
                {
                    User client = Get(id) as User;
                    client.Password = info.Password;
                    client.Name = info.Name;
                    client.Email = info.Email;
                    if (info.ImageData != null)
                    {
                        client.ImageMimeType = info.ImageMimeType;
                        client.ImageData = info.ImageData;
                    }

                    return true;
                }
                catch (Exception)
                {
                    return false;
                }
            }
        }

        public bool Remove(string id)
        {
            lock (_hash)
            {
                return _hash.Remove(id);
            }
        }

        public IUser[] GetAll()
        {
            lock (_hash)
            {
                return _hash.Values.ToArray();
            }
        }

        public bool IsValid(string username, string password)
        {
            lock (_hash)
            {
                return Get(username).Password == password.GetHashCode();
            }
        }
    }
}