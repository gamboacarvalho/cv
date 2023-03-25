using System.Collections.Generic;
using System.Web.Mvc;
using MinesweeperControllers.Proxy;
using MinesweeperControllers.Utils;
using Minesweeper;

namespace MinesweeperControllers
{
    public class GameAsynchronousController : GameBaseController
    {
        public GameAsynchronousController() { }


        public ActionResult RevealBoard(int playerId)
        {
            CurrentGame.RevealBoard(playerId - 1);
            return RefreshCell(playerId);
        }

        public ActionResult RefreshPlayerBoard(int playerId)
        {
            List<GamePlayer> rObj = CurrentGame.GetRefreshPlayer(playerId - 1);
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }
        
        public ActionResult RefreshCell(int playerId)
        {
            List<Cell> rObj = CurrentGame.GetRefreshCell(playerId - 1);

            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshGameInfo(string gName)
        {
            JSONGame game = new JSONGame(gName);
            game.minesLeft = CurrentGame.MinesLeft;
            game.activePlayer = CurrentGame.CurrentPlayer;
            game.gStatus = CurrentGame.Status;
            game.playersCount = CurrentGame.PlayersCount;

            return new ContentResult() { Content = game.ToJSon(), ContentType = "text/x-json" };
        }

        public ActionResult ListActiveGames( string eMail )
        {
            return new ContentResult() { Content = Utils.JSon.Serialize<IEnumerable<string>>(Minesweeper.Lobby.Current.GetActiveGames(eMail)), ContentType = "text/x-json" };
        }

        public ActionResult Play(int playerId, int posX, int posY)
        {
            playerId -= 1;
            if (CurrentGame.Status != GameStatus.GAME_OVER)
            {
                CurrentGame.Play(playerId, posX, posY);
            }
            return new EmptyResult();
        }

        public ActionResult RemovePlayer(int playerID)
        {
            CurrentGame.RemovePlayer(playerID - 1);

            return new EmptyResult();
        }        

        public ActionResult StartGame(string gName, int playerId)
        {
            JSONGame game = new JSONGame(gName);

            CurrentGame.Start();
            game.activePlayer = CurrentGame.CurrentPlayer;
            game.callingPlayer = playerId;
            game.minesLeft = CurrentGame.MinesLeft;
            game.gStatus = CurrentGame.Status;
            game.playersCount = CurrentGame.PlayersCount;

            return new ContentResult() { Content = game.ToJSon(), ContentType = "text/x-json" };
        }

        public ActionResult GameBoard()
        {
            return new ViewResult();            
        }

        public ActionResult RefreshPlayers(int playerId)
        {
            List<Cell> rObj = CurrentGame.GetRefreshCell(playerId - 1);

            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

    }
}
