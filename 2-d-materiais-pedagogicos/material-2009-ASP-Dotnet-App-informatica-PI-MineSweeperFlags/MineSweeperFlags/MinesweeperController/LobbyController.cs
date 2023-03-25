using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Minesweeper;
using MinesweeperControllers.Utils;
using System.Web.Mvc;

namespace MinesweeperControllers
{
    public class LobbyController : GameBaseController
    {

        public LobbyController() { }

        //------------------------------
        // Pooling Handlers

        public ActionResult RefreshPlayers(string eMail)
        {
            Player p = Lobby.Current.GetPlayer(eMail);
            List<Player> rObj = null;
            if (p != null)
            {
                rObj = p.GetRefreshPlayers();
            }
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshFriends(string eMail)
        {
            Player f = Lobby.Current.GetPlayer(eMail);
            List<Player> rObj = null;
            if (f != null)
            {
                rObj = f.GetRefreshFriends();
            }
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshMessages(string eMail)
        {
            Player p = Lobby.Current.GetPlayer(eMail);
            List<Message> rObj = null;
            if (p != null)
            {
                rObj = p.GetRefreshMessages();
            }
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshInvites(string eMail)
        {
            Player p = Lobby.Current.GetPlayer(eMail);
            List<Invite> rObj = null;
            if (p != null)
            {
                rObj = p.GetRefreshInvites();
            }
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshProfile(string eMail)
        {
            Player rObj = Lobby.Current.GetPlayer(eMail);
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }

        public ActionResult RefreshGames(string eMail)
        {
            Player p = Lobby.Current.GetPlayer(eMail);
            List<Game> rObj = null;
            if (p != null)
            {
                rObj = p.GetRefreshGames();
            }
            return new ContentResult() { Content = Generic.GetJSon(rObj), ContentType = "text/x-json" };
        }


        //------------------------------
        // Event Handlers

        public ActionResult SendInvite(string gName, string eMail, string friend)
        {
            Lobby.Current.Invite(gName, eMail, friend);
            return new EmptyResult();
        }

        public ActionResult RefuseInvite(string gName, string eMail, string friend)
        {
            Lobby.Current.GetPlayer(friend).AddMessage(
                new Message("Player " + Lobby.Current.GetPlayer(eMail).Name + " refused "
                    + " invite for game " + gName + ".",eMail));
            return new EmptyResult();
        }
        public ActionResult AddFriend(string eMail, string friend)
        {
            Lobby.Current.GetPlayer(eMail).AddFriend(friend);
            return new EmptyResult();
        }

        public ActionResult RemoveFriend(string eMail, string friend)
        {
            Lobby.Current.GetPlayer(eMail).RemoveFriend(friend);
            return new EmptyResult();
        }

        public ActionResult SendMessage(string eMail, string msg)
        {
            Lobby.Current.UpdateRefreshMessages(new Message(msg, eMail));
            return new EmptyResult();
        }

        public ActionResult SendPrivateMessage(string eMail, string eMailTo, string msg)
        {
            Lobby.Current.GetPlayer(eMailTo).AddMessage((new Message(msg, eMail)));
            return new EmptyResult();
        }




    }
}
