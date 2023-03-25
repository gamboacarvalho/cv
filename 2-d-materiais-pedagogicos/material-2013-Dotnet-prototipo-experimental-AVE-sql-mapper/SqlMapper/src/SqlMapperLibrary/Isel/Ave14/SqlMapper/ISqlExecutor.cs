using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;

namespace Isel.Ave14.SqlMapper
{
    public interface ISqlExecutor : IDisposable
    {
        SqlDataReader ExecuteReader(string cmd);
    }
}
