using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{
    class ShortAutoFixture : IAutoFixture
    {
        private readonly IFixture fix = new ShortFixture();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return t == typeof(short);
        }


        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            return klass == typeof(short);
        }

        public IFixture For(Type t)
        {
            return fix;
        }

        class ShortFixture : Fixture<short>
        {
            public override short New()
            {
                return (short)(rand.Next(short.MaxValue) + 1);
            }
        }
    }
}
