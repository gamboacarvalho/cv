using System;

namespace Webao.Attributes
{
[AttributeUsage(AttributeTargets.Method, AllowMultiple = false)]
public class MappingAttribute : Attribute
{
    public readonly string path;

    public MappingAttribute(Type dto, string path)
    {
        this.path = path;
    }
    public MappingAttribute(Type dto)
    {
            
    }
    public string With { set { } }

        public MappingAttribute(string path)
        {
            this.path = path;
        }
    }
}
