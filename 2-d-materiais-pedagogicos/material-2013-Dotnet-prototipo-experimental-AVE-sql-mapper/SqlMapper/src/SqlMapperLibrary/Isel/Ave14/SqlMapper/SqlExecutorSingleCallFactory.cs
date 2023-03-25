using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    public class SqlExecutorSingleCallFactory : ISqlExecutorFactory
    {
        private string connStr;

        public SqlExecutorSingleCallFactory(string connStr)
        {
            this.connStr = connStr;
        }

        public ISqlExecutor Executor()
        {
            return new SqlExecutorSingleCall(connStr);
        }

        public void Dispose()
        {
            // Nothing to do. Every connection is disposed after a command execution.
        }
    }
}
