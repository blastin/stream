package br.projeto.blastin.jstream;

import br.projeto.blastin.joptional.JOptional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class JStreamTest {

    @Test
    void objetoNulo() {

        Assertions
                .assertTrue(
                        JStreams
                                .de((Object) null)
                                .vazio()
                );

    }

    @Test
    void streamNulo() {

        Assertions
                .assertTrue(
                        JStreams
                                .nula()
                                .vazio()
                );

    }

    @Test
    void primeiroValorDeStreamNula() {
        Assertions
                .assertTrue(JStreams.nula().primeiroValor().vazio());
    }

    @Test
    void primeiroValorDeStreamVazia() {

        final Character[] valores = {};

        final JOptional<Character> primeiroValor = JStreams.de(valores).primeiroValor();

        Assertions
                .assertTrue(primeiroValor.vazio());

    }

    @Test
    void vazio() {

        final String[] valores = {};

        Assertions
                .assertTrue(
                        JStreams
                                .de(valores)
                                .vazio()
                );

    }

    @Test
    void mapeandoNulo() {

        Assertions
                .assertTrue(
                        JStreams
                                .nula()
                                .mapeamento(String::valueOf).vazio());
    }

    @Test
    void mapeandoComFuncaoNula() {
        Assertions
                .assertThrows(NullPointerException.class, () -> JStreams.nula().mapeamento(null));
    }

    @Test
    void mapeandoComValorNulo() {
        Assertions.assertEquals(0, JStreams.de("the smiths").mapeamento(s -> null).quantidade());
    }

    @Test
    void segundoMapeamentoComFuncaoNula() {

        Assertions
                .assertThrows(NullPointerException.class,
                        () -> JStreams.de(1, 2).mapeamento(Integer::doubleValue).mapeamento(null));
    }

    @Test
    void mapeandoVazio() {

        final Double[] valores = {};

        Assertions
                .assertTrue(
                        JStreams
                                .de(valores)
                                .mapeamento(String::valueOf).vazio());

    }

    @Test
    void mapeandoValor() {

        final Class<? extends Integer> classe =
                JStreams
                        .de('A')
                        .mapeamento(Integer::valueOf)
                        .primeiroValor()
                        .obter()
                        .getClass();

        Assertions.assertEquals(Integer.class, classe);

    }

    @Test
    void filtroStreamNula() {

        Assertions
                .assertTrue(
                        JStreams
                                .nula()
                                .filtro(Objects::nonNull).vazio());

    }

    @Test
    void filtroComPredicadoNulo() {
        Assertions
                .assertThrows(NullPointerException.class, () -> JStreams.nula().filtro(null));
    }

    @Test
    void segundoFiltroComPredicadoNulo() {

        Assertions
                .assertThrows(NullPointerException.class,
                        () -> JStreams.de(1, 2).filtro(integer -> integer > 1).filtro(null));

    }

    @Test
    void filtroVazio() {

        final Double[] valores = {};

        Assertions
                .assertTrue(
                        JStreams
                                .de(valores)
                                .filtro(Objects::nonNull).vazio());

    }

    @Test
    void filtrandoSemSucesso() {

        final Double[] valores = {3.4, 1.23};

        Assertions
                .assertTrue(
                        JStreams
                                .de(valores)
                                .filtro(numero -> numero > 5).vazio());

    }

    @Test
    void filtrandoComSucesso() {

        final Double[] valores = {3.4, 1.23};

        Assertions
                .assertTrue(
                        JStreams
                                .de(valores)
                                .filtro(numero -> numero > 2).presente());

    }

    @Test
    void paraColecaoSet() {

        final Integer[] valores = {0, 0, 1, 1, 2, 2, 3, 4, 5};

        final String valor =
                JStreams
                        .de(valores)
                        .filtro(numero -> numero > 1)
                        .mapeamento(String::valueOf)
                        .paraColecao(Collectors.joining());

        Assertions.assertEquals("22345", valor);

    }

    @Test
    void streamNulaParaColecaoSet() {

        final Set<String> valoresString =
                JStreams
                        .de(0, 0, 1, 1, 2, 2)
                        .filtro(numero -> numero > 10)
                        .mapeamento(String::valueOf)
                        .paraColecao(Collectors.toSet());

        Assertions.assertTrue(valoresString.isEmpty());

    }

    @Test
    void todosCombinam() {


        Assertions
                .assertTrue(
                        JStreams
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > -1));

    }

    @Test
    void todosCombinamComPredicadoNulo() {
        Assertions
                .assertThrows(NullPointerException.class, () -> JStreams.nula().todosCombinam(null));
    }

    @Test
    void combinacaoDeTodosParaStreamNula() {

        Assertions
                .assertFalse(
                        JStreams
                                .nula()
                                .todosCombinam(o -> true));

    }

    @Test
    void combinacaoPeloMenosUmParaStreamNula() {

        Assertions
                .assertFalse(
                        JStreams
                                .nula()
                                .peloMenosUmCombina(o -> true));

    }

    @Test
    void peloMenosUmCombinaComPredicadoNulo() {
        Assertions
                .assertThrows(NullPointerException.class, () -> JStreams.nula().peloMenosUmCombina(null));
    }

    @Test
    void todosNaoCombinam() {

        Assertions
                .assertFalse(
                        JStreams
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > 0));

    }

    @Test
    void nenhumCombinaEntreTodos() {


        Assertions
                .assertFalse(
                        JStreams
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > 2));

    }

    @Test
    void peloMenosUmCombina() {

        Assertions
                .assertTrue(
                        JStreams
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 2));

    }

    @Test
    void quatroCombinam() {

        Assertions
                .assertTrue(
                        JStreams
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 0));

    }

    @Test
    void nenhumCombinaPeloMenosEntreUm() {

        Assertions
                .assertFalse(
                        JStreams
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 3));

    }

    @Test
    void somandoValores() {

        Assertions.assertEquals(55, JStreams.de(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reducao(Integer::sum).ou(0));

    }

    @Test
    void reducaoComOperacaoNula() {
        Assertions.assertThrows(NullPointerException.class,
                () -> JStreams.de(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reducao(null));
    }

    @Test
    void concatenandoString() {

        final String reducao =
                JStreams
                        .de("a", "b", "c", "d", "e", "f")
                        .reducao((a, b) -> a + b)
                        .ou("");

        Assertions
                .assertEquals("abcdef", reducao);

    }

    @Test
    void reducaoComStreamNula() {

        Assertions.assertTrue(JStreams.nula().reducao((a, b) -> null).vazio());

    }

    @Test
    void colecaoInteiroParaStream() {

        final List<Integer> inteiros = List.of(1, 2);

        final Class<? extends Integer> inteiroClass =
                JStreams
                        .de(inteiros)
                        .filtro(integer -> integer > 1)
                        .primeiroValor()
                        .ouExcessao(RuntimeException::new)
                        .obter()
                        .getClass();

        Assertions.assertEquals(Integer.class, inteiroClass);

    }

    @Test
    void excecaoPorStreamVazia() {

        final List<Integer> inteiros = List.of(1, 2);

        //noinspection ResultOfMethodCallIgnored
        Assertions
                .assertThrows(
                        NoSuchElementException.class,
                        JStreams
                                .de(inteiros)
                                .filtro(integer -> integer > 2)
                                .primeiroValor()::obter
                );

    }

    @Test
    void naoPresenteRealizaUmaAcao() {

        final AtomicBoolean status = new AtomicBoolean(false);

        JStreams
                .de(1.0, 2.0, 3.0, 4.0, 5.0, 5.5, 6.0)
                .filtro(aDouble -> aDouble > 3.0)
                .mapeamento(Double::intValue)
                .filtro(integer -> integer > 5)
                .mapeamento(Integer::doubleValue)
                .filtro(aDouble -> aDouble > 6.0)
                .mapeamento(String::valueOf)
                .primeiroValor()
                .seNaoPresente(() -> status.set(true))
                .sePresente(s -> status.set(false));

        Assertions
                .assertTrue(status.get());

    }

    @Test
    void presenteRealizaUmaAcao() {

        final AtomicBoolean status = new AtomicBoolean(false);

        final List<Double> numeros = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 5.5, 6.0);

        JStreams
                .de(numeros)
                .filtro(aDouble -> aDouble > 3.0)
                .mapeamento(Double::intValue)
                .filtro(integer -> integer > 5)
                .reducao(Integer::sum)
                .seNaoPresente(() -> status.set(false))
                .sePresente(s -> status.set(s.equals(6)));

        Assertions
                .assertTrue(status.get());

    }

    @Test
    void quantidadeIgual5() {
        Assertions
                .assertEquals(5, JStreams.de(1, 2, 3, 4, 5).quantidade());
    }

    @Test
    void paraCadaFoiChamado2Vezes() {

        final AtomicInteger quantidade = new AtomicInteger(0);

        JStreams
                .de(1, 2, 3)
                .filtro(integer -> integer > 1)
                .paraCada(quantidade::addAndGet);

        Assertions
                .assertEquals(5, quantidade.get());

    }

    @Test
    void paraCadaComConsumidorNulo() {

        Assertions
                .assertThrows(NullPointerException.class,
                        () -> JStreams.de(1).paraCada(null));
    }
}
