using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{

    abstract class AbstractEntityFixture<T> : Fixture<T>
    {
        protected static readonly List<IAutoFixture> fixs = new List<IAutoFixture>(){
            new IntAutoFixture(),
            new ShortAutoFixture(),
            new LongAutoFixture(),
            new DoubleAutoFixture(),
            new StringAutoFixture(),
            new AutoFixture()
        };
        protected static object Instantiate(Type t, IFixture fix)
        {
            return t.IsArray ?
                fix.Fill(rand.Next(10) + 1) :
                fix.New();
        }

    }

    class EntityFixtureByCtorArgs<T> : AbstractEntityFixture<T>
    {
        private readonly Dictionary<ParameterInfo, IFixture> argsFixtures = new Dictionary<ParameterInfo, IFixture>();
        private readonly ConstructorInfo ctor;

        public EntityFixtureByCtorArgs()
        {
            ctor = klass.GetConstructors()[0];
            if (ctor.GetParameters().Length == 0) throw new InvalidOperationException("Wrong Wntity Fixture for parameterless constructor!");
            foreach (var arg in ctor.GetParameters())
            {
                IFixture fix = fixs
                    .Where(fac => fac.IsForMemberType(arg.ParameterType))
                    .Select(fac => fac.For(arg.ParameterType))
                    .First();
                if (fix == null) throw new InvalidOperationException("No Fixture available for " + arg);
                argsFixtures[arg] = fix;
            }

        }

        public override Fixture<T> Member(string name, params object[] pool)
        {
            ParameterInfo p = ctor.GetParameters().Where(arg => arg.Name.Equals(name)).FirstOrDefault();
            if (p == null) throw new ArgumentException("There is no constructor argument with name = " + name);
            argsFixtures[p] = new PoolFixture(pool);
            return this;
        }

        public override Fixture<T> Member<R>(string name, Func<R> supplier)
        {
            ParameterInfo p = ctor.GetParameters().Where(arg => arg.Name.Equals(name)).FirstOrDefault();
            if (p == null) throw new ArgumentException("There is no constructor argument with name = " + name);
            argsFixtures[p] = new SupplierFixture<R>(supplier);
            return this;
        }


        public override T New()
        {
            object [] args = argsFixtures
                .Select(pair => Instantiate(pair.Key.ParameterType, pair.Value))
                .ToArray();
            return (T)ctor.Invoke(args);
        }
    }

    class EntityFixtureByFields<T> : AbstractEntityFixture<T> {
        private readonly Dictionary<FieldInfo, IFixture> fields = new Dictionary<FieldInfo, IFixture>();

        public EntityFixtureByFields()
        {
            ConstructorInfo ctor = klass.GetConstructor(Type.EmptyTypes);
            if (ctor == null) throw new InvalidOperationException("Missing parameterless constructor!!!!");
            FieldInfo[] fs = klass.GetFields(BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public);
            foreach (var f in fs)
            {
                IFixture fix = fixs
                    .Where(fac => fac.IsForMember(f))
                    .Select(fac => fac.For(f.FieldType))
                    .First();
                if (fix == null) throw new InvalidOperationException("No Fixture available for " + f);
                fields[f] = fix;
            }
        }

        public override Fixture<T> Member(string name, params object[] pool)
        {
            FieldInfo f = klass.GetField(
                name,
                BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public);
            fields[f] = new PoolFixture(pool);
            return this;
        }

        public override T New()
        {
            object target = Activator.CreateInstance(klass);
            foreach (var pair in fields)
            {
                object val = Instantiate(pair.Key.FieldType, pair.Value);
                pair.Key.SetValue(target, val);
            }
            return (T)target;
        }

    }
}
