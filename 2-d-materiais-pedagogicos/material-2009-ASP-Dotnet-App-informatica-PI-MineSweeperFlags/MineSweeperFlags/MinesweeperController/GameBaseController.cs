using System;
using System.Collections.Generic;
using System.Web;
using System.IO;
using System.Web.Mvc;
using MinesweeperControllers.Proxy;
using MinesweeperControllers.Utils;
using Minesweeper;

namespace MinesweeperControllers
{
    public class GameBaseController: Controller
    {
        public GameBaseController() { }

        protected Game CurrentGame
        {
            get { return Minesweeper.Lobby.Current[Request["gName"]]; }
        }

    }
}
