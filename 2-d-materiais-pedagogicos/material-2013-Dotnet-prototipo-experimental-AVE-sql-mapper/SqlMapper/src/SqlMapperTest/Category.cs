using Isel.Ave14.SqlMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SqlMapperTest
{
    public class Category : IEquatable<Category>
    {
        [SqlPk] public int CategoryID { set; get; }
        public string CategoryName { set; get; }
        public string Description { set; get; }

        //e o campo Picture image??

        public Category() { }

        public Category(int id, string name, string desc)
        {
            this.CategoryID = id;
            this.CategoryName = name;
            this.Description = desc;
        }

        public bool Equals(Category other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return CategoryID == other.CategoryID
                                          && String.Equals(CategoryName, other.CategoryName)
                                          && String.Equals(Description, other.Description);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Category)obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int hashCode = CategoryID;
                hashCode = (hashCode * 397) ^ (CategoryName != null ? CategoryName.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Description != null ? Description.GetHashCode() : 0);
                return hashCode;
            }
        }

    }

}
