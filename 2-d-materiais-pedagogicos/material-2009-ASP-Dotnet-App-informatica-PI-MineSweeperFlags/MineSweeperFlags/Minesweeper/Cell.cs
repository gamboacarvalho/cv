using System;

namespace Minesweeper
{
    public abstract class Cell : IToJSon
    {
        protected CellType type;
        protected bool hidden;
        protected int ownerId = ~0;
        protected int _posX;
        protected int _posY;

        public Cell(CellType type, int posX, int posY)
        {
            this.type = type;
            hidden = true;
            _posX = posX;
            _posY = posY;
        }

        public int PosX
        {
            get { return _posX; }
        }

        public int PosY
        {
            get { return _posY; }
        }

        public CellType Type
        {
            get { return type; }
            set { type = value; }
        }

        public bool Hidden
        {
            get { return hidden; }
            set { hidden = value; }
        }

        public int Owner
        {
            get { return ownerId; }
            set { ownerId = value; }
        }

        public virtual String ToJSon()
        {
            return "{\"type\":\"" + type + "\", \"hidden\":" + (hidden ? 1 : 0) + ", \"posX\":" + _posX
                + ", \"posY\":" + _posY + ", \"owner\":" + ownerId + "}";
        }
    }
}
