package br.com.alura.matchscreen.principal;

import br.com.alura.matchscreen.model.DadosSerie;
import br.com.alura.matchscreen.model.DadosTemporada;
import br.com.alura.matchscreen.service.ConsumoAPI;
import br.com.alura.matchscreen.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=a24a2dc4";

    public void exibeMenu() {
        System.out.print("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO +
                nomeSerie.replace(" ", "+").toLowerCase() +
                API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO +
                    nomeSerie.replace(" ", "+").toLowerCase() +
                    "&season=" + i +
                    API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
}
