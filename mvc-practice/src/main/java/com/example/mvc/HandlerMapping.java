package com.example.mvc;

public interface HandlerMapping {
    Object findHandler(HandlerKey handlerKey);
}
