package com.proyectofinalad.bot.commands;

public interface Command<T> {

    void execute(T t);

}
