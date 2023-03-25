using System.Collections.Generic;

namespace RepositoryInterfaces.Mappers
{
    /// <summary>
    /// Summary description for IEntityMapper
    /// </summary>
    public interface IEntityMapper<TEntity, TKey>
        where TEntity:class
    {
        int Size { get; }

        TEntity Get(TKey id);
        IEnumerable<TEntity> GetRange(TKey from, TKey to);
        TKey Add(TEntity item);
        bool Update(TKey id, TEntity info);
        bool Remove(TKey id);
        TEntity[] GetAll();
    }
}