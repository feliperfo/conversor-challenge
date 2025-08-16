import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConversorDeMoedas {

    private static final String API_KEY = System.getenv("CONVERSOR_API_KEY");;
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

    private static final Map<Integer, String> MENU_OPTIONS = new LinkedHashMap<>();
    static {
        MENU_OPTIONS.put(1, "USD");
        MENU_OPTIONS.put(2, "BRL");
        MENU_OPTIONS.put(3, "ARS");
        MENU_OPTIONS.put(4, "BOB");
        MENU_OPTIONS.put(5, "CLP");
        MENU_OPTIONS.put(6, "COP");
        MENU_OPTIONS.put(7, "EUR");
        MENU_OPTIONS.put(8, "GBP");
        MENU_OPTIONS.put(9, "JPY");
        MENU_OPTIONS.put(10, "CAD");
        MENU_OPTIONS.put(11, "MXN");
        MENU_OPTIONS.put(12, "CNY");
    }

    private static final Map<String, Double> conversionRates = new HashMap<>();
    private static final List<String> conversionHistory = new ArrayList<>();
    private static final String HISTORY_FILENAME = "historico_conversoes.txt";

    public static void main(String[] args) {
        loadHistoryFromFile();

        if (!fetchConversionRates()) {
            System.out.println("Não foi possível carregar as taxas de câmbio. Verifique sua chave da API.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== CONVERSOR DE MOEDAS ===");

        mainLoop:
        while (true) {
            printMenu();

            System.out.print("Escolha: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "S":
                    break mainLoop;

                case "H":
                    showHistory();
                    break;

                case "C":
                    performConversion(scanner);
                    break;

                default:
                    System.out.println("Opção inválida. Digite 'H' para histórico, 'S' para sair, ou 'C' para converter moedas.");
            }
        }

        System.out.println("\nPrograma encerrado. Aqui está seu histórico final:");
        showHistory();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nMoedas disponíveis:");
        for (Map.Entry<Integer, String> entry : MENU_OPTIONS.entrySet()) {
            System.out.printf("%2d - %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println("\nPara ver o histórico de conversões, digite 'H'");
        System.out.println("Para sair do programa, digite 'S'");
        System.out.println("Para converter moedas, digite 'C'");
    }

    private static void performConversion(Scanner scanner) {
        try {
            System.out.print("Digite o número da moeda de origem: ");
            int fromOption = Integer.parseInt(scanner.nextLine());
            if (!MENU_OPTIONS.containsKey(fromOption)) {
                System.out.println("Moeda de origem inválida. Tente novamente.");
                return;
            }

            System.out.print("Digite o número da moeda de destino: ");
            int toOption = Integer.parseInt(scanner.nextLine());
            if (!MENU_OPTIONS.containsKey(toOption)) {
                System.out.println("Moeda de destino inválida. Tente novamente.");
                return;
            }

            System.out.print("Digite o valor a ser convertido: ");
            double amount = Double.parseDouble(scanner.nextLine());

            String fromCurrency = MENU_OPTIONS.get(fromOption);
            String toCurrency = MENU_OPTIONS.get(toOption);

            double converted = convertCurrency(fromCurrency, toCurrency, amount);

            String log = generateLog(fromCurrency, toCurrency, amount, converted);
            conversionHistory.add(log);
            saveHistoryToFile();

            System.out.println(log);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número válido.");
        }
    }

    private static boolean fetchConversionRates() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Erro HTTP: código " + response.statusCode());
                return false;
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!json.get("result").getAsString().equals("success")) {
                System.out.println("Resposta da API não foi sucesso.");
                return false;
            }

            JsonObject rates = json.getAsJsonObject("conversion_rates");

            for (String code : MENU_OPTIONS.values()) {
                if (rates.has(code)) {
                    JsonElement rateElement = rates.get(code);
                    conversionRates.put(code, rateElement.getAsDouble());
                }
            }

            return true;

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro na requisição: " + e.getMessage());
            return false;
        }
    }

    private static double convertCurrency(String from, String to, double amount) {
        Double rateFrom = conversionRates.get(from);
        Double rateTo = conversionRates.get(to);
        if (rateFrom == null || rateTo == null) {
            System.out.println("Taxa de câmbio não disponível para as moedas selecionadas.");
            return 0.0;
        }
        double usdAmount = amount / rateFrom;
        return usdAmount * rateTo;
    }

    private static String generateLog(String from, String to, double amount, double converted) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        return String.format("[%s] %s %s → %s %s",
                timestamp,
                nf.format(amount),
                from,
                nf.format(converted),
                to);
    }

    private static void showHistory() {
        if (conversionHistory.isEmpty()) {
            System.out.println("Nenhuma conversão registrada ainda.");
        } else {
            System.out.println("\n=== Histórico de Conversões ===");
            for (String record : conversionHistory) {
                System.out.println(record);
            }
        }
    }

    private static void saveHistoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILENAME))) {
            for (String record : conversionHistory) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    private static void loadHistoryFromFile() {
        File file = new File(HISTORY_FILENAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                conversionHistory.add(fileScanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar histórico: " + e.getMessage());
        }
    }
}
