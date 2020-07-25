package br.projeto.blastin.jstream;

import br.projeto.blastin.joptional.Funcao;
import br.projeto.blastin.joptional.JOptional;
import br.projeto.blastin.joptional.Predicado;

import java.util.stream.Collector;

public interface JStream<T> {

    <S> JStream<S> mapeamento(Funcao<? super T, S> funcao);

    JStream<T> filtro(Predicado<? super T> predicado);

    JOptional<T> reducao(OperacaoBinaria<T> operacaoBinaria);

    JOptional<T> primeiroValor();

    <A, R> R paraColecao(Collector<? super T, A, R> collector);

    boolean presente();

    boolean vazio();

    boolean peloMenosUmCombina(Predicado<? super T> predicado);

    boolean todosCombinam(Predicado<? super T> predicado);

}
