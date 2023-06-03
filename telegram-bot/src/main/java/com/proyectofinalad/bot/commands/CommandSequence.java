package com.proyectofinalad.bot.commands;

public interface CommandSequence<T> extends Command<T> {

    void executePrevious(T t);

    void executeNext(T t);

}
