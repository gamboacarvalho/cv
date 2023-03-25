using System;

namespace Minesweeper
{
    [Flags]
    public enum GameStatus
    {
         WAITING_FOR_PLAYERS = 0
        ,WAITING_FOR_START   = 1
        ,STARTED             = 2
        ,INVALID_NAME        = 3
        ,CROWDED             = 4
        ,GAME_OVER           = 5
    }
}
