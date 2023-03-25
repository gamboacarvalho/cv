using System;

namespace DAOBuilder.Attributes
{
    public class SqlCmdAttribute:Attribute
    {
        public string Command { get; set; }

        public SqlCmdAttribute(string cmd)
        {
            Command = cmd;
        }
    }
}
 