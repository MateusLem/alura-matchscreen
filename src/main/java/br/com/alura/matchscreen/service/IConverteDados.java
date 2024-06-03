package br.com.alura.matchscreen.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
