using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Instr
{
    public interface Serializer
    {
        string ToJson(object src);
    }
}
