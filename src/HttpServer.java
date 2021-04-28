import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class HttpServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started!");

            while (true) {
                // ожидаем подключения
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                System.out.println();

                // для подключившегося клиента открываем потоки
                // чтения и записи
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {

                    // ждем первой строки запроса
                    while (!input.ready());


                    System.out.println("Клиент присылает следущее:");
                    System.out.println("Первая строка инфы от клиента:");
                    String firstAsk = input.readLine();
                    System.out.println(firstAsk);
                    //System.out.println(input.readLine());

                    //ищем знак "/" и "HTTP" в строках приходящих от браузера, и вычисляех их позиции
                    int beginDirectoryName = firstAsk.indexOf("/");
                    int endDirectoryName = firstAsk.indexOf("HTTP")-1;

                    char[] askMassive = new char[endDirectoryName - beginDirectoryName];
                    firstAsk.getChars (beginDirectoryName, endDirectoryName, askMassive, 0); //метод, вырезает кусок строки из "ask" в "askMassive"

                    System.out.print("Запрашиваемая директория: ");
                    System.out.println(askMassive);
                    String a = new String(askMassive);
                    System.out.println();

                    Path wwwPath = Path.of("c:\\www\\readme.txt");
                    Files.writeString(wwwPath, a);



                    System.out.println("остальные строки от клиента:");
                    while (input.ready()) { // считываем и печатаем все что было отправлено клиентом
                        String secondAsk = input.readLine();
                        System.out.println(secondAsk);
                    }

                    // отправляем ответ
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    output.println("<p>Привеееемнопогнееееет всем!</p><table  border=0 align=center width= 120>\n" +
                            "\t\t\t<tr valign=middle>\n" +
                            "\t\t\t\t<td bgcolor=\"F4A460\" align=\"left\" valign=\"middle\">\n" +
                            "\t\t\t\t\t<p><a href=index.html>Главная</a>\n" +
                            "\t\t\t\t\t<p><a href=bunneys.html>Зайчики</a>\n" +
                            "\t\t\t\t\t<p><a href=spears.html>Спицы</a>\n" +
                            "\t\t\t\t\t<p><a href=\"games/games.html\">Игры</a>\n" +
                            "\t\t\t\t\t<p><a href=about_Nastya.html>О Настеньке.</a>\n" +
                            "\t\t\t\t\t\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t</tr>\n" +
                            "\t\t</table>");



                    String fromReadme = Files.readString(wwwPath);
                    System.out.println(fromReadme);


                    output.flush();

                    // по окончанию выполнения блока try-with-resources потоки,
                    // а вместе с ними и соединение будут закрыты
                    System.out.println("Client disconnected!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
