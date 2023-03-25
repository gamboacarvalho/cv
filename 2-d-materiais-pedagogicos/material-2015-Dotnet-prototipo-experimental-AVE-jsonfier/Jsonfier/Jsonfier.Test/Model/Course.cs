using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Test.Model
{
    public class Course
    {
        public Student[] stds;
        public string name;


        public Student[] Students
        {
            get
            {
                return stds;
            }
            set
            {
                stds = value;
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


        // override object.Equals
        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }
            Course other = obj as Course;
            if (other == null) return false;
            return name.Equals(other.name) && Enumerable.SequenceEqual(this.stds, other.stds);
        }
    }

}
