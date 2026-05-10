package videostats;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.postgres_driver;

import videostats.application.TelegramBot;
import videostats.repository.DatabaseConnection;
import videostats.repository.VideoRepository;
import videostats.repository.VideoStatsRepository;
import videostats.services.parsers.ParserFactory;
import videostats.services.telegram.TelegramBotService;
import videostats.services.video.VideoService;
import videostats.services.youtube.YoutubeService;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


public class VideoStatsAggregator {
    public static void main(String[] args) {
        try {
            setOutEncoding();

            System.out.println("Старт конфигурирования приложения...");

            var postgresUrl = System.getenv("POSTGRES_URL");
            var postgresUser = System.getenv("POSTGRES_USER");
            var postgresPassword = System.getenv("POSTGRES_PASSWORD");
            var telegramBotToken = System.getenv("TELEGRAM_BOT_TOKEN");
            var youtubeApiKey = System.getenv("YOUTUBE_API_KEY");

            var telegramClient = new OkHttpTelegramClient(telegramBotToken);
            var telegramBotService = new TelegramBotService(telegramClient);

            var parserFactory = new ParserFactory();
            var sessionFactory = DatabaseConnection.getSessionFactory(postgresUrl, postgresUser, postgresPassword);

            var videoRepository = new VideoRepository(sessionFactory);
            var videoStatsRepository = new VideoStatsRepository(sessionFactory);
            var youtubeService = new YoutubeService(youtubeApiKey);
            var videoService = new VideoService(videoRepository, videoStatsRepository, youtubeService, parserFactory);

            var telegramBot = new TelegramBot(telegramBotService, videoService);

            System.out.println("Конфигурирование приложения успешно завершено.");

            System.out.println("Запуск телеграм-бота...");

            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(telegramBotToken, telegramBot);

            System.out.println("Телеграм-бот успешно запущен.");
        } catch (Exception e) {
            System.out.printf("Возникла непредвиденная ошибка. %s", e.getMessage());
        }
    }

    private static void setOutEncoding() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }
}
