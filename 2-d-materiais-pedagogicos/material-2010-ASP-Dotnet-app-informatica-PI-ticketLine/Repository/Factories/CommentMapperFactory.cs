using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Repository.Mappers;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository.Factories
{
    public class CommentMapperFactory:ICommentMapperFactory
    {
        #region Implementation of ICommentMapperFactory

        private readonly ICommentMapper _mapper = new CommentMapperMemory();

        public ICommentMapper GetMapper()
        {
            return _mapper;
        }

        #endregion
    }
}
