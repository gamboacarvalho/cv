namespace DAOModel
{
    public class Supplier
    {
        public int SupplierID { get; set; }
        public string CompanyName;
        public string ContactName;
        public string ContactTitle;
        public string Address { get; set; }
        public string City { get; set; }
        public string Region;
        public string PostalCode;
        public string Country;
        public string Phone;
        public string Fax { get; set; }
        public string HomePage { get; set; }
    }
}
