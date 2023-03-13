package com.order.bachlinh.processor.internal;

import com.order.bachlinh.processor.spi.ClassInfo;
import com.order.bachlinh.processor.spi.ClassWriter;
import com.order.bachlinh.processor.spi.MethodInfo;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collection;

final class DefaultClassWriter implements ClassWriter {
    private final Filer filer;
    private final Collection<ClassInfo> classInfo;

    DefaultClassWriter(Collection<ClassInfo> info, Filer filer) {
        this.filer = filer;
        this.classInfo = info;
    }
    @Override
    public void write() throws IOException {
        for (ClassInfo info : classInfo) {
            int count = 0;
            for (MethodInfo methodInfo : info.getMethods()) {
                JavaFileObject javaFileObject = filer.createSourceFile(info.getName().concat(String.valueOf(count)));
                try (PrintWriter writer = new PrintWriter(javaFileObject.openWriter())) {
                    String className = info.getName().concat(String.valueOf(count));
                    writer.println(MessageFormat.format(getPackagePattern(), info.getPackage().getName()));
                    writer.println(MessageFormat.format(getClassAnnotationPattern(), info.getPackage().getName().concat(".").concat(info.getName())));
                    writer.println(MessageFormat.format(getClassPattern(), className, methodInfo.getReturnType().getSimpleName(), info.getName(), methodInfo.getParameterTypes()[0].getSimpleName(), getBracketLeft()));
                    writer.print(getTab());
                    writer.println(resolveStaticMutableCallSite(methodInfo));
                    writer.print(getTab());
                    writer.println("private static final java.lang.invoke.MethodHandle invoker = callSite.dynamicInvoker();");
                    writer.print(getTab());
                    writer.println("private final java.lang.invoke.MethodHandle mh;");
                    writer.print(getTab());
                    writer.print("public ");
                    writer.print(className);
                    writer.print("() ");
                    writer.println(getBracketLeft());
                    writer.print(getTab());
                    writer.print(getTab());
                    writer.println("MethodHandles.Lookup lookup = java.lang.invoke.MethodHandles.lookup()");
                    writer.println(resolveMethodType(methodInfo));
                    writer.print(getTab());
                    writer.print(getTab());
                    writer.println(resolveMethodHandle(methodInfo));
                    writer.println("callSite.setTarget(mh);");
                    writer.println(getBracketRight());
                    writer.println();
                    writer.print(getTab());
                    writer.println("@Override");
                    writer.println();
                    writer.print(getTab());
                    writer.println(resolveMethodOverride(info, methodInfo));
                    writer.println(getBracketRight());
                    writer.flush();
                }
                count++;
            }
        }
    }

    private String resolveMethodOverride(ClassInfo classInfo, MethodInfo methodInfo) {
        return "public " + methodInfo.getReturnType().getName() +
                " " +
                "invoke(" +
                classInfo.getPackage().getName().concat(".").concat(classInfo.getName()) +
                " target, " +
                methodInfo.getParameterTypes()[0].getName() +
                " param) " +
                getBracketLeft() +
                System.lineSeparator() +
                getTab() +
                getTab() +
                "return " +
                "(" +
                methodInfo.getReturnType() +
                ") " +
                "invoker.invokeExact(target, param);" +
                getBracketRight();
    }

    private String resolveStaticMutableCallSite(MethodInfo methodInfo) {
        StringBuilder builder = new StringBuilder("private static final java.lang.invoke.MutableCallSite callSite = new java.lang.invoke.MutableCallSite(java.lang.invoke.MethodType.methodType(");
        builder.append(methodInfo.getReturnType().getName().concat(".class"));
        for (Class<?> param : methodInfo.getParameterTypes()) {
            builder.append(", ");
            builder.append(param.getName().concat(".class"));
        }
        builder.append("));");
        return builder.toString();
    }

    private String resolveMethodType(MethodInfo methodInfo) {
        StringBuilder builder = new StringBuilder("java.lang.invoke.MethodType methodType = java.lang.invoke.MethodType.methodType(");
        builder.append(methodInfo.getReturnType().getName().concat(".class"));
        for (Class<?> param : methodInfo.getParameterTypes()) {
            builder.append(", ");
            builder.append(param.getName().concat(".class"));
        }
        builder.append(");");
        return builder.toString();
    }

    private String resolveMethodHandle(MethodInfo methodInfo) {
        return "mh = " + "lookup.findVirtual(" +
                methodInfo.getReturnType().getName().concat(".class") +
                ", " +
                methodInfo.getName() +
                ", " +
                "methodType)";
    }

    private String getPackagePattern() {
        return "package {0};";
    }

    private String getClassPattern() {
        return "public final class {0} implements com.order.bachlinh.processor.spi.GenericController<{1}, {2}, {3}> {4}";
    }

    private String getClassAnnotationPattern() {
        return "@ReferenceTo({0}.class)";
    }

    private char getBracketLeft() {
        return '{';
    }

    private char getBracketRight() {
        return '}';
    }

    private char getTab() {
        return '\t';
    }

    static ClassWriter.Builder builder() {
        return new DefaultClassWriterBuilder();
    }

    static class DefaultClassWriterBuilder implements ClassWriter.Builder {
        private Collection<ClassInfo> classInfo;
        private Filer filer;

        @Override
        public Builder classInfo(Collection<ClassInfo> classInfo) {
            this.classInfo = classInfo;
            return this;
        }

        @Override
        public Builder filer(Filer filer) {
            this.filer = filer;
            return this;
        }


        @Override
        public ClassWriter build() {
            return new DefaultClassWriter(classInfo, filer);
        }
    }
}
