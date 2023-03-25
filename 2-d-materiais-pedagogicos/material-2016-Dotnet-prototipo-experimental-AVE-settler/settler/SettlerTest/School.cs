using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Settler.Test
{
    class School
    {
        public string Name { get; set;  }
        public string Location{ get; set; }

        public override string ToString()
        {
            return String.Format("Name: {0}, Location: {1}", Name, Location);
        }
    }
}
