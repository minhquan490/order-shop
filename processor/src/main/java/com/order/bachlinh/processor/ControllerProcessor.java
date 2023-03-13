package com.order.bachlinh.processor;

import com.google.auto.service.AutoService;
import com.order.bachlinh.processor.internal.ClassInfoCreator;
import com.order.bachlinh.processor.internal.ClassWriterBuilderProvider;
import com.order.bachlinh.processor.spi.ClassInfo;
import com.order.bachlinh.processor.spi.ClassWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.order.bachlinh.processor.annotation.Controller")
public final class ControllerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Optional<? extends Set<? extends Element>> optionalElements = annotations.stream().map(roundEnv::getElementsAnnotatedWith).findAny();
        if (optionalElements.isPresent()) {
            List<ClassInfo> classInfo = optionalElements
                    .get()
                    .stream()
                    .map(Element::asType)
                    .map(name -> {
                       try {
                           return classLoader.loadClass(name.toString());
                       } catch (ClassNotFoundException e) {
                           return null;
                       }
                    })
                    .map(clazz -> {
                        if (clazz != null) {
                            return ClassInfoCreator.createClassInfo(clazz);
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
            ClassWriter.Builder builder = ClassWriterBuilderProvider.getClassWriterBuilder();
            builder.classInfo(classInfo);
            builder.filer(processingEnv.getFiler());
            ClassWriter classWriter = builder.build();
            try {
                classWriter.write();
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
            return true;
        } else {
            return false;
        }
    }
}
