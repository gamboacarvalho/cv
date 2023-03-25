using System.Collections.Generic;
using DAOBuilder.Attributes;

namespace DAOModel.Interfaces
{
    public interface IProductDao
    {
        [SqlCmd("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products")]
        IEnumerable<Product> GetAll();

        [SqlCmd("SELECT * FROM Products WHERE ProductID = @id")]
        Product GetById(int id);

        [SqlCmd("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE UnitPrice < @price ")]
        IEnumerable<Product> GetWithMaxPrice(decimal price);

        [SqlCmd("UPDATE Products SET ProductName = @name, UnitPrice = @price, UnitsInStock = @stock WHERE ProductID = @id")]
        void Update(string name, decimal price, short stock, int id);

        [SqlCmd("DELETE FROM Products WHERE ProductID = @id")]
        void Delete(int id);

        [SqlCmd("INSERT INTO Products (ProductName, UnitPrice, UnitsInStock) OUTPUT INSERTED.ProductID VALUES (@name, @price, @stock)")]
        int Insert(string name, decimal price, short stock); //used to return Product

        int DoNothing(string name);
    }
}
