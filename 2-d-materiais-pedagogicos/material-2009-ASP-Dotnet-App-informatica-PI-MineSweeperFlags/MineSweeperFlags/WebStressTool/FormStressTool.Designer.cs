namespace WebStressTool
{
    partial class FormStressTool
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnGo = new System.Windows.Forms.Button();
            this.txtResult = new System.Windows.Forms.TextBox();
            this.requestCount = new System.Windows.Forms.TrackBar();
            this.lblRequestCount = new System.Windows.Forms.Label();
            this.fbdObject = new System.Windows.Forms.FolderBrowserDialog();
            this.txtBaseDirectory = new System.Windows.Forms.TextBox();
            this.btnShowDialog = new System.Windows.Forms.Button();
            this.pgbTotal = new System.Windows.Forms.ProgressBar();
            this.pgbPartial = new System.Windows.Forms.ProgressBar();
            ((System.ComponentModel.ISupportInitialize)(this.requestCount)).BeginInit();
            this.SuspendLayout();
            // 
            // btnGo
            // 
            this.btnGo.Enabled = false;
            this.btnGo.Location = new System.Drawing.Point(416, 218);
            this.btnGo.Name = "btnGo";
            this.btnGo.Size = new System.Drawing.Size(75, 23);
            this.btnGo.TabIndex = 0;
            this.btnGo.Text = "Go";
            this.btnGo.UseVisualStyleBackColor = true;
            this.btnGo.Click += new System.EventHandler(this.button1_Click);
            // 
            // txtResult
            // 
            this.txtResult.Location = new System.Drawing.Point(12, 10);
            this.txtResult.Multiline = true;
            this.txtResult.Name = "txtResult";
            this.txtResult.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.txtResult.Size = new System.Drawing.Size(479, 166);
            this.txtResult.TabIndex = 1;
            // 
            // requestCount
            // 
            this.requestCount.Location = new System.Drawing.Point(3, 210);
            this.requestCount.Maximum = 1000;
            this.requestCount.Minimum = 1;
            this.requestCount.Name = "requestCount";
            this.requestCount.Size = new System.Drawing.Size(307, 42);
            this.requestCount.TabIndex = 2;
            this.requestCount.Value = 1;
            this.requestCount.Scroll += new System.EventHandler(this.requestCount_Scroll);
            // 
            // lblRequestCount
            // 
            this.lblRequestCount.AutoSize = true;
            this.lblRequestCount.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblRequestCount.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(0)))), ((int)(((byte)(0)))));
            this.lblRequestCount.Location = new System.Drawing.Point(322, 221);
            this.lblRequestCount.Name = "lblRequestCount";
            this.lblRequestCount.Size = new System.Drawing.Size(0, 20);
            this.lblRequestCount.TabIndex = 3;
            // 
            // txtBaseDirectory
            // 
            this.txtBaseDirectory.Enabled = false;
            this.txtBaseDirectory.Location = new System.Drawing.Point(13, 185);
            this.txtBaseDirectory.Name = "txtBaseDirectory";
            this.txtBaseDirectory.Size = new System.Drawing.Size(438, 20);
            this.txtBaseDirectory.TabIndex = 4;
            // 
            // btnShowDialog
            // 
            this.btnShowDialog.Location = new System.Drawing.Point(457, 185);
            this.btnShowDialog.Name = "btnShowDialog";
            this.btnShowDialog.Size = new System.Drawing.Size(34, 23);
            this.btnShowDialog.TabIndex = 5;
            this.btnShowDialog.Text = "...";
            this.btnShowDialog.UseVisualStyleBackColor = true;
            this.btnShowDialog.Click += new System.EventHandler(this.btnShowDialog_Click);
            // 
            // pgbTotal
            // 
            this.pgbTotal.ForeColor = System.Drawing.Color.SaddleBrown;
            this.pgbTotal.Location = new System.Drawing.Point(13, 249);
            this.pgbTotal.Name = "pgbTotal";
            this.pgbTotal.Size = new System.Drawing.Size(297, 23);
            this.pgbTotal.TabIndex = 6;
            // 
            // pgbPartial
            // 
            this.pgbPartial.ForeColor = System.Drawing.Color.SandyBrown;
            this.pgbPartial.Location = new System.Drawing.Point(316, 249);
            this.pgbPartial.Name = "pgbPartial";
            this.pgbPartial.Size = new System.Drawing.Size(175, 23);
            this.pgbPartial.TabIndex = 7;
            // 
            // FormStressTool
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(515, 284);
            this.Controls.Add(this.pgbPartial);
            this.Controls.Add(this.pgbTotal);
            this.Controls.Add(this.btnShowDialog);
            this.Controls.Add(this.txtBaseDirectory);
            this.Controls.Add(this.lblRequestCount);
            this.Controls.Add(this.requestCount);
            this.Controls.Add(this.txtResult);
            this.Controls.Add(this.btnGo);
            this.MaximizeBox = false;
            this.Name = "FormStressTool";
            this.Text = ".: Web Stresssss Tool :.";
            this.Load += new System.EventHandler(this.FormStressTool_Load);
            ((System.ComponentModel.ISupportInitialize)(this.requestCount)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnGo;
        private System.Windows.Forms.TextBox txtResult;
        private System.Windows.Forms.TrackBar requestCount;
        private System.Windows.Forms.Label lblRequestCount;
        private System.Windows.Forms.FolderBrowserDialog fbdObject;
        private System.Windows.Forms.TextBox txtBaseDirectory;
        private System.Windows.Forms.Button btnShowDialog;
        private System.Windows.Forms.ProgressBar pgbTotal;
        private System.Windows.Forms.ProgressBar pgbPartial;
    }
}

