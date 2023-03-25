using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{
    public class PoolFixture : Fixture<object>
    {
        private object[] pool;

        public PoolFixture(object[] pool)
        {
            this.pool = pool;
        }

        public override object New()
        {
            return pool[rand.Next(pool.Length)];
        }
    }
}
