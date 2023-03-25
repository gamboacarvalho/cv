using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    public class Builder
    {

        private readonly Dictionary<Type, IDataMapper> cache;
        private readonly ISqlExecutorFactory fac;

        public Builder(ISqlExecutorFactory fac)
        {
            this.cache = new Dictionary<Type, IDataMapper>();
            this.fac = fac;
        }


        public IDataMapper<T> Build<T>()
            where T : class
        {
            IDataMapper mapper;
            if (!cache.TryGetValue(typeof(T), out mapper))
            {
                mapper = new AutoDataMapper<T>(this, fac);
                cache[typeof(T)] = mapper;
            }

            return (IDataMapper<T>)mapper;
        }
    }
}
