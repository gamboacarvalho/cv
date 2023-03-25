using System;

namespace Minesweeper
{
    public class CellMine: Cell
    {
        public CellMine(int posX, int posY): base(CellType.Mine , posX , posY)
        {
        }
    }
}
