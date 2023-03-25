using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    class SqlEnumerator<T> : IEnumerator<T> where T : class
    {
        private readonly ISqlExecutor executor;
        private readonly SqlDataReader reader;
        private readonly SqlConverterHandler<T> converter;
        private T currElem;

        public SqlEnumerator(ISqlExecutorFactory execFac, string readStr, SqlConverterHandler<T> converter)
        {
            this.executor = execFac.Executor();
            this.reader = executor.ExecuteReader(readStr);
            this.converter = converter;
        }
        public T Current
        {
            get
            {
                if (currElem == null)
                {
                    throw new InvalidOperationException("Enumerator inm invalid state. Move it first.");
                }
                return currElem;
            }
        }

        public void Dispose()
        {
            executor.Dispose();
            reader.Dispose();
        }

        object IEnumerator.Current
        {
            get { return Current; }
        }

        public bool MoveNext()
        {
            currElem = null;
            return converter(reader, out currElem);
        }

        public void Reset()
        {
            throw new NotImplementedException();
        }
    }
}
