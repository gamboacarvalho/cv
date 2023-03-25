using System;

namespace Minesweeper
{
    public class Invite : Message
    {

        public string YesHandler { get; set; }
        public string YesParam { get; set; }
        public string NoHandler { get; set; }
        public string NoParam { get; set; }
        public string Game { get; set; }

        public override string ToJSon()
        {
            string rObj;
            rObj = "{\"msg\":\"" + Value + "\" , \"sender\":\"" + Sender + "\" , \"gName\":\"" + Game + "\" ,"
                    + "\"yHandler\":\"" + YesHandler + "\" , \"yParam\":\"" + YesParam + "\" ,"
                    + "\"nHandler\":\"" + NoHandler + "\" , \"nParam\":\"" + NoParam + "\"}";
            return rObj;
        }
    }
}
