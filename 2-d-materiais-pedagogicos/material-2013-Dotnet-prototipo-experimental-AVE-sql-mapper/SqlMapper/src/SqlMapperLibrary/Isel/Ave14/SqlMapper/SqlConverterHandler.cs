using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    public delegate bool SqlConverterHandler<T>(SqlDataReader reader, out T obj);
}
