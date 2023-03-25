using System.Collections.Generic;
using DAOBuilder.Attributes;

namespace DAOModel.Interfaces
{
    public interface ICategoryDao
    {
        [SqlCmd("SELECT CategoryID, CategoryName, Description FROM Categories")]
        IEnumerable<Category> GetAll();

        [SqlCmd("SELECT CategoryID, CategoryName, Description FROM Categories WHERE CategoryID = @id")]
        Category GetById(int id);

        [SqlCmd("UPDATE Categories SET CategoryName = @name, Description = @desc WHERE Category = @id")]
        void Update(string name, string desc, int id);

        [SqlCmd("DELETE FROM Categories WHERE CategoryID = @id")]
        void Delete(int id);

        [SqlCmd("INSERT into Categories(CategoryName,Description) OUTPUT INSERTED.CategoryID values(@name,@desc)")]
        int Insert(string name, string desc);

        [SqlCmd("SELECT CategoryName FROM Categories")]
        IEnumerable<string> GetAllNames();
    }
}
