using Webao.Test.Dto;

namespace Webao.Test
{
    public interface IWebaoArtist
    {
        DtoArtist GetInfo(string name);
        DtoSearch Search(string name);
    }
}
