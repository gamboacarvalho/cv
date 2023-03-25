using Jsonzai.Reflect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Instr
{
    public static class SerializerBuilder
    {
        private const string ASSEMBLY_NAME = "Jsonfiers";
        private const string TYPE_PREFIX = "Jsonfier";
        private const string JSONFIER_METHOD = "ToJson";
        private const int COMMA = ',';
        private const int DOUBLE_QUOTES = '"';
        private const int COLON = ':';
        private const int BRACKET_OPEN= '{';
        private const int BRACKET_CLOSE = '}';

        private static readonly MethodInfo TO_STRING = typeof(object).GetMethod("ToString");
        private static readonly MethodInfo TO_LOWER = typeof(string).GetMethod("ToLower", Type.EmptyTypes);

        private static readonly Type[] ARG_OBJECT = new Type[1] { typeof(object) };
        private static readonly Type[] ARG_STRING = new Type[1] { typeof(string) };
        private static readonly Type[] ARG_BOOLEAN = new Type[1] { typeof(bool) };
        private static readonly Type[] ARG_CHAR = new Type[1] { typeof(char) };
        private static readonly Type[] ARG_SHORT = new Type[1] { typeof(short) };
        private static readonly Type[] ARG_INT = new Type[1] { typeof(int) };
        private static readonly Type[] ARG_LONG = new Type[1] { typeof(long) };
        private static readonly Type[] ARG_FLOAT = new Type[1] { typeof(float) };
        private static readonly Type[] ARG_DOUBLE = new Type[1] { typeof(double) };

        private static readonly Dictionary<Type, MethodInfo> APPENDERS;
        

        static SerializerBuilder (){
            APPENDERS = new Dictionary<Type, MethodInfo>();
            APPENDERS[typeof(string)] = typeof(StringBuilder).GetMethod("Append", ARG_STRING);
            APPENDERS[typeof(bool)] = typeof(StringBuilder).GetMethod("Append", ARG_BOOLEAN);
            APPENDERS[typeof(char)] = typeof(StringBuilder).GetMethod("Append", ARG_CHAR);
            APPENDERS[typeof(short)] = typeof(StringBuilder).GetMethod("Append", ARG_SHORT);
            APPENDERS[typeof(int)] = typeof(StringBuilder).GetMethod("Append", ARG_INT);
            APPENDERS[typeof(long)] = typeof(StringBuilder).GetMethod("Append", ARG_LONG);
            APPENDERS[typeof(float)] = typeof(StringBuilder).GetMethod("Append", ARG_FLOAT);
            APPENDERS[typeof(double)] = typeof(StringBuilder).GetMethod("Append", ARG_DOUBLE);
        }

        public static Serializer BuildSerializer(Type srcKlass)
        {
            
            //
            // Create Assembly Builder
            //
            ModuleBuilder mb;
            AssemblyBuilder asmBuilder = SerializerBuilder.CreateDllBuilder(ASSEMBLY_NAME, out mb);
            // 
            // Create Type Builder
            //
            TypeBuilder tb = mb.DefineType(TYPE_PREFIX + srcKlass.Name);
            tb.AddInterfaceImplementation(typeof(Serializer));
            // 
            // Create Method Builder
            //
            MethodBuilder toJsonBuilder = tb.DefineMethod(
                JSONFIER_METHOD,
                MethodAttributes.Virtual | MethodAttributes.Public | MethodAttributes.ReuseSlot,
                typeof(string),
                new Type[1] { typeof(object) });
            ImplementToJson(toJsonBuilder, srcKlass);
            //
            // Finish the type.
            //
            Type t = tb.CreateType();
            asmBuilder.Save(ASSEMBLY_NAME + ".dll");
            return (Serializer) Activator.CreateInstance(t);
        }

        private static void ImplementToJson(MethodBuilder toJsonBuilder, Type srcKlass)
        {
            PropertyInfo[] fields = srcKlass.GetProperties(BindingFlags.Public | BindingFlags.Instance);
            PropertyInfo last = fields.Last();
            ILGenerator il = toJsonBuilder.GetILGenerator();
            //
            // Declare and Init local variable 0
            //
            il.DeclareLocal(srcKlass); // Local variable 0
            il.Emit(OpCodes.Ldarg_1); //  Load src object
            il.Emit(OpCodes.Castclass, srcKlass); //  Cast to srcKlass
            il.Emit(OpCodes.Stloc_0); // Store object on local variable 0
            //
            // Instantiate StringBuidler
            //
            il.Emit(OpCodes.Newobj, typeof(StringBuilder).GetConstructor(Type.EmptyTypes));
            //
            // Build Json string into StringBuilder object
            //
            il.EmitCallToAppendChar(BRACKET_OPEN);
            il = fields
                .Aggregate(il, (prevIl, curr) => {
                    prevIl.EmitAppendMember(curr); // Appends Name : Value
                    if (curr != last)
                        prevIl.EmitCallToAppendChar(COMMA); // Appends COMMA separator to StringBuilder object. Append returns this for next call.
                    return prevIl; 
                });
            il.EmitCallToAppendChar(BRACKET_CLOSE);
            il.EmitCall(OpCodes.Callvirt, TO_STRING, Type.EmptyTypes);
            il.Emit(OpCodes.Ret);

        }
        private static void EmitAppendMember(this ILGenerator il, PropertyInfo pi)
        {
            il.EmitCallToAppendChar(DOUBLE_QUOTES);
            il.Emit(OpCodes.Ldstr, pi.Name); // Load field name
            il.EmitCall(OpCodes.Call, APPENDERS[typeof(string)], ARG_STRING); // Append field name to StringBuilder
            il.EmitCallToAppendChar(DOUBLE_QUOTES);
            il.EmitCallToAppendChar(COLON);
            il.EmitAppendInstanceField(pi); // Load instance field value and append it to StringBuilder
        }
        private static void EmitAppendInstanceField(this ILGenerator il, PropertyInfo fi)
        {
            if (fi.PropertyType == typeof(bool)) 
            {
                il.EmitAppendInstanceFieldBool(fi); // differentiate bool because tostring returns cap letter
            }
            else if (fi.PropertyType.IsPrimitive) 
            {
                il.EmitAppendInstanceFieldPrimitive(fi);
            }
            else if (fi.PropertyType == typeof(string))
            {
                il.EmitAppendInstanceFieldString(fi);
            }
            else
            { // Redirect to Jsonfier
                il.Emit(OpCodes.Ldloc_0); //  Loads local variable 0 -- the src parameter
                il.EmitCall(OpCodes.Call, fi.GetGetMethod(), Type.EmptyTypes); // Gets property and loads on stack
                il.EmitCall(OpCodes.Call, typeof(Jsoninstr).GetMethod("ToJson", ARG_OBJECT), ARG_OBJECT);
                il.EmitCall(OpCodes.Call, APPENDERS[typeof(string)], ARG_STRING);
            }
        }
        private static void EmitAppendInstanceFieldBool(this ILGenerator il, PropertyInfo fi)
        {
            il.Emit(OpCodes.Ldloc_0); //  Loads local variable 0 -- the src parameter
            il.EmitCall(OpCodes.Call, fi.GetGetMethod(), Type.EmptyTypes); // Gets property and loads on stack
            il.Emit(OpCodes.Box, typeof(bool));
            il.EmitCall(OpCodes.Callvirt, TO_STRING, Type.EmptyTypes);
            il.EmitCall(OpCodes.Callvirt, TO_LOWER, Type.EmptyTypes);
            il.EmitCall(OpCodes.Call, APPENDERS[typeof(string)], ARG_STRING);
        }
        private static void EmitAppendInstanceFieldPrimitive(this ILGenerator il, PropertyInfo fi)
        {
            il.Emit(OpCodes.Ldloc_0); //  Loads local variable 0 -- the src parameter
            il.EmitCall(OpCodes.Call, fi.GetGetMethod(), Type.EmptyTypes); // Gets property and loads on stack
            il.EmitCallToAppend(fi); // call StringBuilder::Append. The Append returns the StringBuilder for next call.
        }
        private static void EmitAppendInstanceFieldString(this ILGenerator il, PropertyInfo fi)
        {
            il.EmitCallToAppendChar(DOUBLE_QUOTES);
            il.EmitAppendInstanceFieldPrimitive(fi);
            il.EmitCallToAppendChar(DOUBLE_QUOTES);
        }
        private static void EmitCallToAppend(this ILGenerator il, PropertyInfo fi)
        {
            il.EmitCall(OpCodes.Call, APPENDERS[fi.PropertyType], new Type[1] { fi.PropertyType}); // !!! Reuse args type array
        }

        private static void EmitCallToAppendChar(this ILGenerator il, int character)
        {
            il.Emit(OpCodes.Ldc_I4, character);
            il.EmitCall(OpCodes.Call, APPENDERS[typeof(char)], ARG_CHAR);
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
