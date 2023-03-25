using System;
using System.Collections.Generic;
using System.Threading;

namespace Minesweeper
{
    public class Game : IToJSon
    {
        const int MAX_PLAYERS = 4;
        const int MIN_PLAYERS = 2;
        const int TOTAL_MINES = 51;

        string _name;
        GameStatus _sStatus;
        GamePlayer[] _players;
        int _playersCount;
        Cell[,] _cells;
        int _lines;
        int _cols;
        int _currentPlayer;
        int _totalMines;
        int _minesLeft;
        GameType _gType = GameType.Public;

        public Game(string name, string playerName, string playerEMail, int cols, int rows)
            : this(name, playerName, playerEMail, cols, rows, GameType.Public) { }

        public Game(string name, string playerName, string playerEMail, int cols, int rows, GameType type)
        {
            _name = name;
            _sStatus = GameStatus.WAITING_FOR_PLAYERS;
            _gType = type;
            _players = new GamePlayer[MAX_PLAYERS];
            _lines = rows;
            _cols = cols;
            _cells = new Cell[rows, cols];

            _playersCount = 0;
            _currentPlayer = 0;
            AddPlayer(playerName, playerEMail); //Owner (id = 1)
        }

        public string Name
        {
            get { return _name; }
        }
        public int MinesLeft
        {
            get { return _minesLeft; }
        }
        public GameStatus Status
        {
            get { return _sStatus; }
        }

        public GameType Type
        {
            get { return _gType; }
            set { _gType = value; }
        }

        public int PlayersCount { get { return _playersCount; } }

        public int CurrentPlayer { get { return _currentPlayer; } }

        private List<GamePlayer> ScoreList
        {
            get
            {
                List<GamePlayer> scoreArr = new List<GamePlayer>(_players);
                scoreArr.RemoveAll(p => p == null || !p.Active);
                scoreArr.Sort((a, b) => b.Points.CompareTo(a.Points));
                return scoreArr;
            }
        }
        public List<GamePlayer> GetRefreshPlayer(int playerId)
        {
            List<GamePlayer> d = _players[playerId].GetRefreshPlayer();
            _players[playerId].ResetRefreshPlayer();
            return d;
        }
        public List<Cell> GetRefreshCell(int playerId)
        {
            List<Cell> sRef = _players[playerId].GetRefreshCell();
            _players[playerId].ResetRefreshCell();
            return sRef;
        }


        private int CalcMines()
        {
            if (TOTAL_MINES % _playersCount == 0)
                return TOTAL_MINES + 1;
            return TOTAL_MINES;
        }
        private void ReCalcMines()
        {
            if (_minesLeft % _playersCount == 0)
                _minesLeft -= 1;
        }
        private void GenMinesPos()
        {
            int cont = 0, x = 0, y = 0, absPos = 0;

            Random rNum = new Random();

            while (cont < _totalMines)
            {
                absPos = rNum.Next(0, (_cols * _lines) - 1);
                x = (int)(absPos / _cols);
                y = (int)(absPos % _cols);
                if (_cells[x, y] == null)
                {
                    _cells[x, y] = new CellMine(x, y);
                    cont++;
                }
            }
        }
        private void CalcValueCells()
        {
            //Calculates the value of each cell according to the mines already puted in the board
            for (int i = 0; i < _cols; i++)
            {
                for (int j = 0; j < _lines; j++)
                {
                    if (_cells[i, j].Type == CellType.Mine)
                    {
                        List<Cell> list = GetAdjacentCells(_cells[i, j]);
                        foreach (Cell c in list)
                        {
                            if (c.Type != CellType.Mine)
                                ((CellNumber)c).IncValue();
                        }
                    }
                }
            }
        }
        private List<Cell> GetAdjacentCells(Cell cell)
        {
            List<Cell> retList = new List<Cell>();

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    if ((i != 0 || j != 0) && isInBounds(cell.PosX + i, cell.PosY + j))
                    {
                        retList.Add(_cells[cell.PosX + i, cell.PosY + j]);
                    }
                }

            }
            return retList;
        }
        private bool isInBounds(int x, int y)
        {
            return x >= 0 && x < _cols && y >= 0 && y < _lines;
        }
        private void SetCurrentPlayer()
        {
            if (_sStatus != GameStatus.STARTED)
            {
                Random rPlayer = new Random();
                _currentPlayer = rPlayer.Next(0, _playersCount - 1);
            }
            else
            {
                do
                {
                    _currentPlayer = (_currentPlayer + 1) % _players.Length;
                } while (_players[_currentPlayer] == null || _players[_currentPlayer].Active == false);
            }
        }
        private bool CheckGameOver()
        {
            if ((_playersCount < 2) || (MinesLeft + ScoreList[1].Points < ScoreList[0].Points))
            {
                _sStatus = GameStatus.GAME_OVER;
                _currentPlayer = (ScoreList.Count == 0 ? 0 : ScoreList[0].Id);
                return true;
            }
            return false;
        }
        private bool ProcessCell(int playerID, int posX, int posY)
        {
            Cell cell = _cells[posX, posY];
            cell.Owner = _players[playerID].Id;
            cell.Hidden = false;
            foreach (GamePlayer p in _players)
            {
                if (p != null)
                    p.RefreshAddCell(cell);
            }
            if (cell.Type == CellType.Mine)
            {
                _minesLeft--;
                return true;
            }
            else
            {
                //If is Number and value equals 0 then chainReaction
                //If is Number and is not 0, nothing has to be done

                if (((CellNumber)cell).Value == 0)
                {
                    List<Cell> adjCell = GetAdjacentCells(cell);
                    foreach (Cell c in adjCell)
                    {
                        if (c.Type != CellType.Mine)
                        {
                            if (c.Hidden)
                                ProcessCell(playerID, c.PosX, c.PosY);
                        }
                    }
                }
                return false;
            }
        }

        public void Play(int playerID, int posX, int posY)
        {
            if (isInBounds(posX, posY)) //Sanity check
            {
                if (_cells[posX, posY].Hidden) //Another Sanity check
                {
                    if (ProcessCell(playerID, posX, posY))
                    {
                        _players[playerID].Points++;
                        if (CheckGameOver())
                            return;
                        foreach (GamePlayer player in _players)
                        {
                            if (player != null && player.Active == true)
                            {
                                player.RefreshAddPlayer(_players[playerID]);
                            }
                        }
                    }
                    else
                        SetCurrentPlayer();

                }
            }
        }

        public void RevealBoard(int playerId)
        {
            if (_sStatus == GameStatus.GAME_OVER)
            {
                foreach (Cell c in _cells)
                {
                    if (c.Hidden && (c.Type == CellType.Mine || c.Type == CellType.Bomb))
                    {
                        c.Type = CellType.Bomb;
                        _players[playerId].RefreshAddCell(c);
                    }
                }
            }
        }

        public int AddPlayer(string name, string eMail)
        {
            if (_playersCount < MAX_PLAYERS)
            {
                GamePlayer player = new GamePlayer(_playersCount + 1, name, eMail);
                _players[_playersCount++] = player;
                foreach (GamePlayer p in _players)
                {
                    if (p != null)
                    {
                        p.RefreshAddPlayer(player);
                        if (p.Id != player.Id)
                            player.RefreshAddPlayer(p);
                    }
                }
                return _playersCount;
            }
            return ~0;
        }
        public void RemovePlayer(int id)
        {
            _players[id].Active = false;
            _playersCount--;

            if (!CheckGameOver())
            {
                ReCalcMines();
                foreach (GamePlayer p in _players)
                {
                    if (p != null)
                        p.RefreshAddPlayer(_players[id]);
                }
                if (_players[_currentPlayer].Id == id)
                    SetCurrentPlayer();
            }
        }
        public bool Start()
        {
            if (_playersCount < 2) return false;

            _minesLeft = _totalMines = CalcMines();

            //Generate mines e puts them into the cell[,]
            GenMinesPos();

            //Fills the rest of cell[,] with CellNumber
            for (int i = 0; i < _cols; i++)
            {
                for (int j = 0; j < _lines; j++)
                {
                    if (_cells[i, j] == null)
                    {
                        _cells[i, j] = new CellNumber(i, j);
                    }
                }
            }
            //Calculate cellValues
            CalcValueCells();

            //Sets the player to start the game
            SetCurrentPlayer();
            _sStatus = GameStatus.STARTED;

            return true;
        }
        public GamePlayer GetPlayer(int playerId)
        {
            return _players[playerId];
        }

        public GamePlayer GetPlayer(string eMail)
        {
            for (int i = 0; i < _playersCount; i++)
            {
                if (string.Compare(eMail, _players[i].EMail) == 0) return _players[i];
            }

            return null;
        }

        public bool IsOwner(string eMail)
        {
            return string.Compare(eMail, _players[0].EMail) == 0;
        }

        public string ToJSon()
        {
            return "{\"name\":\"" + _name + "\", \"status\":\"" + _sStatus + "\"}";
        }
    }
}