using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace SqlReflect
{
    interface IPropertyMapper
    {
        string Column { get;  }
        object Set(IDataReader dr, object target);
        object Get(object target);
        bool IsPk { get; }
    }
}
