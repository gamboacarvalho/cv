using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;

namespace Mocky
{
    public class ClassBuilder
    {

        static readonly MethodInfo MOCKMETHOD_CALL = typeof(MockMethod).GetMethod("Call");

        // 1. Gerar uma classe que implementa ou extende de klass
        // 2. Instanciar a classe 1. e retornar essa instância.
        public static object Build (Type itf, MockMethod[] ms)
        {
            AssemblyName aName = new AssemblyName("Mock" + itf.Name);
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    aName,
                    AssemblyBuilderAccess.RunAndSave);

            // For a single-module assembly, the module name is usually
            // the assembly name plus an extension.
            ModuleBuilder mb =
                ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder mock =  mb.DefineType(
                "Mock" + itf.Name, 
                TypeAttributes.Public, 
                typeof(object), 
                new Type[] { itf });

            // Add a private field of List<MockMethod> to store ms
            FieldBuilder fld = mock.DefineField("ms", typeof(MockMethod[]), FieldAttributes.Private);

            // Add constructor
            BuildCtor(mock, fld);

            // Override methods
            foreach (var m in GetMethods(itf))
                BuildMethod(itf, mock, m, ms, fld);

            // Finish the type.
            Type t = mock.CreateType();

            // The following line saves the single-module assembly. This
            // requires AssemblyBuilderAccess to include Save. 
            // 
            ab.Save(aName.Name + ".dll");
            return Activator.CreateInstance(t, new object[1] { ms });
        }

        private static IEnumerable<MethodInfo> GetMethods(Type itf)
        {
            foreach (var m in itf.GetMethods()) {
                yield return m;
            }
            foreach (var i in itf.GetInterfaces()) {
                foreach (var m in GetMethods(i))
                    yield return m;
            }
        }

        private static void BuildCtor(TypeBuilder mock, FieldInfo fld)
        {
            ILGenerator il = mock
                            .DefineConstructor(
                                        MethodAttributes.Public,
                                        CallingConventions.Standard,
                                        new Type[] { typeof(MockMethod[]) }
                            )
                            .GetILGenerator();
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Call, typeof(object).GetConstructor(Type.EmptyTypes));
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Stfld, fld);
            il.Emit(OpCodes.Ret);
        }

        private static void BuildMethod(Type itf, TypeBuilder mock, MethodInfo m, MockMethod[] ms, FieldInfo fld)
        {
            Type[] args = m.GetParameters().Select(mth => mth.ParameterType).ToArray();

            ILGenerator il = mock
                            .DefineMethod(
                                m.Name,
                                m.Attributes & ~MethodAttributes.Abstract,
                                m.ReturnType,
                                args)
                            .GetILGenerator();


            int idx = -1;
            for (int i = 0; i < ms.Length; i++)
                if (ms[i].Method == m)
                    idx = i;
            if (idx >= 0)
            {
                MockMethod mm = ms[idx];
                il.Emit(OpCodes.Ldarg_0);     // Push Array fld on Top of Stack
                il.Emit(OpCodes.Ldfld, fld);  // END
                il.Emit(OpCodes.Ldc_I4, idx); // Read element of idx from Array
                il.Emit(OpCodes.Ldelem_Ref);  // END
                il.Emit(OpCodes.Ldc_I4, args.Length); // New Array for arguments
                il.Emit(OpCodes.Newarr, typeof(Object)); // END
                int i = 0;
                foreach (Type t in args) {
                    il.Emit(OpCodes.Dup);         // Dup array of the Top of the stack
                    il.Emit(OpCodes.Ldc_I4, i);
                    il.Emit(OpCodes.Ldarg, i + 1);
                    if (t.IsValueType)
                        il.Emit(OpCodes.Box, t);
                    il.Emit(OpCodes.Stelem_Ref);
                    i++;
                }
                il.Emit(OpCodes.Callvirt, MOCKMETHOD_CALL);
                if (m.ReturnType.IsValueType)
                    il.Emit(OpCodes.Unbox_Any, m.ReturnType);
                il.Emit(OpCodes.Ret);
            }
            else {
                il.Emit(OpCodes.Newobj, typeof(NotImplementedException).GetConstructor(Type.EmptyTypes));
                il.Emit(OpCodes.Throw);
            }    
        }
    }
}
