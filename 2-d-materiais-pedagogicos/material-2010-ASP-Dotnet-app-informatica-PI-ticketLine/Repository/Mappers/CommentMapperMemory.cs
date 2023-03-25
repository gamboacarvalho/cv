using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace Repository.Mappers
{
    public class CommentMapperMemory:ICommentMapper
    {
        #region Implementation of ICommentMapper

        private readonly IList<IComment> _comments = new List<IComment>();

        public int Size
        {
            get
            {
                lock (_comments)
                {
                    return _comments.Count;
                }
            }
        }

        public IEnumerable<IComment> GetByShow(int session)
        {
            lock (_comments)
            {
                return _comments.Where(s => s.Show == session);
            }
        }

        public void Add(IComment item)
        {
            lock (_comments)
            {
                _comments.Add(item);
            }
        }

        public IEnumerable<IComment> GetAll()
        {
            lock (_comments)
            {
                return _comments;
            }
        }

        #endregion
    }
}
