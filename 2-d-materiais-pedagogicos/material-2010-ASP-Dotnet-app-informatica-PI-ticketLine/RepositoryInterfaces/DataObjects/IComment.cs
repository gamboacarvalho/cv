using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RepositoryInterfaces.DataObjects
{
    public interface IComment
    {
        int Show { get; }
        string UserComment { get; }
        string Title { get; }
        DateTime CommentDate { get; }
        string UserName { get; }
    }
}
