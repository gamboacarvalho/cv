using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jsonzai.Reflect
{
    /// <summary>
    /// Implementations of Reflector define different approaches for extracting 
    /// the state from a source object.
    /// </summary>
    public interface Reflector
    {
        Dictionary<string, object> ValuesFrom(object src);
    }
}
