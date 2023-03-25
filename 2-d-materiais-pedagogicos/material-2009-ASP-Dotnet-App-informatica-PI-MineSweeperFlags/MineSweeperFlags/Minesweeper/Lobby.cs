using System;
using System.Collections.Generic;
using System.Linq;

namespace Minesweeper
{
    public class Lobby
    {
        const int COLS = 20;
        const int ROWS = 20;
        Dictionary<string, Game> games;
        Dictionary<string, Player> players;

        Lobby()
        {
            games = new Dictionary<string, Game>();
            players = new Dictionary<string, Player>(){
                    {"26657@p3.pi",new Player("Ricardo","26657@p3.pi",new Photo(){Name="r"})},
                    {"31923@p3.pi",new Player("Nuno","31923@p3.pi",new Photo(){Name="n"})},
                    {"32223@p3.pi",new Player("Nuno","32223@p3.pi",new Photo(){Name="p"})}
                };
        }

        static Lobby()
        {
            if (Current == null) Current = new Lobby();
        }

        public static Lobby Current;

        public Game this[string name]
        {
            get
            {
                if (games.ContainsKey(name)) return games[name];
                return null;
            }
        }


        //--------------------------
        // Players Refresh Structures Management

        private void UpdatePlayersOnNewPlayer(Player player)
        {
            foreach (Player p in players.Values)
                if (p != player && p.Online)
                    player.AddRefreshPlayers(p);
        }

        private void UpdateRefreshPlayers(Player player)
        {
            foreach (Player p in players.Values)
                if (p != player && p.Online)
                    p.AddRefreshPlayers(player);
        }

        private void UpdateGamesOnNewPlayer(Player player)
        {
            games.Values.Where(g => g != null && (g.Status == GameStatus.WAITING_FOR_PLAYERS ||
                g.Status == GameStatus.WAITING_FOR_START)).All(g => player.AddRefreshGames(g));
        }

        private void UpdateRefreshGames(Game game)
        {
            players.Values.Where(p => p.Online).All(p => p.AddRefreshGames(game));
        }

        public void UpdateRefreshMessages(Message msg)
        {
            players.Values.All(p => p.AddMessage(msg));
        }

        //--------------------------
        // Games Management

        public bool CreateGame(string gName, string pName, string pEMail, GameType type)
        {
            if (gName == null) throw new ArgumentNullException("gName");
            if (pName == null) throw new ArgumentNullException("pName");
            if (pEMail == null) throw new ArgumentNullException("pEMail");

            Game game = new Game(gName, pName, pEMail, COLS, ROWS, type);
            games[gName] = game;
            if (type == GameType.Public)
                UpdateRefreshGames(game);
            return true;
        }

        public bool ContainsGame(string gName) { return games.ContainsKey(gName); }
        public void ReserveGame(string gName) { games.Add(gName, null); }

        public IEnumerable<string> GetActiveGames( string eMail )
        {
            return (from g in games.Keys
                    where (games[g].Status == GameStatus.WAITING_FOR_PLAYERS && games[g].IsOwner(eMail))
                    select g);
        }

        //--------------------------
        // Players Management

        public IEnumerable<Player> GetOnlinePlayers()
        {
            return (from p in players.Values
                    where (p.Online)
                    select p);
        }

        public Player LoadPlayer(string eMail)
        {
            Player rObj = null;

            if (eMail != null)
            {
                try { rObj = players[eMail]; }
                catch (KeyNotFoundException) { }
                if (rObj == null) return rObj;
            }
            rObj.Status = PlayerStatus.Online;
            UpdatePlayersOnNewPlayer(rObj);
            UpdateGamesOnNewPlayer(rObj);
            UpdateRefreshPlayers(rObj);
            return rObj;
        }



        public Player GetPlayer(string name)
        {
            if (players.ContainsKey(name)) return players[name];
            return null;
        }

        public bool AddPlayer(Player player)
        {
            if (player == null) throw new ArgumentNullException("player");
            if (players.ContainsValue(player))
            {
                return false;
            }
            players.Add(player.EMail, player);
            return true;
        }

        public bool UpdatePlayer(Player player)
        {
            if (!players.ContainsValue(player) || player == null) return false;
            players[player.EMail] = player;
            return true;
        }


        //--------------------------
        // Invites Management

        public bool Invite(string gName, string eMailFrom, string eMailTo)
        {
            if (gName == null) throw new ArgumentNullException("gName");
            if (eMailFrom == null) throw new ArgumentNullException("eMailFrom");
            if (eMailTo == null) throw new ArgumentNullException("eMailTo");

            //if (!games.ContainsKey(gName)) throw new ApplicationException("Invalid Game!");
            if (!players.ContainsKey(eMailFrom)) throw new ApplicationException("Invalid Source Plyer!");
            if (!players.ContainsKey(eMailTo)) throw new ApplicationException("Invalid Destination Plyer!");


            Invite inv = new Invite()
            {
                Value = "Player " + players[eMailFrom].Name + " invites you to a private game! Accept?",
                Sender = eMailFrom,
                Game = gName,
                YesHandler = "",
                YesParam = gName,
                NoHandler = "",
                NoParam = eMailFrom
            };

            return players[eMailTo].AddRefreshInvites(gName, eMailFrom, inv);
        }

        //--------------------------
        // Friends Management

        public bool AddFriend(string eMail, string friend)
        {
            if (eMail == null) throw new ArgumentNullException("eMail");
            if (friend == null) throw new ArgumentNullException("friend");

            if (!players.ContainsKey(eMail)) return false;
            return (players[eMail].AddFriend(friend));
        }

        public bool RemoveFriend(string eMail, string friend)
        {
            if (eMail == null) throw new ArgumentNullException("eMail");
            if (friend == null) throw new ArgumentNullException("friend");

            if (!players.ContainsKey(eMail)) return false;
            return (players[eMail].RemoveFriend(friend));
        }
    }
}
