using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MapperTest.Model
{
    public class Student
    {
        public int nr;
        public string name;

        public int Nr {
            get{
                return nr;
            }
            set {
                nr = value;
            }
        }

        public string Name
        {
            get
            {
                return name;
            }
            set
            {
                name = value;
            }
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }
            Student other = obj as Student;
            if (other == null) return false;
            return nr == other.nr && name.Equals(other.name);
        }
    }

}
