using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    public interface ISqlExecutorFactory : IDisposable
    {
        ISqlExecutor Executor();
    }
}
