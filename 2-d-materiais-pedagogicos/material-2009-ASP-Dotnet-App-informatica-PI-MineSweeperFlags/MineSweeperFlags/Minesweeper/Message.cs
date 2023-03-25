using System;

namespace Minesweeper
{
    public class Message : IToJSon
    {
        public Message()
            : this(MessageType.Undefined, null, null) { }

        public Message(string message, string owner)
            : this(MessageType.Undefined, message, owner) { }

        public Message(MessageType type, string message, string sender)
        {
            Type = type;
            Value = message;
            Sender = sender;
        }

        public MessageType Type { get; set; }
        public string Value { get; set; }
        public string Sender { get; set; }

        public virtual string ToJSon()
        {
            return "{\"msg\":\"" + Value + "\" , \"sender\":\"" + Sender + "\"}"; ;
        }
    }
}
