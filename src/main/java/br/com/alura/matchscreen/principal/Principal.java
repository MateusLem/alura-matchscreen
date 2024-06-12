package br.com.alura.matchscreen.principal;

import br.com.alura.matchscreen.model.DadosEpisodio;
import br.com.alura.matchscreen.model.DadosSerie;
import br.com.alura.matchscreen.model.DadosTemporada;
import br.com.alura.matchscreen.model.Episodio;
import br.com.alura.matchscreen.service.ConsumoAPI;
import br.com.alura.matchscreen.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private String API_KEY = "&apikey=";


    public void treinoStream() {
        List<String> nomes = Arrays.asList("teor", "Mateus", "Diogo", "Heitor", "Igor");

        nomes.stream()
                .sorted()
                .forEach(System.out::println);
        System.out.println();

        nomes.stream()
                .sorted()
                .limit(3)
                .forEach(System.out::println);
        System.out.println();

        nomes.stream()
                .sorted()
                .limit(3)
                .filter(n -> n.startsWith("I"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
        System.out.println();

        nomes.parallelStream()
                .map(n -> n.endsWith("r"))
                .forEach(System.out::println);
        System.out.println();

        nomes.parallelStream()
                .map(n -> n.replaceAll("r", "l"))
                .forEach(System.out::println);
    }

    public void exibeMenu() {
        System.out.print("Sua apikey: ");
        API_KEY += leitura.nextLine();
        System.out.println();
        
        System.out.print("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO +
                nomeSerie.replace(" ", "+").toLowerCase() +
                API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie + "\n");

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO +
                    nomeSerie.replace(" ", "+").toLowerCase() +
                    "&season=" + i +
                    API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
//        for (int i=0; i<dadosSerie.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j=0; j<episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<DadosEpisodio> episodiosDados = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
////                .collect(Collectors.toList()); // Mutável
//                .toList(); // Imutável

//        episodiosDados.add(new DadosEpisodio("Teste", 3, "4.5", "2020"));

//        List<DadosEpisodio> topFive = episodiosDados.stream()
//                .filter(e-> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .toList();

//        System.out.println("\nTop cinco EP:");
//        topFive.forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                ).toList();

        episodios.forEach(System.out::println);

//        System.out.print("Ano de Inicio: ");
//        var ano = Integer.parseInt(leitura.nextLine());
//        LocalDate dataBusca = LocalDate.of(ano, 1 , 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//        episodios.stream()
//                .filter(e -> e.getDataLancamento()!=null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> {
//                    System.out.printf("Titulo: %s\n" +
//                            "Temporada: %d\n" +
//                            "Episódio: %d\n" +
//                            "Avaliação: %.2f\n"+
//                            "Data de Lançamento: "+e.getDataLancamento().format(dtf)+"\n\n",
//                            e.getTitulo(),
//                            e.getTemporada(),
//                            e.getNumeroEpisodio(),
//                            e.getAvaliacao()
//                    );
//                });

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e->e.getAvaliacao()>0.0)
                .collect(
                        Collectors.groupingBy(
                                Episodio::getTemporada, Collectors.averagingDouble(
                                        Episodio::getAvaliacao
                                )
                        )
                );

        System.out.println(avaliacoesPorTemporada+"\n");

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e->e.getAvaliacao()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.printf("Média: %.2f\n"+
                "Maior nota: %.2f\n" +
                "Menor nota: %.2f\n",
                est.getAverage(), est.getMax(), est.getMin()
        );
    }
}
