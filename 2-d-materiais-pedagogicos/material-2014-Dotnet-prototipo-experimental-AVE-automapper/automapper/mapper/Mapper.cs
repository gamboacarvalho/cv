using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mapper
{
    public class Mapper<TSrc, TDest> : IMapper<TSrc, TDest>
    {
        private readonly Action<TSrc, TDest>[] membersMappers;

        public Mapper(params Action<TSrc, TDest> [] membersMappers) {
            this.membersMappers = membersMappers;
        }
        public TDest Map(TSrc src)
        {
            TDest dest = Activator.CreateInstance<TDest>();
            return membersMappers.Aggregate(dest, (acc, mapper) => { mapper(src, acc); return acc; });
        }
        public TColDest Map<TColDest>(IEnumerable<TSrc> src) where TColDest:ICollection<TDest>
        {
            TColDest dest = Activator.CreateInstance<TColDest>();
            var tmp = src.Select(item => Map(item));
            foreach (var item in tmp) dest.Add(item);
            return dest;
        }
    }
}
