using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Web;
using System.Xml;
using System.IO;
using System.Web.SessionState;
using System.Security.Principal;

namespace MineSweeperLog
{
    public delegate bool logDelegate();
    public class Logger
    {
        private static Object mon = new Object();

        private String _appPath;  //Path to Log Folder under the application Folder
        private XmlDocument _doc;       //XmlDocument used to persist Log
        private FileStream _myStream;   //Stream used to write XML file

        private HttpContext _context;
        private HttpRequest _request;
        private HttpResponse _response;
        private HttpSessionState _session;
        private IPrincipal _user;
        
        private logDelegate _del;
        private AsyncCallback _cb;

        public Logger(HttpApplication application)
        {
            _context = application.Context;
            _request = application.Request;
            _response = application.Response;
            //_session = application.Session;
            _user = application.User;

            _del = new logDelegate(this.StartLog);
            _cb = new AsyncCallback(this.EndLog);
        }

        public logDelegate Delegate { get { return _del; } }
        public AsyncCallback CallBack { get { return _cb; } }

        public bool StartLog()
        {
            if (_context == null)
                _appPath = "C:\tmp"; // FMC ?????? MEU DEUS ????
            
            SetAppPath();             

            return LogItem();
        }
        
        public void EndLog(IAsyncResult res)
        {
            if (res.IsCompleted)//Fast Path
            {
                if(_del.EndInvoke(res))
                    return;
                XmlElement root = _doc.CreateElement("LogError");
                root.SetAttribute("Message", "Log returned false");
                _doc.AppendChild(root);
                Logger.WriteToFile(_doc, _appPath + "/LogFile_" + DateTime.Today.ToShortDateString() + ".xml");
                return;
            }
            lock (res.AsyncWaitHandle)
            {
                while (!res.IsCompleted)
                    Monitor.Wait(res.AsyncWaitHandle);
                if (_del.EndInvoke(res))
                    return;
                XmlElement root = _doc.CreateElement("Log Error");
                root.SetAttribute("Message", "Log returned false");
                _doc.AppendChild(root);
                Logger.WriteToFile(_doc, _appPath + "/LogFile_" + DateTime.Today.ToShortDateString() + ".xml");
            }
        }
        
        private void SetAppPath()
        {
            if (!Directory.Exists(_context.Server.MapPath("/Log")))
                Directory.CreateDirectory(_context.Server.MapPath("/Log"));

            _appPath = _context.Server.MapPath("/Log");
        }
        
        private bool LogItem()
        {
            _doc = new XmlDocument();
        
            //Cria o elemento raiz contendo informação do Registo de Log
            XmlElement root = _doc.CreateElement("Log");
            XmlElement contextElem = _doc.CreateElement("Context");
            if (_context != null)
            {
                contextElem.SetAttribute("TimeStamp", _context.Timestamp.ToString());
                contextElem.SetAttribute("IsDebuggingEnabled", _context.IsDebuggingEnabled.ToString());

                if (_context.AllErrors != null && _context.AllErrors.Length > 0)
                {
                    XmlElement errorsElem = _doc.CreateElement("Errors");
                    for (Int32 i = 0; i < _context.AllErrors.Length; i++)
                    {
                        XmlElement error = _doc.CreateElement("Error");
                        error.SetAttribute("Message", _context.AllErrors[i].Message);
                        error.SetAttribute("Source", _context.AllErrors[i].Source);
                        errorsElem.AppendChild(error);
                    }
                    contextElem.AppendChild(errorsElem);
                }
            }
            root.AppendChild(contextElem);
            XmlElement requestElem = _doc.CreateElement("Request");
            if (_request != null)
                requestElem = LogRequest();
            root.AppendChild(requestElem);
            XmlElement responseElem = _doc.CreateElement("Response");
            if (_response != null)
                responseElem = LogResponse();
            root.AppendChild(responseElem);
            XmlElement sessionElem = _doc.CreateElement("Session");
            if (_session != null)
                sessionElem = LogSession();
            root.AppendChild(sessionElem);

            XmlElement userElem = _doc.CreateElement("User");
            if (_user != null)
            {
                userElem.SetAttribute("Name", _user.Identity.Name);
            }
            root.AppendChild(userElem);
            try
            {
                contextElem.SetAttribute("IsCustomErrorEnabled", _context.IsCustomErrorEnabled.ToString());
            }
            catch (Exception) {}

            _doc.AppendChild(root);
            Logger.WriteToFile(_doc, _appPath + "/LogFile_" + DateTime.Today.ToShortDateString() + ".xml");

            return true;
        }
        
        private XmlElement LogRequest()
        {
            XmlElement requestElem = _doc.CreateElement("Request");
            if (_request.HttpMethod != null)
                requestElem.SetAttribute("Method", _request.HttpMethod);
            
            if (_request.QueryString.Count > 0)
            {
                XmlElement qs = _doc.CreateElement("QueryString");
                qs.SetAttribute("Count", _request.QueryString.Count.ToString());
                for (int i = 0; i < _request.QueryString.Count; i++)
                {
                    XmlElement paramElem = _doc.CreateElement(_request.QueryString.Keys[i]);
                    paramElem.InnerText += _request.QueryString[i];
                    qs.AppendChild(paramElem);
                }
                requestElem.AppendChild(qs);
            }
            if (_request.CurrentExecutionFilePath != null)
            {
                XmlElement e = _doc.CreateElement("Controller");
                e.InnerText = _request.CurrentExecutionFilePath;
                requestElem.AppendChild(e);
            }
            if (_request.Browser != null)
            {
                XmlElement e = _doc.CreateElement("Browser");
                e.InnerText = _request.Browser.Browser;
                requestElem.AppendChild(e);
            }
            if (_request.Cookies.Count > 0)
            {
                XmlElement cookies = _doc.CreateElement("Cookies");
                cookies.SetAttribute("Count", _request.Cookies.Count.ToString());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < _request.Cookies.Count; i++)
                {
                    sb.Append(_request.Cookies[0].Name);
                    if (i < _request.Cookies.Count - 1)
                        sb.Append(", ");
                }
                cookies.InnerText = sb.ToString();
                requestElem.AppendChild(cookies);
            }
            if (_request.ContentLength > 0)
            {
                XmlElement content = _doc.CreateElement("Content");
                content.SetAttribute("Length", _request.ContentLength.ToString());
                content.SetAttribute("Type", _request.ContentType);
                requestElem.AppendChild(content);
            }
            try
            {
                if (_request.Url != null)
                    requestElem.SetAttribute("Url", _request.Url.ToString());
            }
            catch (Exception) { }

            return requestElem;
        }
        
        private XmlElement LogResponse()
        {
            XmlElement responseElem = _doc.CreateElement("Response");
            responseElem.SetAttribute("ContentType", _response.ContentType);
            if (_response.Cookies.Count > 0)
            {
                XmlElement cookies = _doc.CreateElement("Cookies");
                cookies.SetAttribute("Count", _response.Cookies.Count.ToString());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < _response.Cookies.Count; i++)
                {
                    sb.Append(_response.Cookies[0].Name);
                    if (i < _response.Cookies.Count - 1)
                        sb.Append(", ");
                }
                cookies.InnerText = sb.ToString();
                responseElem.AppendChild(cookies);
            }
            if (_response.Status != null)
                responseElem.SetAttribute("Status", _response.Status);

            if (_response.Charset != null)
                responseElem.SetAttribute("Charset", _response.Charset);

            if (_response.ContentEncoding != null)
                responseElem.SetAttribute("ContentEncoding", _response.ContentEncoding.WebName);

            return responseElem;
        }

        private XmlElement LogSession()
        {
            XmlElement sessionElem = _doc.CreateElement("Session");
            sessionElem.SetAttribute("isCookieless", _session.IsCookieless.ToString());
            sessionElem.SetAttribute("isNewSession", _session.IsNewSession.ToString());
            sessionElem.SetAttribute("isReadOnly", _session.IsReadOnly.ToString());
            sessionElem.SetAttribute("isSynchronized", _session.IsSynchronized.ToString());
            if (_session.Keys.Count > 0)
            {
                XmlElement KeysElem = _doc.CreateElement("Keys");
                for (Int32 i = 0; i < _session.Keys.Count; i++)
                {
                    XmlElement key = _doc.CreateElement("key");
                    key.InnerText = _session.Keys[i];
                    KeysElem.AppendChild(key);
                }
                sessionElem.AppendChild(KeysElem);
            }
            sessionElem.SetAttribute("LCID", _session.LCID.ToString());
            sessionElem.SetAttribute("SessionMode", _session.Mode.ToString());
            sessionElem.SetAttribute("ID", _session.SessionID);
            sessionElem.SetAttribute("Timeout", _session.Timeout.ToString());

            return sessionElem;
        }

        private static void WriteToFile(XmlDocument doc, String path)
        {
            FileStream fStream;
            lock (mon)
            {
                fStream = File.Open(path, FileMode.Append);
                doc.Save(fStream);
                fStream.Close();
            }
        }
    }
}
