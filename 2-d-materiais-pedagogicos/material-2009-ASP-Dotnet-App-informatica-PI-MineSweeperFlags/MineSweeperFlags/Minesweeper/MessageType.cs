using System;

namespace Minesweeper
{
    [Flags]
    public enum MessageType
    {
         Undefined      = 0x0
        ,PrivateMessage = 0x1
        ,PublicMessage  = 0x2 
    }
}
