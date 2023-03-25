namespace RepositoryInterfaces.Factories
{
    /// <summary>
    /// Summary description for IEntityMapperFactory
    /// </summary>
    public interface IEntityMapperFactory<out TEntityMapper, TEntity, TKey> where TEntity:class
    {
        TEntityMapper GetMapper();
    }
}