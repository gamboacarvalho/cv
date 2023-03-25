using System;

namespace Minesweeper
{
    internal class CellNumber: Cell
    {
        int value;

        public CellNumber(int posX, int posY): base(CellType.Number , posX , posY) 
        {
            value = 0;
        }

        public int Value
        {
            get { return value; }
            set { }
        }

        public void IncValue()
        {
            value ++;
        }

        public override String ToJSon()
        {
            return "{\"type\":\"" + type + "\", \"hidden\":" + (hidden ? 1 : 0) + ", \"posX\":" + _posX
                + ", \"posY\":" + _posY + ", \"owner\":" + ownerId + ", \"value\":" + value + "}";
        }

    }
}
