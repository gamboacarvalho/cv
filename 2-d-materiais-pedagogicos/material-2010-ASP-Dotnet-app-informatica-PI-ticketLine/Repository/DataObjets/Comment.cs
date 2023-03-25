using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RepositoryInterfaces.DataObjects;

namespace Repository.DataObjets
{
    public class Comment:IComment
    {
        #region Implementation of IComment

        public int Show { get; private set; }

        public string UserComment { get; private set; }

        public string Title { get; private set; }

        public DateTime CommentDate { get; private set; }

        public string UserName { get; private set; }

        #endregion

        public Comment(int session, string userName, string title, string userComment, DateTime dateTime)
        {
            Show = session;
            UserName = userName;
            Title = title;
            UserComment = userComment;
            CommentDate = dateTime;
        }
    }
}
