using System;
using System.Collections.Generic;

namespace Minesweeper
{
    public class GamePlayer : IToJSon
    {
        int  _id;
        string _name;
        string _eMail;
        int  _points;
        bool _active;
        readonly List<Cell>       _refreshCell;
        readonly List<GamePlayer> _refreshPlayer;

        public GamePlayer(int id) : this(id, null) { }

        public GamePlayer(int id, string name) : this(id, name, null) { }

        public GamePlayer(int id, string name, string eMail)
        { 
            _id = id;
            _active = true;
            _points = 0;
            _name = name;
            _eMail = eMail;
            _refreshCell   = new List<Cell>();
            _refreshPlayer = new List<GamePlayer>();
        } 

        public int Id
        {
            get { return _id; }
            set { _id = value; }
        }

        public string Name
        {
            get { return _name; }
            set { _name = value; }
        }

        public string EMail
        {
            get { return _eMail; }
            set { _eMail = value; }
        }

        public int Points
        {
            get { return _points; }
            set { _points = value; }
        }

        public bool Active
        {
            get { return _active; }
            set { _active = value; }
        }


        public void RefreshAddPlayer(GamePlayer p)
        {
            lock (_refreshPlayer)
            {
                _refreshPlayer.Add(p);
            }
        }

        public List<GamePlayer> GetRefreshPlayer()
        {
            List<GamePlayer> retList;
            lock (_refreshPlayer)
            {
                retList = new List<GamePlayer>(_refreshPlayer);
            }
            return retList;
        }

        public void ResetRefreshPlayer()
        {
            lock (_refreshPlayer)
            {
                _refreshPlayer.Clear();
            }
        }

        public void RefreshAddCell(Cell c)
        {
            lock (_refreshCell)
            {
                _refreshCell.Add(c);
            }
        }

        public List<Cell> GetRefreshCell()
        {
            List<Cell> retList;
            lock (_refreshCell)
            {
                retList = new List<Cell>(_refreshCell);
            }
            return retList;
        }

        public void ResetRefreshCell()
        {
            lock (_refreshCell)
            {
                _refreshCell.Clear();
            }
        }

        public string ToJSon()
        {
            return "{\"id\":\"" + _id + "\", \"name\":\"" + _name + "\", \"points\":" + _points
            + ", \"active\":" + (_active ? 1 : 0) + ", \"email\":\"" + _eMail + "\"}";
        }
    }
}
