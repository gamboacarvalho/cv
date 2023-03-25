
namespace RepositoryInterfaces.DataObjects
{
    /// <summary>
    /// Summary description for User
    /// </summary>
    public interface IUser
    {
        string UserName { get; }

        int Password { get; }

        string Email { get; set; }
        string Name { get; set; }
        string[] Roles { get; }

        byte[] ImageData { get; set; }
        string ImageMimeType { get; set; }
    }
}