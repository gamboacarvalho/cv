using System.Collections.Generic;
using DAOBuilder.Attributes;

namespace DAOModel.Interfaces
{
    public interface ISupplierDao
    {
        [SqlCmd("SELECT SupplierID, CompanyName, Address, Country  FROM Suppliers")]
        IEnumerable<Supplier> GetAll();

        [SqlCmd("SELECT *  FROM Suppliers WHERE SupplierID = @id")]
        Supplier GetById(int id);

        [SqlCmd("SELECT *  FROM Suppliers WHERE Country LIKE @country ")]
        IEnumerable<Supplier> GetWithMaxPrice(string country);

        [SqlCmd("SELECT distinct Country FROM Suppliers ")]
        IEnumerable<string> GetAllCountries();

        [SqlCmd("UPDATE Suppliers SET ContactName = @name, ContactTitle = @title WHERE SupplierID = @id")]
        void Update(string name, string title, int id);

        [SqlCmd("DELETE FROM Suppliers WHERE SupplierID = @id")]
        void Delete(int id);

        [SqlCmd("INSERT INTO Suppliers (CompanyName, ContactName, ContactTitle, Address, City, Country) OUTPUT INSERTED.SupplierID VALUES (@companyName, @contactName, @title,@address,@city,@country)")]
        int Insert(string companyName, string contactName, string title, string address, string city, string country);

        void DoNothing(string str, int i);
    }
}
