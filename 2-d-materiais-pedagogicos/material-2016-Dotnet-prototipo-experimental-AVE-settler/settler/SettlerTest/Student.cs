using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Settler.Test
{
    class Student
    {
        public Student(int nr, string name, School school)
        {
            this.Nr = nr;
            this.Name = name;
            this.School = school;
        }
        public int Nr { get; set; }
        public string Name { get; set; }
        public School School { get; set; }
        public override string ToString()
        {
            return String.Format("({0}) {1} {2}\n", School, Nr, Name);
        }
    }
}
