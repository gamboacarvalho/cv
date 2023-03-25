using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.IO;

namespace WebStressTool.HttpClient
{
    internal class AsynchronousHttpClient
    {
        const int    BufferSize = 256;

        public EventHandler EndRequest = null;

        Uri          requestUrl = null;
        StreamReader dataReader = null;
        StreamWriter dataWriter = null;
        Socket       workSocket = null;
        byte[]       buffer     = null;


        public AsynchronousHttpClient(Uri requestUrl, StreamReader dataReader, StreamWriter dataWriter)
        {
            if (requestUrl == null) throw new ArgumentNullException("requestUrl");
            if (dataReader == null) throw new ArgumentNullException("dataReader");
            if (dataWriter == null) throw new ArgumentNullException("dataWriter");

            this.requestUrl = requestUrl;
            this.dataReader = dataReader;
            this.dataWriter = dataWriter;
        }

        public Uri RequestUrl { get { return requestUrl; } }

        public void DoRequest()
        {
            try
            {            
                IPHostEntry ipHostInfo = Dns.GetHostEntry( requestUrl.Host );
                IPAddress   ipAddress  = ipHostInfo.AddressList[0];
                IPEndPoint  remoteEP   = new IPEndPoint(ipAddress, requestUrl.Port);

                workSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                workSocket.BeginConnect(remoteEP, new AsyncCallback(ConnectCallback), this);            
            }
            catch (Exception e)
            {
                dataWriter.WriteLine(e.ToString());
                if( EndRequest != null ) EndRequest.Invoke( this, new EndRequestArgs( dataReader, dataWriter, RequestState.Error ));
            }
        }

        private void ConnectCallback(IAsyncResult ar)
        {
            try
            {
                workSocket.EndConnect(ar);
                dataWriter.WriteLine("Socket connected to {0}", workSocket.RemoteEndPoint.ToString());

                Send(dataReader.ReadToEnd() + Environment.NewLine);

            }
            catch (Exception e)
            {
                dataWriter.WriteLine(e.ToString());
                if (EndRequest != null) EndRequest.Invoke(this, new EndRequestArgs(dataReader, dataWriter, RequestState.Error));
            }
        }

        private void Receive()
        {
            try
            {
                buffer = new byte[BufferSize];

                workSocket.BeginReceive(buffer, 0, BufferSize, 0,
                    new AsyncCallback(ReceiveCallback), this);
            }
            catch (Exception e)
            {
                dataWriter.WriteLine(e.ToString());
                if (EndRequest != null) EndRequest.Invoke(this, new EndRequestArgs(dataReader, dataWriter, RequestState.Error));
            }
        }

        private void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                int bytesRead = workSocket.EndReceive(ar);

                if (bytesRead > 0)
                {              
                    dataWriter.Write(Encoding.ASCII.GetString(buffer, 0, bytesRead));

                    workSocket.BeginReceive(buffer, 0, BufferSize, 0,
                        new AsyncCallback(ReceiveCallback), this);
                }
                else
                {
                    workSocket.Shutdown(SocketShutdown.Both);
                    workSocket.Close();
                    if (EndRequest != null) EndRequest.Invoke(this, new EndRequestArgs(dataReader, dataWriter, RequestState.Success)); 
                }
            }
            catch (Exception e)
            {
                if (dataWriter != null && dataWriter.BaseStream != null && dataWriter.BaseStream.CanWrite) dataWriter.WriteLine(e.ToString());
                if (EndRequest != null) EndRequest.Invoke(this, new EndRequestArgs(dataReader, dataWriter, RequestState.Error));
            }
        }

        private  void Send(String data)
        {
            byte[] byteData = Encoding.ASCII.GetBytes(data);

            workSocket.BeginSend(byteData, 0, byteData.Length, 0,
                new AsyncCallback(SendCallback), this);
        }

        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                int bytesSent = workSocket.EndSend(ar);
                dataWriter.WriteLine("Sent {0} bytes to server.", bytesSent);

                Receive();
            }
            catch (Exception e)
            {
                dataWriter.WriteLine(e.ToString());
                if (EndRequest != null) EndRequest.Invoke(this, new EndRequestArgs(dataReader, dataWriter, RequestState.Error));
            }
        }
    }
}
