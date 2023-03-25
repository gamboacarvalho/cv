using System.Collections.Generic;
using System.Linq;

namespace DAOBuilder
{


    internal struct Method
    {
        private readonly IEnumerable<string> _paramNames;

        public IEnumerable<string> ParamNames
        {
            get { return _paramNames; }
        }

        public readonly AccessorCall DoAccess;


        public Method(AccessorCall ac, IEnumerable<string> pr)
        {
            DoAccess = ac;
            _paramNames = pr;
        }

        public override int GetHashCode()
        {
            return DoAccess.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
                return false;
            if (obj.GetType() != typeof (Method))
                return false;
            Method mi2 = (Method) obj;

            return mi2.DoAccess.Equals(DoAccess) && !mi2.ParamNames.Except(ParamNames).Any();


        }
    }
}