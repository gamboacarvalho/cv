using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler
{
    public interface IAutoFixture
    {
        bool IsForMember(MemberInfo t);

        bool IsForMemberType(Type t);

        IFixture For(Type t);
    }
}
