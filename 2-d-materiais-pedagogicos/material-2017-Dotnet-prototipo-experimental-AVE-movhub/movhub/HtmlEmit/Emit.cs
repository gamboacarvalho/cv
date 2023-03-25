using System;
using System.Reflection;
using System.Reflection.Emit;
using HtmlReflect.MyCustomAttributes;
using System.Collections.Generic;

namespace HtmlEmit{
    public class Emit{

        static readonly MethodInfo getHtml = typeof(HTMLUtils).GetMethod("GetHtml", new Type[] { typeof(String), typeof(object) });
        static readonly MethodInfo getHtmlToLink = typeof(HTMLUtils).GetMethod("GetHtmlToLink", new Type[] { typeof(String), typeof(object), typeof(String) });
        static readonly MethodInfo getHtmlHeader = typeof(HTMLUtils).GetMethod("GetHtmlHeader", new Type[] { typeof(String) });
        static readonly MethodInfo getHtmlArrayToLink = typeof(HTMLUtils).GetMethod("GetHtmlArrayToLink", new Type[] { typeof(object), typeof(String), typeof(String) });
        static readonly MethodInfo getHtmlArray = typeof(HTMLUtils).GetMethod("GetHtmlArray", new Type[] { typeof(object) });
        static readonly MethodInfo concat = typeof(String).GetMethod("Concat", new Type[] { typeof(string), typeof(string) });

        public static IHtmlParser EmitDetails(Type klass)
        {
            AssemblyName aName = new AssemblyName("DynamicGetter" + klass.Name);
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    aName,
                    AssemblyBuilderAccess.RunAndSave);

            // For a single-module assembly, the module name is usually
            // the assembly name plus an extension.
            ModuleBuilder mb =
                ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder tb = mb.DefineType(
                "Getter" + klass.Name,
                 TypeAttributes.Public,
                 typeof(HtmlDetails));

            PropertyInfo[] pi = klass.GetProperties();

            MethodBuilder methodBuilder = MyMethodBuilder(tb, "ParseToHtml", typeof(string), new Type[] { typeof(object) });
            ParseToHtmlBuilder(klass, methodBuilder, pi);

            // Finish the type.
            Type t = tb.CreateType();
            ab.Save(aName.Name + ".dll");
            return (IHtmlParser)Activator.CreateInstance(t);
        }
        public static IHtmlParser EmitTable<T>(Type klass)
        {
            AssemblyName aName = new AssemblyName("DynamicGetter" + klass.Name);
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    aName,
                    AssemblyBuilderAccess.RunAndSave);

            // For a single-module assembly, the module name is usually
            // the assembly name plus an extension.
            ModuleBuilder mb =
                ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder tb = mb.DefineType(
                "Getter" + klass.Name,
                 TypeAttributes.Public,
                 typeof(HtmlTable<T>));

            PropertyInfo[] pi = klass.GetProperties();

            MethodBuilder methodBuilder = MyMethodBuilder(tb, "ParseToHtmlArr", typeof(string), new Type[] { typeof(object) });
            ParseToHtmlArrBuilder(klass, methodBuilder, pi);

            methodBuilder = MyMethodBuilder(tb, "ParseToHtmlHeaders", typeof(string), new Type[] { } );
            ParseToHtmlHeaderBuilder(klass, methodBuilder, pi);

            // Finish the type.
            Type t = tb.CreateType();
            ab.Save(aName.Name + ".dll");
            return (IHtmlParser)Activator.CreateInstance(t);
        }

        private static void ParseToHtmlHeaderBuilder(Type klass, MethodBuilder methodBuilder, PropertyInfo[] pi)
        {
            ILGenerator il = methodBuilder.GetILGenerator();

            il.Emit(OpCodes.Ldstr, "<table class='table table-hover'>" +
                            "<thead>" +
                                "<tr>");
            foreach (PropertyInfo p in pi)
            {
                if (p.IsDefined(typeof(HtmlIgnoreAttribute))) continue;
                il.Emit(OpCodes.Ldstr, p.Name);
                il.Emit(OpCodes.Call, getHtmlHeader);
                il.Emit(OpCodes.Call, concat);
            }
            il.Emit(OpCodes.Ldstr, "</tr>" +
                            "</thead><tbody>");
            il.Emit(OpCodes.Call, concat);
            il.Emit(OpCodes.Ret);
        }

        private static void ParseToHtmlArrBuilder(Type klass, MethodBuilder methodBuilder, PropertyInfo[] pi)
        {
       
            ILGenerator il = methodBuilder.GetILGenerator();

            LocalBuilder target = il.DeclareLocal(klass);

            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Castclass, klass);
            il.Emit(OpCodes.Stloc, target);
            il.Emit(OpCodes.Ldstr, "<tr>");

            foreach (PropertyInfo p in pi)
            {
                if (p.IsDefined(typeof(HtmlIgnoreAttribute))) continue;

                il.Emit(OpCodes.Ldloc_0);
                il.Emit(OpCodes.Callvirt, p.GetGetMethod());
                if (p.PropertyType.IsValueType)
                    il.Emit(OpCodes.Box, p.PropertyType);
                if (p.IsDefined(typeof(HtmlAsAttribute)))
                {
                    il.Emit(OpCodes.Ldstr, p.Name);
                    il.Emit(OpCodes.Ldstr, ((HtmlAsAttribute)p.GetCustomAttribute(typeof(HtmlAsAttribute))).template);
                    il.Emit(OpCodes.Call, getHtmlArrayToLink);
                }
                else
                    il.Emit(OpCodes.Call, getHtmlArray);
                il.Emit(OpCodes.Call, concat);
            }
            il.Emit(OpCodes.Ldstr, "</tr>");
            il.Emit(OpCodes.Call, concat);
            il.Emit(OpCodes.Ret);
        }

        private static void ParseToHtmlBuilder(Type klass, MethodBuilder methodBuilder, PropertyInfo[] pi)
        {
            ILGenerator il = methodBuilder.GetILGenerator();

            LocalBuilder target = il.DeclareLocal(klass);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Castclass, klass);
            il.Emit(OpCodes.Stloc, target);

            il.Emit(OpCodes.Ldstr, "<ul class='list-group'>");
            foreach (PropertyInfo p in pi)
            {
                if (p.IsDefined(typeof(HtmlIgnoreAttribute))) continue;

                il.Emit(OpCodes.Ldstr, p.Name);
                il.Emit(OpCodes.Ldloc, target);
                il.Emit(OpCodes.Callvirt, p.GetGetMethod());

                if (p.PropertyType.IsValueType)
                    il.Emit(OpCodes.Box, p.PropertyType);
                if (p.IsDefined(typeof(HtmlAsAttribute)))
                {
                    il.Emit(OpCodes.Ldstr, ((HtmlAsAttribute)p.GetCustomAttribute(typeof(HtmlAsAttribute))).template);
                    il.Emit(OpCodes.Call, getHtmlToLink);
                }
                else
                    il.Emit(OpCodes.Call, getHtml);
                il.Emit(OpCodes.Call, concat);
            }
            il.Emit(OpCodes.Ldstr, "</ul>");
            il.Emit(OpCodes.Call, concat);
            il.Emit(OpCodes.Ret);
        }

        private static MethodBuilder MyMethodBuilder(TypeBuilder tb, string name, Type returnType, Type[] argumentsType)
        {
            return tb.DefineMethod(
                    name,
                    MethodAttributes.Public | MethodAttributes.Virtual | MethodAttributes.ReuseSlot,
                    returnType,
                    argumentsType
                );
        }
    }
}
