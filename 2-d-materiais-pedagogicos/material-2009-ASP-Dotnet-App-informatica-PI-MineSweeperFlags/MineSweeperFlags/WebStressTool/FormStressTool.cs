using System;
using System.Windows.Forms;
using System.Threading;
using System.IO;
using WebStressTool.HttpClient;
using WebStressTool.Utils;


namespace WebStressTool
{
    public partial class FormStressTool : Form
    {
        int totalRequestCount;

        public FormStressTool()
        {
            InitializeComponent();
        }

        //Implementação recorrendo à tecnica de confinamento. ( não há concorrência )

        void InitProgressBarTotal( int maxValue ){

            if (pgbTotal.InvokeRequired)
            {
                pgbTotal.BeginInvoke(new BeginInvoke(delegate()
                {
                    pgbTotal.Minimum = 0;
                    pgbTotal.Maximum = maxValue;
                    pgbTotal.Value   = 0;
                }));

                return;
            }

            pgbTotal.Minimum = 0;
            pgbTotal.Maximum = maxValue;
            pgbTotal.Value   = 0;
        }

        void IncProgressBarTotal()
        {
            if (pgbTotal.InvokeRequired)
            {
                pgbTotal.BeginInvoke(new BeginInvoke(delegate()
                {
                    pgbTotal.Value += 1;
                }));

                return;
            }

            pgbTotal.Value += 1;
        }

        void ResetProgressBarParcial(  )
        {
            if (pgbPartial.InvokeRequired)
            {
                pgbPartial.BeginInvoke(new BeginInvoke(delegate()
                {
                    pgbPartial.Minimum = 0;
                    pgbPartial.Maximum = totalRequestCount;
                    pgbPartial.Value = 0;
                }));

                return;
            }

            pgbPartial.Minimum = 0;
            pgbPartial.Maximum = totalRequestCount;
            pgbPartial.Value   = 0;

        }

        void IncProgressBarParcial()
        {            
            if (pgbPartial.InvokeRequired)
            {
                pgbPartial.BeginInvoke(new BeginInvoke(delegate()
                {
                    if( pgbPartial.Value < pgbPartial.Maximum ) pgbPartial.Value += 1;
                }));

                return;
            }

            pgbPartial.Value += 1;
        }

        void setResultText(string text)
        {
            if (txtResult.InvokeRequired)
            {
                txtResult.BeginInvoke(new BeginInvoke(delegate()
                {
                    txtResult.AppendText(text);
                    txtResult.AppendText(Environment.NewLine);
                }));

                return;
            }

            txtResult.AppendText(text);
            txtResult.AppendText(Environment.NewLine);
        }

        protected void OnStartInvoke(object sender, EventArgs e)
        {
            InitProgressBarTotal(((StartInvokeEventArgs)e).TotalRequestFiles);
            setResultText("OnStartInvoke");

        }
        protected void OnStartInvokeRequest(object sender, EventArgs e) 
        {
            IncProgressBarTotal();
            setResultText("OnStartInvokeRequest");
        }
        protected void OnStartInvokeRequestItem(object sender, EventArgs e)
        {
            try
            {
                IncProgressBarParcial();
                setResultText("OnStartInvokeRequestItem");
            }
            catch (Exception exc) { setResultText(exc.Message); }
        }
        protected void OnEndInvokeRequestItem(object sender, EventArgs e)
        {
            setResultText("OnEndInvokeRequestItem");
        }
        protected void OnEndInvokeRequest(object sender, EventArgs e) 
        {
            ResetProgressBarParcial();
            setResultText("OnEndInvokeRequest");
        }
        protected void OnEndInvoke(object sender, EventArgs e)
        {
            setResultText("OnEndInvoke");
        }
        
       
        private void button1_Click(object sender, EventArgs e)
        {
            StressToolWorker worker = new StressToolWorker(new DirectoryInfo(fbdObject.SelectedPath));
            worker.StartInvoke += OnStartInvoke;
            worker.StartInvokeRequest += OnStartInvokeRequest;
            worker.StartInvokeRequestItem += OnStartInvokeRequestItem;
            worker.EndInvokeRequestItem += OnEndInvokeRequestItem;
            worker.EndInvokeRequest += OnEndInvokeRequest;
            worker.EndInvoke += OnEndInvoke;

            worker.Invoke( totalRequestCount = requestCount.Value );
        }

        private void FormStressTool_Load(object sender, EventArgs e)
        {
            requestCount_Scroll( null, null );
        }

        private void requestCount_Scroll(object sender, EventArgs e)
        {
            lblRequestCount.Text =  requestCount.Value.ToString("000000000");

        }

        private void btnShowDialog_Click(object sender, EventArgs e)
        {
            fbdObject.SelectedPath = AppDomain.CurrentDomain.BaseDirectory;
            fbdObject.ShowDialog();

            txtBaseDirectory.Text = fbdObject.SelectedPath;
            btnGo.Enabled = fbdObject.SelectedPath != null;
        }        

    }
}
