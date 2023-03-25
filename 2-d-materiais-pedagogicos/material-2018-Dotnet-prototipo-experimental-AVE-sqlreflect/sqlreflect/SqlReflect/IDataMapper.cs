using System.Collections;
using System.Collections.Generic;

namespace SqlReflect
{
    public interface IDataMapper
    {
        /// <summary>
        /// Returns a domain object with given id.
        /// </summary>
        object GetById(object id);
        /// <summary>
        /// Returns all rows corresponding table as domain objects.
        /// </summary>
        IEnumerable GetAll();
        /// <summary>
        /// Inserts given target domain object into corresponding table.
        /// </summary>
        /// <returns>The entity value of the primary key column.</returns>
        object Insert(object target);
        /// <summary>
        /// Updates the corresponding table row with tha values of given target domain object.
        /// </summary>
        void Update(object target);
        /// <summary>
        /// Removes the row of the table corresponding to the target domain object.
        /// </summary>
        void Delete(object target);
    }

    public interface IDataMapper<K, V> : IDataMapper
    {
        /// <summary>
        /// Returns a domain object with given id.
        /// </summary>
        V GetById(K id);
        /// <summary>
        /// Returns all rows corresponding table as domain objects.
        /// </summary>
        new IEnumerable<V> GetAll();
        /// <summary>
        /// Inserts given target domain object into corresponding table.
        /// </summary>
        /// <returns>The entity value of the primary key column.</returns>
        K Insert(V target);
        /// <summary>
        /// Updates the corresponding table row with tha values of given target domain object.
        /// </summary>
        void Update(V target);
        /// <summary>
        /// Removes the row of the table corresponding to the target domain object.
        /// </summary>
        void Delete(V target);
    }
}
