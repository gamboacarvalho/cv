using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Test
{
    class Classroom
    {
        public Student[] students;
        public string id;

        public override string ToString()
        {
            String lst = string.Join(",", (IEnumerable<Student>) students);
            return String.Format("Id: {0}, Students: {1}", id, lst);
        }
    }
}
