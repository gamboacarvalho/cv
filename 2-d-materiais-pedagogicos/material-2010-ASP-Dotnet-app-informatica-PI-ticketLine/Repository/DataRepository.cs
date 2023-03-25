using System;
using Repository.DataObjets;
using Repository.Factories;
using RepositoryInterfaces.Factories;
using RepositoryInterfaces.Mappers;

namespace Repository
{
    /// <summary>
    /// Summary description for Repository
    /// </summary>
    public static class DataRepository
    {
        public static readonly IReserveMapperFactory ReserveMapperFactory = new ReserveMapperFactory();
        public static readonly IRoomMapperFactory RoomMapperFactory = new RoomMapperFactory();
        public static readonly ISessionMapperFactory SessionMapperFactory = new SessionMapperFactory();
        public static readonly IShowMapperFactory ShowMapperFactory = new ShowMapperFactory();
        public static readonly IUserMapperFactory UserMapperFactory = new UserMapperFactory();
        public static readonly ILoginMapperFactory LoginMapperFactory = new LoginMapperFactory();
        public static readonly ICommentMapperFactory CommentMapperFactory = new CommentMapperFactory();

        static DataRepository()
        {
            #region Init room

            int[] layout = new[]
                               {
                                   1, 0, 0, 0, 0, 0, 0, 0, 1,
                                   1, 1, 0, 0, 0, 0, 0, 1, 1,
                                   1, 1, 1, 1, 1, 1, 1, 1, 1,
                                   1, 1, 1, 1, 1, 1, 1, 1, 1,
                                   1, 1, 1, 1, 1, 1, 1, 1, 1,
                                   0, 1, 1, 1, 1, 1, 1, 1, 0
                               };
            Room room = new Room("Sala 1", layout.Clone() as int[], 6, 9);
            RoomMapperFactory.GetMapper().Add(room);

            layout = new[]
                               {
                                   1, 0, 0, 1, 1, 1, 0, 0, 1,
                                   1, 1, 0, 0, 0, 0, 0, 1, 1,
                                   1, 1, 1, 1, 0, 1, 1, 1, 1,
                                   0, 0, 0, 0, 0, 0, 0, 0, 0,
                                   1, 1, 1, 1, 0, 1, 1, 1, 1,
                                   0, 1, 1, 1, 0, 1, 1, 1, 0
                               };
            room = new Room("Sala 2", layout.Clone() as int[], 6, 9);
            RoomMapperFactory.GetMapper().Add(room);

            string[] roomNames = new[] { "Sala 1", "Sala 2" };

            #endregion

            #region Init shows

            const int showCount = 3;
            for (int i = 0; i < showCount; i++)
            {
                Show show = new Show("Concerto " + i, "Expectáculo para teste!");
                ShowMapperFactory.GetMapper().Add(show);
            }

            #endregion

            #region Init sessions

            const int sessionCount = 8;
            for (int i = 0; i < sessionCount; i++)
            {
                Session session = new Session(2010, 4, i + 1, 12, RoomMapperFactory.GetMapper().Get(roomNames[i % 2]),
                                              ShowMapperFactory.GetMapper().Get(i % showCount), 15.00);
                int id = SessionMapperFactory.GetMapper().Add(session);

                CommentMapperFactory.GetMapper().Add(new Comment(id, "Anónimo", "Comentário " + i, "Eh lecas! Comentário de teste!!!", DateTime.Now));
            }

            #endregion

            #region Init users

            IUserMapper userMapper = UserMapperFactory.GetMapper();

            #region Init admins

            userMapper.Add(new Admin("admin", "12345", "admin@domain.com", "Admin"));
            userMapper.Add(new Admin("admin2", "12345", "admin2@domain.com", "Admin 2"));

            #endregion
            #region Init sample clients

            userMapper.Add(new Client("A", "12345", "a1@domain.com", "António das Couves"));
            userMapper.Add(new Client("B", "12345", "b1@domain.com", "António dos Nabos"));

            #endregion

            #endregion

        }
    }
}