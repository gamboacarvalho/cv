using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Settler
{
    public abstract class Fixture<T> : IFixture<T>
    {

        protected static readonly Random rand = new Random();
        protected readonly Type klass;

        public Fixture() {
            this.klass = typeof(T);
        }

        public abstract T New();

        public virtual Fixture<T> Member(string name, params object [] pool)
        {
            throw new NotImplementedException();
        }

        public virtual Fixture<T> Member<R>(string name, Func<R> supplier)
        {
            throw new NotImplementedException();
        }

        public T[] Fill(int size)
        {
            T[] res = new T[size];
            for (int i = 0; i < size; i++)
            {
                res[i] = New();
            }
            return res;
        }


        object IFixture.Fill(int size)
        {
            return Fill(size);
        }
        object IFixture.New()
        {
            return New();
        }

        public Fixture<T> Ignore(string prop)
        {
            throw new NotImplementedException();
        }

        public Fixture<T> Ignore<R>() where R : Attribute
        {
            return this;
        }
    }
}
