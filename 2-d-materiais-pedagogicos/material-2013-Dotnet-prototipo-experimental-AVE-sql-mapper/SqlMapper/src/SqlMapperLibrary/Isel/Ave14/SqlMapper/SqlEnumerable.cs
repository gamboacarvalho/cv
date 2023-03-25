using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{

    public interface ISqlEnumerable<T> : IEnumerable<T>, ISqlEnumerable
    {
        ISqlEnumerable<T> Where(String clause);
        ISqlEnumerable<T> Where(Expression<Func<T, bool>> clause);
    }

    public interface ISqlEnumerable : IEnumerable
    {
        ISqlEnumerable Where(String clause);
        ISqlEnumerable Where(Expression<Func<object, bool>> clause);
    }

    public class SqlEnumerable<T> : ISqlEnumerable<T> where T : class
    {
        private readonly ISqlExecutorFactory execFac;
        private readonly string readStr;
        private readonly SqlConverterHandler<T> converter;

        public SqlEnumerable(ISqlExecutorFactory execFac, string p, SqlConverterHandler<T> converter)
        {
            this.execFac = execFac;
            this.readStr = p;
            this.converter = converter;
        }
        public IEnumerator<T> GetEnumerator()
        {
            return new SqlEnumerator<T>(execFac, readStr, converter);
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }

        public ISqlEnumerable<T> Where(String clause)
        {
            string res = readStr.ToUpper().Contains("WHERE") ? 
                readStr + " AND " + clause : 
                readStr + " WHERE " + clause;
            return new SqlEnumerable<T>(
                    execFac,
                    res,
                    converter
                );
        }

        ISqlEnumerable ISqlEnumerable.Where(string clause)
        {
            return Where(clause);
        }


        public ISqlEnumerable<T> Where(Expression<Func<T, bool>> clause)
        {
            throw new NotImplementedException();
        }


        public ISqlEnumerable Where(Expression<Func<object, bool>> clause)
        {
            

            Expression<Func<T, bool>> e = (arg) => clause.Compile()(arg);

            return Where(e);
        }
    }
}
