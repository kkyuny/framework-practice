package com.example.mvc;

import com.example.mvc.annotation.RequestMapping;
import com.example.mvc.controller.RequestMethod;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, AnnotationHandler> handlers = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> clazzesWithControllerAnnotation = reflections.getTypesAnnotatedWith(com.example.mvc.annotation.Controller.class, true);

        System.out.println("=== 컨트롤러 클래스 수: " + clazzesWithControllerAnnotation.size());
        clazzesWithControllerAnnotation.forEach(c -> System.out.println(" → 클래스: " + c.getName()));

        clazzesWithControllerAnnotation.forEach(clazz ->
                Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod -> {
                    RequestMapping requestMappingAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);

                    if (requestMappingAnnotation == null) return; // 예외 처리

                    Arrays.stream(getRequestMethods(requestMappingAnnotation))
                            .forEach(requestMethod -> {
                                HandlerKey key = new HandlerKey(requestMappingAnnotation.value(), requestMethod);
                                AnnotationHandler handler = new AnnotationHandler(clazz, declaredMethod);
                                handlers.put(key, handler);

                                // ✅ 등록된 핸들러 로그
                                System.out.println(" → 핸들러 등록됨: " + key + " → " + clazz.getSimpleName() + "#" + declaredMethod.getName());
                            });
                })
        );

        System.out.println("=== 총 등록된 핸들러 수: " + handlers.size());
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMappingAnnotation) {
        return requestMappingAnnotation.method();
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
