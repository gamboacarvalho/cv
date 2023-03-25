using Isel.Ave14.SqlMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SqlMapperTest
{
    public class Supplier : IEquatable<Supplier>
    {
        [SqlPk] public int SupplierID { set; get; }
        public IEnumerable<Product> Products { set; get; }
        public string CompanyName { set; get; }
        public string ContactName { set; get; }
        public string ContactTitle { set; get; }
        public string Address { set; get; }
        public string City { set; get; }
        public string Region { set; get; }
        public string PostalCode { set; get; }
        public string Country { set; get; }
        public string Phone { set; get; }
        public string Fax { set; get; }
        public string HomePage { set; get; }

        public Supplier() { }

        public Supplier(string address, string city, string companyName, string contactName, string contactTitle,
                           string country, string fax, string homePage, string phone, string postalCode,
                               string region, int supplierId)
        {
            Address = address;
            City = city;
            CompanyName = companyName;
            ContactName = contactName;
            ContactTitle = contactTitle;
            Country = country;
            Fax = fax;
            HomePage = homePage;
            Phone = phone;
            PostalCode = postalCode;
            Region = region;
            SupplierID = supplierId;
        }

        public bool Equals(Supplier other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return SupplierID == other.SupplierID && string.Equals(CompanyName, other.CompanyName)
                && string.Equals(ContactName, other.ContactName) && string.Equals(ContactTitle, other.ContactTitle)
                && string.Equals(Address, other.Address) && string.Equals(City, other.City)
                && string.Equals(Region, other.Region) && string.Equals(PostalCode, other.PostalCode)
                && string.Equals(Country, other.Country) && string.Equals(Phone, other.Phone)
                && string.Equals(Fax, other.Fax) && string.Equals(HomePage, other.HomePage);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Supplier)obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int hashCode = SupplierID;
                hashCode = (hashCode * 397) ^ (CompanyName != null ? CompanyName.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (ContactName != null ? ContactName.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (ContactTitle != null ? ContactTitle.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Address != null ? Address.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (City != null ? City.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Region != null ? Region.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (PostalCode != null ? PostalCode.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Country != null ? Country.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Phone != null ? Phone.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Fax != null ? Fax.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (HomePage != null ? HomePage.GetHashCode() : 0);
                return hashCode;
            }
        }
    }
}
