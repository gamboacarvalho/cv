using Newtonsoft.Json;
using System;
using System.Diagnostics;
using System.Reflection;
using Webao.Attributes;

namespace Webao
{
    public abstract class AbstractAccessObject
    {
        private readonly IRequest req;
        
        protected AbstractAccessObject(IRequest req)
        {
            this.req = req;
        }

        public object Request(params object[] args) {
            StackTrace stackTrace = new StackTrace();
            MethodInfo callSite = (MethodInfo) stackTrace.GetFrame(1).GetMethod();
            return req.Get(Path(callSite, args), callSite.ReturnType); 
        }
        private static string Path(MethodInfo mth, object[] args) {
            GetAttribute attr = (GetAttribute)mth.GetCustomAttribute(typeof(GetAttribute));
            string path = attr.path;
            ParameterInfo[] argsInfos = mth.GetParameters();
            if (args.Length != argsInfos.Length)
                throw new InvalidOperationException("Get Request with different parameters than provided arguments!");
            int i = 0;
            foreach (var info in argsInfos) {
                string arg = "{" + info.Name + "}";
                if (!path.Contains("{" + info.Name + "}"))
                    throw new ArgumentException("There is no Get path argument for the method parameter: " + info.Name);
                path = path.Replace(arg, args[i++].ToString());
            }
            /**
             * !!!!! Missing validation if there is still any {...} arg in path for substitution.
             */
            return path; 
        }
    }
}
