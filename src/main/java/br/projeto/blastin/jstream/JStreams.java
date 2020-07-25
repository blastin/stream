package br.projeto.blastin.jstream;

import br.projeto.blastin.joptional.Funcao;
import br.projeto.blastin.joptional.JOptional;
import br.projeto.blastin.joptional.Predicado;
import br.projeto.blastin.joptional.Provedor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collector;

/**
 * @param <T> tipo generico da classe final
 * @author Jefferson Lisboa < lisboa.jeff@gmail.com >
 * @apiNote JStream é uma releitura básica da biblioteca stream. A proposta é estudar e desenvolver
 * habilidades com a utilizao de interfaces para resolver o canivete <p> map, filter, reduce </p>
 */
public final class JStreams<T> implements JStream<T> {

    public static <T> JStream<T> de(final T t) {
        return new JStreams<>(t);
    }

    @SafeVarargs
    public static <T> JStream<T> de(final T... t) {
        return new JStreams<>(t, t.length);
    }

    @SuppressWarnings("unchecked")
    public static <T> JStream<T> de(final Collection<T> colecao) {
        Objects.requireNonNull(colecao);
        final T[] ts = (T[]) colecao.toArray();
        return new JStreams<>(ts, ts.length);
    }

    @SuppressWarnings("unchecked")
    public static <T> JStream<T> nula() {
        return (JStream<T>) JStreams.JSTREAM_NULO;
    }

    private static final JStream<?> JSTREAM_NULO = new JStreams<>(null);

    @SuppressWarnings("unchecked")
    private static <T> T[] instancias(final int tamanho) {
        return (T[]) new Object[tamanho];
    }

    private JStreams(final T t) {

        ts = instancias(1);

        if (t == null) {
            tamanho = 0;
        } else {
            ts[0] = t;
            tamanho = 1;
        }

    }

    private JStreams(final T[] ts, final int tamanho) {
        this.ts = ts;
        this.tamanho = tamanho;
    }

    private final T[] ts;

    private final int tamanho;

    @Override
    public <S> JStream<S> mapeamento(final Funcao<? super T, S> funcao) {
        return aplicar(() -> mapear(funcao), JStreams::nula);
    }

    @Override
    public JStream<T> filtro(final Predicado<? super T> predicado) {
        return aplicar(() -> filtrar(predicado), JStreams::nula);
    }

    @Override
    public JOptional<T> reducao(final OperacaoBinaria<T> operacaoBinaria) {
        return aplicar(() -> reduzir(operacaoBinaria), JOptional::nulo);
    }

    @Override
    public JOptional<T> primeiroValor() {
        if (vazio()) return JOptional.nulo();
        return JOptional.dePossivelNulo(ts[0]);
    }

    @Override
    public <A, R> R paraColecao(final Collector<? super T, A, R> collector) {
        return Arrays.stream(ts, 0, tamanho).collect(collector);
    }

    @Override
    public boolean presente() {
        return ts != null && tamanho > 0;
    }

    @Override
    public boolean vazio() {
        return !presente();
    }

    @Override
    public boolean peloMenosUmCombina(final Predicado<? super T> predicado) {
        return combinacoes(predicado, integer -> integer > 0);
    }

    @Override
    public boolean todosCombinam(final Predicado<? super T> predicado) {
        return combinacoes(predicado, integer -> integer == tamanho);
    }

    private <S> JStream<S> mapear(final Funcao<? super T, S> funcao) {

        final S[] ss = instancias(tamanho);

        int quantidadeMapeada = 0;

        for (int i = 0; i < tamanho; i++) {

            final S mapeado = funcao.aplicar(ts[i]);

            if (mapeado != null) ss[quantidadeMapeada++] = mapeado;

        }

        return new JStreams<>(ss, quantidadeMapeada);

    }

    private JStream<T> filtrar(final Predicado<? super T> predicado) {

        T[] tss = instancias(tamanho);

        int filtrados = 0;

        for (int i = 0; i < tamanho; i++) {

            final T t = ts[i];

            if (predicado.teste(t)) {
                tss[filtrados++] = t;
            }

        }

        if (filtrados == 0) tss = instancias(0);

        return new JStreams<>(tss, filtrados);

    }

    private JOptional<T> reduzir(final OperacaoBinaria<T> operacaoBinaria) {

        T acumulo = ts[0];

        for (int i = 1; i < tamanho; i++) {
            acumulo = operacaoBinaria.operar(acumulo, ts[i]);
        }

        return JOptional.dePossivelNulo(acumulo);

    }

    private boolean combinacoes(final Predicado<? super T> predicado, final Predicado<Integer> predicadoCombinacao) {
        return aplicar(() -> predicadoCombinacao.teste(quantidadeCombinacao(predicado)), () -> false);
    }

    private int quantidadeCombinacao(final Predicado<? super T> predicado) {

        int quantidade = 0;

        for (int i = 0; i < tamanho; i++) {
            if (predicado.teste(ts[i])) quantidade++;
        }

        return quantidade;

    }

    private <S> S aplicar(final Provedor<S> sucesso, final Provedor<S> outroCaso) {

        Objects.requireNonNull(sucesso);

        if (presente()) {
            return sucesso.prover();
        }

        return outroCaso.prover();

    }

}
