using System;
using System.Linq;

namespace DAOBuilder
{
    internal struct MethodKey
    {

        public override int GetHashCode()
        {
                int hashCode = Name.GetHashCode();
                hashCode = (hashCode*397) ^ ReturnType.GetHashCode();
                return hashCode;
        }

        public string Name { get; set; }
        public Type[] ParamTypes { get; set; }
        public Type ReturnType { get; set; }


        public override string ToString()
        {
                 return string.Format("{0} {1}({2})",
                        ReturnType, Name, ParamTypes.Length < 1 ? "" : (ParamTypes.First() + (ParamTypes.Length==1?"":
                        ", "+string.Join(", ", ParamTypes.Skip(1).Select(t => t.ToString())))));
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
                return false;
            if (obj.GetType() != typeof (MethodKey))
                return false;
            MethodKey m = (MethodKey) obj;
            return Name.Equals(m.Name) && ReturnType == m.ReturnType && CompareAssignable(m.ParamTypes);
        }

        private bool CompareAssignable(Type[] list)
        {
           if(list.Length!=ParamTypes.Length)
               return false;;
            
            for (int i = 0; i < list.Length; ++i)
            {
                if (!list[i].IsAssignableFrom(ParamTypes[i]))
                    return false;
                    
            }

            return true;
        }
    }
}
