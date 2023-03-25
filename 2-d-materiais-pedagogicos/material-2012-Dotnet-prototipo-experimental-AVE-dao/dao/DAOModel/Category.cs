using System.Collections.Generic;
using DAOBuilder;

namespace DAOModel
{
    public class Category:ISettable
    {
        private readonly Dictionary<string, object> _properties = new Dictionary<string, object>();

        public int CategoryID
        {
            get { return (int)_properties["CategoryID"]; }
            set { _properties["CategoryID"] = value; }
        }

        public string CategoryName
        {
            get { return (string)_properties["CategoryName"]; }
            set { _properties["CategoryName"] = value; }
        }

        public string Description
        {
            get { return (string)_properties["Description"]; }
            set { _properties["Description"] = value; }
        }

        public byte[] Picture
        {
            get { return (byte[])_properties["Picture"]; }
            set { _properties["Picture"] = value; }
        }

        public void Set(string name, object val)
        {
            _properties[name] = val;
        }
    }
}
