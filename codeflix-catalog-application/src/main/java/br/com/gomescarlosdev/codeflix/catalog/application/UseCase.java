package br.com.gomescarlosdev.codeflix.catalog.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);

}
