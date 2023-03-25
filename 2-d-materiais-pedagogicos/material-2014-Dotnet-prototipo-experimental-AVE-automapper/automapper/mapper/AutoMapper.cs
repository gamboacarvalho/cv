using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mapper
{
    public class AutoMapper{
        public static AutoMapperBuilder<KSrc, KDest> Build<KSrc, KDest>()
        {
            return new AutoMapperBuilder<KSrc, KDest>();
        }

    }
}
