using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Settler.Fixtures
{

    class StringAutoFixture : IAutoFixture
    {
        private readonly IFixture fix = new StringFixture();

        public bool IsForMemberType(Type t)
        {
            if (t.IsArray) t = t.GetElementType();
            return t == typeof(string);
        }


        public bool IsForMember(MemberInfo t)
        {
            FieldInfo prop = t as FieldInfo;
            if (prop == null) throw new InvalidOperationException("No support for " + t);
            Type klass = prop.FieldType;
            return klass == typeof(string);
        }

        public IFixture For(Type t)
        {
            return fix;
        }

        class StringFixture : Fixture<String>
        {

            const string letters = "abcdefgilnopqrstuv ";

            public override string New()
            {
                char[] str = new char[rand.Next(20) + 1];
                for (int i = 0; i < str.Length; i++)
                {
                    str[i] = letters[rand.Next(letters.Length)];
                }
                return new String(str);
            }
        }
    }
}
