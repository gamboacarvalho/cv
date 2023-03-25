using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Isel.Ave14.SqlMapper
{
    public interface IDataMapper<T> : IDataMapper where T : class
    {
        new ISqlEnumerable<T> GetAll();
        void Update(T val);
        void Delete(T val);
        void Insert(T val);
    }

    public interface IDataMapper
    {
        ISqlEnumerable GetAll();
        void Update(object val);
        void Delete(object val);
        void Insert(object val);
        string[] PkNames { get; }
    }

}
