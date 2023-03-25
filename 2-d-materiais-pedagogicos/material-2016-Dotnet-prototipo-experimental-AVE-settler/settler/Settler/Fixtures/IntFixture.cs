using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{
    class IntAutoFixture : IAutoFixture
    {
        private readonly IFixture fix = new IntFixture();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return t == typeof(int);
        }


        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            return klass == typeof(int);
        }

        public IFixture For(Type t)
        {
            return fix;
        }

        class IntFixture : Fixture<int>
        {
            public override int New()
            {
                return rand.Next(int.MaxValue) + 1;
            }
        }
    }
}
