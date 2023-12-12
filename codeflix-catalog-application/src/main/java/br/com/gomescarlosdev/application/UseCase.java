package br.com.gomescarlosdev.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);

}
