using System;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;
using Webao;
using Webao.Attributes;

namespace WebaoDyn
{
    public class WebaoDynBuilder
    {
        public static object Build(Type webao, IRequest req) {
            object[] attrs = webao.GetCustomAttributes(typeof(BaseUrlAttribute), true);
            if (attrs != null && attrs.Length > 0)
            {
                string host = ((BaseUrlAttribute)attrs[0]).host;
                req.BaseUrl(host);
            }
            attrs = webao.GetCustomAttributes(typeof(AddParameterAttribute), true);
            foreach (var at in attrs)
            {
                AddParameterAttribute a = (AddParameterAttribute)at;
                req.AddParameter(a.name, a.val);
            }
            return Activator.CreateInstance(BuildWebao(webao), req);
        }
        public static Type BuildWebao(Type klass)
        {
            AssemblyName myAssemblyName = new AssemblyName();
            myAssemblyName.Name = klass.Name + "Lib";

            // Define a dynamic assembly in the current application domain.
            AssemblyBuilder myAssemblyBuilder = AppDomain
                .CurrentDomain
                .DefineDynamicAssembly(myAssemblyName, AssemblyBuilderAccess.RunAndSave);

            // Define a dynamic module in this assembly.
            ModuleBuilder myModuleBuilder = myAssemblyBuilder.
                                          DefineDynamicModule(myAssemblyName.Name, myAssemblyName.Name + ".dll");

            // Define a runtime class with specified name and attributes.
            // <=> class <klass.name>Dyn : <klass.name>
            //
            TypeBuilder myTypeBuilder = myModuleBuilder.DefineType(
                klass.Name + "Dyn",
                TypeAttributes.Public,
                typeof(object),
                new Type[] { klass });

            // Add 'req' field to the class, with the specified attribute and type.
            FieldBuilder req = myTypeBuilder
                .DefineField("req", typeof(IRequest), FieldAttributes.Private);

            BuildConstructor(myTypeBuilder, req);
            foreach(MethodInfo mi in klass.GetMethods())
                BuildGetMethod(myTypeBuilder, mi, req);

            // Create the Getter<klass.name><p.name>
            Type t = myTypeBuilder.CreateType();

            // The following line saves the single-module assembly. This
            // requires AssemblyBuilderAccess to include Save. You can now
            // type "ildasm MyDynamicAsm.dll" at the command prompt, and 
            // examine the assembly. You can also write a program that has
            // a reference to the assembly, and use the MyDynamicType type.
            // 
            myAssemblyBuilder.Save(myAssemblyName.Name + ".dll");
            return t;
        }

        private static void BuildConstructor(TypeBuilder tb, FieldBuilder req)
        {
            // Define a default constructor that supplies a default value
            // for the private field. For parameter types, pass the empty
            // array of types or pass null.
            ConstructorBuilder ctor0 = tb.DefineConstructor(
                MethodAttributes.Public,
                CallingConventions.Standard,
                new Type[] { typeof(IRequest)});

            ILGenerator ctor0IL = ctor0.GetILGenerator();
            // For a constructor, argument zero is a reference to the new
            // instance. Push it on the stack before.
            // Specify the default constructor of the 
            // base class (System.Object) by passing an empty array of 
            // types (Type.EmptyTypes) to GetConstructor.
            ctor0IL.Emit(OpCodes.Ldarg_0);
            ctor0IL.Emit(OpCodes.Call,
                typeof(object).GetConstructor(Type.EmptyTypes));
            // Push the instance on the stack before pushing the argument
            // that is to be assigned to the private field req.
            ctor0IL.Emit(OpCodes.Ldarg_0);
            ctor0IL.Emit(OpCodes.Ldarg_1);
            ctor0IL.Emit(OpCodes.Stfld, req);
            ctor0IL.Emit(OpCodes.Ret);
        }

        static readonly MethodInfo REPLACE = typeof(string).GetMethod("Replace", new Type[] { typeof(string), typeof(string) });
        static readonly MethodInfo GET_TYPE = typeof(Type).GetMethod("GetTypeFromHandle", new Type[] { typeof(RuntimeTypeHandle) });
        static readonly MethodInfo REQUEST = typeof(IRequest).GetMethod("Get", new Type[] { typeof(string), typeof(Type) });
        private static void BuildGetMethod(TypeBuilder myTypeBuilder, MethodInfo mi, FieldBuilder req)
        {
            GetAttribute attr = (GetAttribute)mi.GetCustomAttribute(typeof(GetAttribute));
            string path = attr.path;

            MethodBuilder getValue = myTypeBuilder.DefineMethod(
                mi.Name,
                MethodAttributes.Public | MethodAttributes.ReuseSlot |
                MethodAttributes.HideBySig | MethodAttributes.Virtual,
                CallingConventions.Standard,
                mi.ReturnType, // Return type
                mi.GetParameters().Select(p => p.ParameterType).ToArray());
            ILGenerator il = getValue.GetILGenerator();
            //
            // 0. Load field req
            //
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Ldfld, req);
            //
            // 1. Load path and Replace path with arguments values
            //
            il.Emit(OpCodes.Ldstr, path);
            int index = 1;
            foreach (ParameterInfo p in mi.GetParameters()) {
                il.Emit(OpCodes.Ldstr, "{" + p.Name + "}");
                if (p.ParameterType.IsValueType)
                {
                    il.Emit(OpCodes.Ldarga_S, index);
                    il.Emit(OpCodes.Call, p.ParameterType.GetMethod("ToString", Type.EmptyTypes));
                }
                else {
                    il.Emit(OpCodes.Ldarg, index);
                    il.Emit(OpCodes.Callvirt, typeof(object).GetMethod("ToString", Type.EmptyTypes));
                }
                il.Emit(OpCodes.Callvirt, REPLACE);
                index++;
            }
            //
            // 2. Get typeof
            //
            il.Emit(OpCodes.Ldtoken, mi.ReturnType);
            il.Emit(OpCodes.Call, GET_TYPE);
            //
            // call re.Get(path, type)
            //
            il.Emit(OpCodes.Callvirt, REQUEST);
            if (mi.ReturnType.IsValueType)
                il.Emit(OpCodes.Unbox_Any, mi.ReturnType);
            else
                il.Emit(OpCodes.Castclass, mi.ReturnType);
            il.Emit(OpCodes.Ret);// ret
        }
    }
}
