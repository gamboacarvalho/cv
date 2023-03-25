using System;
using System.IO;
using System.Threading;
using WebStressTool.Proxy;
using WebStressTool.HttpClient;


namespace WebStressTool
{
    internal class StressToolWorker
    {
        delegate void AsyncInvoke(FileInfo[] requestItems );

        public EventHandler StartInvoke            = null;
        public EventHandler StartInvokeRequest     = null;
        public EventHandler StartInvokeRequestItem = null;
        public EventHandler EndInvokeRequestItem   = null;
        public EventHandler EndInvokeRequest       = null;
        public EventHandler EndInvoke              = null;

        

        DirectoryInfo    baseDirectory; 
        int              requestCount;
        int              requestItemCount;
        int              totalRequestFiles;
        ManualResetEvent doRequestManualEvent;

        public StressToolWorker( DirectoryInfo baseDirectory  )
        {
            if (baseDirectory == null) throw new ArgumentNullException("baseDirectory");
            if (!baseDirectory.Exists) throw new ArgumentException("baseDirectory");

            this.baseDirectory = baseDirectory;

            doRequestManualEvent = new ManualResetEvent(false);
        }

        protected void OnEndInvokeRequestItem(object sender, EventArgs args)
        {
            EndRequestArgs eRequest = ((EndRequestArgs)args);
            try
            {
                eRequest.DataReader.Close();

                eRequest.DataWriter.Flush();
                eRequest.DataWriter.Close();

                if (EndInvokeRequestItem != null) EndInvokeRequestItem(this, new EndRequestEventArgs(eRequest.State));
            }
            finally
            {
                if (System.Threading.Interlocked.Decrement(ref requestItemCount) == 0) doRequestManualEvent.Set();
            }
        }


        void DoRequestItem( object requestItem )
        {
            RequestItem request = (RequestItem)requestItem;

            if (StartInvokeRequestItem != null) StartInvokeRequestItem(this, new RequestItemEventArgs( request ));

            AsynchronousHttpClient ac = new AsynchronousHttpClient(request.RequestUrl, new StreamReader(request.SourceFilePath), new StreamWriter( request.DestinationFilePath ));
            ac.EndRequest += OnEndInvokeRequestItem;
            ac.DoRequest();
        }

        void RunInvoke(FileInfo[] requestItems)
        {
            foreach (FileInfo request in requestItems)
            {
                doRequestManualEvent.Reset();
                if (StartInvokeRequest != null) StartInvokeRequest(this, new InvokeRequestEventArgs(request));                
                requestItemCount = requestCount;                
                for (int i = 0; i < requestCount; i++)
                {
                    ThreadPool.QueueUserWorkItem(DoRequestItem, new RequestItem(request, i));
                }

                doRequestManualEvent.WaitOne(System.Threading.Timeout.Infinite);
                
                if (EndInvokeRequest != null) EndInvokeRequest(this, new InvokeRequestEventArgs(request));
            }

            if (EndInvoke != null) EndInvoke(this, null);
        }

        public void Invoke( int count )
        {            
            FileInfo[] requestItems = baseDirectory.GetFiles("*.req");
            if (requestItems != null)
            {
                if (StartInvoke != null) StartInvoke(this, new StartInvokeEventArgs(requestItems.Length));

                totalRequestFiles  = requestItems.Length;
                requestCount       = count;
                AsyncInvoke method = new AsyncInvoke(RunInvoke);
                method.BeginInvoke(requestItems, null, null);
            }
        }
    }
}
