using SqlReflect.Attributes;
using System;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;

namespace SqlReflect
{
    public class EmitDataMapper
    {
        private static readonly ConstructorInfo baseCtor = 
            typeof(DynamicDataMapper).GetConstructor(new Type[] { typeof(Type), typeof(string), typeof(bool)});

        private static readonly MethodInfo drGetItemMethod = 
            typeof(IDataRecord).GetMethod("get_Item", new Type[] { typeof(string) });

        public static IDataMapper Build(Type klass, string connStr, bool withCache) {
            string asmName = "EmitDataMapper" + klass.Name;
            //
            // Create Assembly Builder
            //
            ModuleBuilder mb;
            AssemblyBuilder asmBuilder = CreateDllBuilder(asmName, out mb);
            // 
            // Create Type Builder
            //
            TypeBuilder tb = mb.DefineType(asmName, TypeAttributes.Public, typeof(DynamicDataMapper));
            //
            // Implement abstract methods
            // 
            PropertyInfo[] props = klass.GetProperties();
            PropertyInfo pk = props.First(p => p.IsDefined(typeof(PKAttribute)));
            props = props.Where(prop => prop != pk).ToArray();

            AddConstructor(klass, tb, props, pk);
            AddMethodLoad(klass, tb, props, pk);
            AddMethodSqlInsert(klass, tb, props, pk);
            AddMethodSqlUpdate(klass, tb, props, pk);
            AddMethodSqlDelete(klass, tb, props, pk);
            //
            // Finish the type.
            //
            Type t = tb.CreateType();
            asmBuilder.Save(asmName + ".dll");
            return (IDataMapper)Activator.CreateInstance(t, new object[] { klass, connStr, withCache });
        }

        private static void AddConstructor(Type klass, TypeBuilder tb, PropertyInfo[] props, PropertyInfo pk)
        {
            Type[] parameterTypes = { typeof(Type), typeof(string), typeof(bool) };
            ConstructorBuilder ctor = 
                tb.DefineConstructor(MethodAttributes.Public, CallingConventions.Standard, parameterTypes);
            ILGenerator il = ctor.GetILGenerator();
            il.Emit(OpCodes.Ldarg_0);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Ldarg_2);
            il.Emit(OpCodes.Ldarg_3);
            il.Emit(OpCodes.Call, baseCtor);
            il.Emit(OpCodes.Ret);
        }

        private static void AddMethodLoad(Type klass, TypeBuilder tb, PropertyInfo[] props, PropertyInfo pk)
        {
            MethodBuilder m = tb.DefineMethod(
                "Load",
                MethodAttributes.Virtual | MethodAttributes.Public | MethodAttributes.ReuseSlot,
                typeof(object),
                new Type[] { typeof(IDataReader) });
            
            ILGenerator il = m.GetILGenerator();
            il.Emit(OpCodes.Newobj, klass.GetConstructor(Type.EmptyTypes));
            EmitLoadSetProperty(il, pk);
            foreach (var p in props) EmitLoadSetProperty(il, p);
            il.Emit(OpCodes.Ret);
        }
        private static void EmitLoadSetProperty(ILGenerator il, PropertyInfo prop)
        {
            il.Emit(OpCodes.Dup);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Ldstr, prop.Name);
            il.Emit(OpCodes.Callvirt, drGetItemMethod);
            if (prop.PropertyType.IsPrimitive)
                il.Emit(OpCodes.Unbox_Any, prop.PropertyType);
            else
                il.Emit(OpCodes.Isinst, prop.PropertyType);
            il.Emit(OpCodes.Callvirt, prop.GetSetMethod());
        }

        private static void AddMethodSqlInsert(Type klass, TypeBuilder tb, PropertyInfo[] props, PropertyInfo pk)
        {
            MethodBuilder m = tb.DefineMethod(
                "SqlInsert",
                MethodAttributes.Virtual | MethodAttributes.Public | MethodAttributes.ReuseSlot,
                typeof(string),
                new Type[] { typeof(object) });
            ILGenerator il = m.GetILGenerator();
            il.Emit(OpCodes.Ldstr, "");
            il.Emit(OpCodes.Ret);
        }
        private static void AddMethodSqlUpdate(Type klass, TypeBuilder tb, PropertyInfo[] props, PropertyInfo pk)
        {
            MethodBuilder m = tb.DefineMethod(
                "SqlUpdate",
                MethodAttributes.Virtual | MethodAttributes.Public | MethodAttributes.ReuseSlot,
                typeof(string),
                new Type[] { typeof(object) });
            ILGenerator il = m.GetILGenerator();
            il.Emit(OpCodes.Ldstr, "");
            il.Emit(OpCodes.Ret);
        }
        private static void AddMethodSqlDelete(Type klass, TypeBuilder tb, PropertyInfo[] props, PropertyInfo pk)
        {
            MethodBuilder m = tb.DefineMethod(
                "SqlDelete",
                MethodAttributes.Virtual | MethodAttributes.Public | MethodAttributes.ReuseSlot,
                typeof(string),
                new Type[] { typeof(object) });
            ILGenerator il = m.GetILGenerator();
            il.Emit(OpCodes.Ldstr, "");
            il.Emit(OpCodes.Ret);
        }
        private static AssemblyBuilder CreateDllBuilder(string name, out ModuleBuilder mb)
        {
            AssemblyName asmName = new AssemblyName(name);
            AssemblyBuilder asmBuilder =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    asmName,
                    AssemblyBuilderAccess.RunAndSave);
            //
            // For a single-module assembly, the module name is usually 
            // the assembly name plus an extension.
            //
            mb = asmBuilder.DefineDynamicModule(asmName.Name, asmName.Name + ".dll");
            return asmBuilder;
        }

    }
}
