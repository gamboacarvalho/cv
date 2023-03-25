using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Settler
{
    public interface IFixture
    {
        Object New();

        Object Fill(int size);

    }

    public interface IFixture<T> : IFixture
    {
        new T New();

        new T[] Fill(int size);

        Fixture<T> Member(string name, params object[] pool);

        Fixture<T> Member<R>(string name, Func<R> supplier);

        Fixture<T> Ignore(string prop);
    }

}
