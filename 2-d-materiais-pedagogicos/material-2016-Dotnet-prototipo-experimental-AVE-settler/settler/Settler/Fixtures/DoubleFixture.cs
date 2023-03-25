using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{

    class DoubleAutoFixture : IAutoFixture
    {
        private readonly IFixture fix = new DoubleFixture();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return t == typeof(double);
        }

        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            return klass == typeof(double);
        }

        public IFixture For(Type t)
        {
            return fix;
        }
        class DoubleFixture : Fixture<Double>
        {
            public override double New()
            {
                return rand.NextDouble() * double.MaxValue + 1;
            }
        }
    }
}
