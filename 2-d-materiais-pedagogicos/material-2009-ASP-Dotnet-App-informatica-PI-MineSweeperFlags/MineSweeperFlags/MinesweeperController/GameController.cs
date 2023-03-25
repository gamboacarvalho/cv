using System;
using System.Web.Mvc;
using Minesweeper;
using MinesweeperControllers.ExtensionMethods;

namespace MinesweeperControllers
{
    public class GameController : GameBaseController
    {
        public ActionResult Create(string gName, string pName, string eMail, GameType type)
        {
            Minesweeper.Lobby.Current.CreateGame(gName, pName, eMail, type);
            return Show(gName, eMail);
        }

        public ActionResult Join(string gName, string pName, string eMail)
        {
            if (CurrentGame != null && CurrentGame.GetPlayer(eMail) == null)
            {
                CurrentGame.AddPlayer(pName, eMail);
                return Show(gName, eMail);
            }

            return new EmptyResult();
        }

        public ActionResult ReserveGameName(string gName, string eMail)
        {
            string retContent = null;
            if (!Minesweeper.Lobby.Current.ContainsGame(gName))
            {
                Minesweeper.Lobby.Current.ReserveGame(gName);
                retContent = gName;
            }
            else
            {
                Minesweeper.Lobby.Current.GetPlayer(eMail).AddMessage(
                    new Message("Game " + gName + " is currently in use!", "MS2500")
                );
            }
            return new ContentResult() { Content = retContent, ContentType = "text" };
        }

        ActionResult Show(string gName, string eMail)
        {
            if (gName == null) throw new ArgumentNullException("gName");
            if (eMail == null) throw new ArgumentNullException("eMail");

            if (Minesweeper.Lobby.Current[gName] == null) throw new ArgumentOutOfRangeException("gName");
            if (Minesweeper.Lobby.Current[gName].Status > GameStatus.WAITING_FOR_START) throw new ApplicationException("Invalid Game Status.");

            Minesweeper.GamePlayer gPlayer = Minesweeper.Lobby.Current[gName].GetPlayer(eMail);
            if (gPlayer == null) throw new ApplicationException("Invalid Game Player.");


            ViewData.Add("gName", gName);
            ViewData.Add("pName", gPlayer.Name);
            ViewData.Add("pEMail", gPlayer.EMail);
            ViewData.Add("pId", gPlayer.Id);
            ViewData.Add("isOwner", Minesweeper.Lobby.Current[gName].IsOwner(eMail));

            ViewData.Add("gKey", Utils.GameKey.GetKey());
            return View("Show");
        }
        public ActionResult Lobby(string eMail)
        {
            return View(Minesweeper.Lobby.Current.LoadPlayer(eMail));
        }

        public ActionResult Main(string eMail)
        {
            Player p = Minesweeper.Lobby.Current.LoadPlayer(eMail);
            p.UpdateRefreshFriends();
            return View(p);
        }


        [AcceptVerbs(HttpVerbs.Get)]
        public ActionResult Start(string message)
        {
            ViewData["message"] = message;
            return View();
        }

        [AcceptVerbs(HttpVerbs.Post)]
        public ActionResult Start(string email, string pwd)
        {
            if (email != null && email.IsEMail())
            {
                Player p = null;
                if ((p = Minesweeper.Lobby.Current.GetPlayer(email)) != null)
                {
                    //if (p.DoLogin(pwd) == Minesweeper.Login.Ok)
                    //{
                        return new RedirectResult(string.Format("/Game/Main?eMail={0}", Server.UrlEncode(email)));
                    //}
                    //else
                    //{
                    //    ViewData["message"] = "Invalid username or password.";
                    //}
                }
                else
                {
                    ViewData["message"] = "E-mail " + email + " is not registered!";
                }

                return View();
            }

            ViewData["message"] = "Please insert a valid e-mail!";
            return View();
        }
    }
}
