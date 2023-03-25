using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{
    class LongAutoFixture : IAutoFixture
    {
        private readonly IFixture fix = new LongFixture();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return t == typeof(long);
        }


        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            return klass == typeof(long);
        }

        public IFixture For(Type t)
        {
            return fix;
        }
        class LongFixture : Fixture<long>
        {
            public override long New()
            {
                byte[] buffer = new byte[sizeof(Int64)];
                rand.NextBytes(buffer);
                return BitConverter.ToInt64(buffer, 0);
            }
        }
    }
}
