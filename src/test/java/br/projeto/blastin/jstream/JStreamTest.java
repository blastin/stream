package br.projeto.blastin.jstream;

import br.projeto.blastin.joptional.JOptional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class JStreamTest {

    @Test
    void objetoNulo() {

        Assertions
                .assertTrue(
                        JStream
                                .de((Object) null)
                                .vazio()
                );

    }

    @Test
    void streamNulo() {

        Assertions
                .assertTrue(
                        JStream
                                .nula()
                                .vazio()
                );

    }

    @Test
    void primeiroValorDeStreamNula() {
        Assertions
                .assertTrue(JStream.nula().primeiroValor().vazio());
    }

    @Test
    void primeiroValorDeStreamVazia() {

        final Character[] valores = {};

        final JOptional<Character> primeiroValor = JStream.de(valores).primeiroValor();

        Assertions
                .assertTrue(primeiroValor.vazio());

    }

    @Test
    void vazio() {

        final String[] valores = {};

        Assertions
                .assertTrue(
                        JStream
                                .de(valores)
                                .vazio()
                );

    }

    @Test
    void mapeandoNulo() {

        Assertions
                .assertTrue(
                        JStream
                                .nula()
                                .mapeamento(String::valueOf).vazio());
    }

    @Test
    void mapeandoVazio() {

        final Double[] valores = {};

        Assertions
                .assertTrue(
                        JStream
                                .de(valores)
                                .mapeamento(String::valueOf).vazio());

    }

    @Test
    void mapeandoValor() {

        final Class<? extends Integer> classe =
                JStream
                        .de('A')
                        .mapeamento(Integer::valueOf)
                        .primeiroValor()
                        .obter()
                        .getClass();

        Assertions.assertEquals(Integer.class, classe);

    }

    @Test
    void filtroNulo() {

        Assertions
                .assertTrue(
                        JStream
                                .nula()
                                .filtro(Objects::nonNull).vazio());

    }

    @Test
    void filtroVazio() {

        final Double[] valores = {};

        Assertions
                .assertTrue(
                        JStream
                                .de(valores)
                                .filtro(Objects::nonNull).vazio());

    }

    @Test
    void filtrandoSemSucesso() {

        final Double[] valores = {3.4, 1.23};

        Assertions
                .assertTrue(
                        JStream
                                .de(valores)
                                .filtro(numero -> numero > 5).vazio());

    }

    @Test
    void filtrandoComSucesso() {

        final Double[] valores = {3.4, 1.23};

        Assertions
                .assertTrue(
                        JStream
                                .de(valores)
                                .filtro(numero -> numero > 2).presente());

    }

    @Test
    void paraColecaoSet() {

        final Integer[] valores = {0, 0, 1, 1, 2, 2};

        final Set<String> valoresString =
                JStream
                        .de(valores)
                        .filtro(numero -> numero > 1)
                        .mapeamento(String::valueOf)
                        .paraColecao(Collectors.toSet());

        Assertions.assertEquals(1, valoresString.size());

    }

    @Test
    void streamNulaParaColecaoSet() {

        final Set<String> valoresString =
                JStream
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
                        JStream
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > -1));

    }

    @Test
    void combinacaoDeTodosParaStreamNula() {

        Assertions
                .assertFalse(
                        JStream
                                .nula()
                                .todosCombinam(o -> true));

    }

    @Test
    void combinacaoPeloMenosUmParaStreamNula() {

        Assertions
                .assertFalse(
                        JStream
                                .nula()
                                .peloMenosUmCombina(o -> true));

    }


    @Test
    void todosNaoCombinam() {

        Assertions
                .assertFalse(
                        JStream
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > 0));

    }

    @Test
    void nenhumCombinaEntreTodos() {


        Assertions
                .assertFalse(
                        JStream
                                .de(0, 0, 1, 1, 2, 2)
                                .todosCombinam(numero -> numero > 2));

    }


    @Test
    void peloMenosUmCombina() {

        Assertions
                .assertTrue(
                        JStream
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 2));

    }

    @Test
    void quatroCombinam() {

        Assertions
                .assertTrue(
                        JStream
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 0));

    }

    @Test
    void nenhumCombinaPeloMenosEntreUm() {

        Assertions
                .assertFalse(
                        JStream
                                .de(0, 0, 1, 1, 2, 3)
                                .peloMenosUmCombina(numero -> numero > 3));

    }

    @Test
    void somandoValores() {

        Assertions.assertEquals(55, JStream.de(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).reducao(Integer::sum).ou(0));

    }

    @Test
    void concatenandoString() {

        final String reducao =
                JStream
                        .de("a", "b", "c", "d", "e", "f")
                        .reducao((a, b) -> a + b)
                        .ou("");

        Assertions
                .assertEquals("abcdef", reducao);

    }

    @Test
    void reducaoComStreamNula() {

        Assertions.assertTrue(JStream.nula().reducao((a, b) -> null).vazio());

    }


    @Test
    void colecaoInteiroParaStream() {

        final List<Integer> inteiros = List.of(1, 2);

        final Class<? extends Integer> inteiroClass =
                JStream
                        .de(inteiros)
                        .primeiroValor()
                        .ouExcessao(RuntimeException::new)
                        .obter()
                        .getClass();

        Assertions.assertEquals(Integer.class, inteiroClass);

    }
}
