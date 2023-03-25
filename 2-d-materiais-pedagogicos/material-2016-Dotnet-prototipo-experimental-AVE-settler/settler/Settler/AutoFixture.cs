using Settler.Fixtures;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler
{
    public class AutoFixture : IAutoFixture
    {
        public static Fixture<T> For<T>()
        {
            Type t = typeof(T);
            Fixture<T> fix;
            if (typeof(T).GetConstructor(Type.EmptyTypes) == null)
                fix = new EntityFixtureByCtorArgs<T>();
            else
                fix = new EntityFixtureByFields<T>();
            fixts[t] = fix;
            return fix;
        }

        private readonly static IDictionary<Type, IFixture> fixts = new Dictionary<Type, IFixture>();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return !t.IsPrimitive && t != typeof(string);
        }

        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            if (klass.IsArray) klass = klass.GetElementType();
            return !klass.IsPrimitive && klass != typeof(string);
        }

        public IFixture For(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            IFixture fix;
            if (fixts.TryGetValue(t, out fix)) return fix;
            return (IFixture) typeof(AutoFixture)
                .GetMethod("For", Type.EmptyTypes)
                .MakeGenericMethod(t)
                .Invoke(null, new object[0]);
        }
    }
}
