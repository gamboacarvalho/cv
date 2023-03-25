using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace DAOBuilder
{
    internal class ObjectIEnumerable<T>:IEnumerable<T>
    {
        private readonly IEnumerable<object> _objects; 
        public ObjectIEnumerable(IEnumerable<object> objects)
        {
            _objects = objects;
        }
        public IEnumerator<T> GetEnumerator()
        {
            return _objects.Cast<T>().GetEnumerator();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}
