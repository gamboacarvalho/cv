using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Fixtures
{
    class SupplierFixture<T> : Fixture<T>
    {
        private Func<T> supplier;

        public SupplierFixture(Func<T> supplier)
        {
            this.supplier = supplier;
        }

        public override T New()
        {
            return supplier();
        }
    }
}
