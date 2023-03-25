using Jsonzai.Converters;
using Jsonzai.Cache.Setters;
using System;
using System.Reflection;
using System.Reflection.Emit;

namespace Jsonzai.Cache {
    class SetterGenerator {

        public Type KlassType { get; private set; }

        private readonly static Type baseType = typeof(BaseSetter);

        private readonly static MethodInfo getGetConverter = baseType.GetProperty("Converter").GetGetMethod();

        private readonly static Type[] baseConstructorParamTypes = new Type[] { typeof(PropertyInfo) };
        private readonly static Type[] baseConstructorWithConverterParamTypes = new Type[] { typeof(PropertyInfo), typeof(IConverter) };
        private readonly static ConstructorInfo baseConstructor = baseType.GetConstructor(baseConstructorParamTypes);
        private readonly static ConstructorInfo baseConstructorWithConverter = baseType.GetConstructor(baseConstructorWithConverterParamTypes);

        public SetterGenerator(Type type) {
            this.KlassType = type;
        }

        public BaseSetter GenerateSetter(PropertyInfo p, IConverter converter) {
            AssemblyName assemblyName = new AssemblyName {
                Name = "LibSetter" + KlassType.Name + p.Name
            };

            // Define a dynamic assembly in the current application domain.
            AssemblyBuilder assemblyBuilder = AppDomain
                .CurrentDomain
                .DefineDynamicAssembly(assemblyName, AssemblyBuilderAccess.RunAndSave);

            // Define a dynamic module in this assembly.
            ModuleBuilder moduleBuilder = assemblyBuilder.
                                          DefineDynamicModule(assemblyName.Name, assemblyName.Name + ".dll");

            // Define a runtime class with specified name and attributes.
            // <=> class Setter<KlassType.name><p.Name> : BaseSetter 
            //
            TypeBuilder typeBuilder = moduleBuilder.DefineType(
                "Setter" + KlassType.Name + p.Name,
                TypeAttributes.Public,
                typeof(BaseSetter));

            if (converter != null) {
                BuildConstructorWithConverter(typeBuilder);
            }
            else {
                BuildConstructor(typeBuilder);
            }

            BuildSetValueMethod(typeBuilder, p, (converter != null));


            // Create the Setter<KlassType.name><p.name>
            Type t = typeBuilder.CreateType();

            // The following line saves the single-module assembly. This
            // requires AssemblyBuilderAccess to include Save. You can now
            // type "ildasm LibSetter.dll" at the command prompt, and 
            // examine the assembly. You can also write a program that has
            // a reference to the assembly, and use the MyDynamicType type.
            // 
            assemblyBuilder.Save(assemblyName.Name + ".dll");

            if (converter != null) {
                return (BaseSetter)Activator.CreateInstance(t, p, converter);
            }

            return (BaseSetter)Activator.CreateInstance(t, p);
        }

        private void BuildConstructor(TypeBuilder typeBuilder) {
            ConstructorBuilder ctorBuilder = typeBuilder.DefineConstructor(MethodAttributes.Public, baseConstructor.CallingConvention , baseConstructorParamTypes);

            ILGenerator ctorIL = ctorBuilder.GetILGenerator();
            ctorIL.Emit(OpCodes.Ldarg_0);
            ctorIL.Emit(OpCodes.Ldarg_1);
            ctorIL.Emit(OpCodes.Call, baseConstructor);
            ctorIL.Emit(OpCodes.Ret);
        }

        private void BuildConstructorWithConverter(TypeBuilder typeBuilder) {
            ConstructorBuilder ctorBuilder = typeBuilder.DefineConstructor(MethodAttributes.Public, baseConstructorWithConverter.CallingConvention, baseConstructorWithConverterParamTypes);

            ILGenerator ctorIL = ctorBuilder.GetILGenerator();
            ctorIL.Emit(OpCodes.Ldarg_0);
            ctorIL.Emit(OpCodes.Ldarg_1);
            ctorIL.Emit(OpCodes.Ldarg_2);
            ctorIL.Emit(OpCodes.Call, baseConstructorWithConverter);
            ctorIL.Emit(OpCodes.Ret);
        }

        private void BuildSetValueMethod(TypeBuilder typeBuilder, PropertyInfo p, bool converter) {
            MethodBuilder setValue = typeBuilder.DefineMethod(
                "SetValue",
                MethodAttributes.Public | MethodAttributes.ReuseSlot |
                MethodAttributes.HideBySig | MethodAttributes.Virtual,
                CallingConventions.Standard,
                typeof(object), // Return type
                new Type[] { typeof(object), typeof(object) });

            // Add IL to SetValue body
            ILGenerator methodIL = setValue.GetILGenerator();
            LocalBuilder localTarget = null;

            methodIL.Emit(OpCodes.Ldarg_1);                             // ldarg.1
            if (KlassType.IsValueType) {
                localTarget = methodIL.DeclareLocal(KlassType);
                methodIL.Emit(OpCodes.Unbox_Any, KlassType);            // unbox_any     Object -> ValueType
                methodIL.Emit(OpCodes.Stloc, localTarget);              // stloc          localTarget 
                methodIL.Emit(OpCodes.Ldloca_S, 0);                     // ldloca.s V_0
            }
            else {
                methodIL.Emit(OpCodes.Castclass, KlassType);            // castclass     Object -> ReferenceType
            }

            if (converter) {
                methodIL.Emit(OpCodes.Ldarg_0);                             // ldarg.0
                methodIL.Emit(OpCodes.Callvirt, getGetConverter);           // call          instance class IConvert BaseSetter::get_Converter()

                methodIL.Emit(OpCodes.Ldarg_2);                             // ldarg.2
                methodIL.Emit(OpCodes.Castclass, typeof(string));           // castclass     System.String

                methodIL.Emit(OpCodes.Callvirt, typeof(IConverter).GetMethod("Convert"));  //callvirt instance object IConvert::Convert(string)
            } else {
                methodIL.Emit(OpCodes.Ldarg_2);                            // ldarg.2
            }

            if (p.PropertyType.IsValueType) {
                methodIL.Emit(OpCodes.Unbox_Any, p.PropertyType);       // unbox         Object -> ValueType
            }
            else {
                methodIL.Emit(OpCodes.Castclass, p.PropertyType);       // castclass     Object -> ReferenceType
            }

            methodIL.Emit(OpCodes.Callvirt, p.GetSetMethod());

            if (KlassType.IsValueType) {
                methodIL.Emit(OpCodes.Ldloc, localTarget);              // ldloc         localTarget
                methodIL.Emit(OpCodes.Box, KlassType);                  // box           KlassType
            }
            else {
                methodIL.Emit(OpCodes.Ldarg_1);                         // ldarg.1
            }
            methodIL.Emit(OpCodes.Ret);                                 // ret
        }
    }
}
