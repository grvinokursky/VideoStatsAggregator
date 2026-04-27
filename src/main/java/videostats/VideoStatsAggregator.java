package videostats;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import videostats.application.TelegramBot;
import videostats.model.Video;
import videostats.repository.DatabaseConnection;
import videostats.repository.VideoRepository;
import videostats.repository.VideoStatsRepository;
import videostats.services.telegram.TelegramBotService;
import videostats.services.video.VideoService;
import videostats.services.youtube.YoutubeService;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class VideoStatsAggregator {
    public static void main(String[] args) {
        try {
            setOutEncoding();

            System.out.println("Старт конфигурирования приложения...");

            var postgresUrl = System.getenv("POSTGRES_URL");
            var postgresUser = System.getenv("POSTGRES_USER");
            var postgresPassword = System.getenv("POSTGRES_PASSWORD");
            var telegramBotToken = System.getenv("TELEGRAM_BOT_TOKEN");

            var databaseConnection = DatabaseConnection.getInstance(postgresUrl, postgresUser, postgresPassword);

            var telegramClient = new OkHttpTelegramClient(telegramBotToken);
            var telegramBotService = new TelegramBotService(telegramClient);

            var videoRepository = new VideoRepository(databaseConnection);
            var videoStatsRepository = new VideoStatsRepository(databaseConnection);
            var youtubeService = new YoutubeService();
            var videoService = new VideoService(videoRepository, videoStatsRepository, youtubeService);

            var telegramBot = new TelegramBot(telegramBotService, videoService);

            System.out.println("Конфигурирование приложения успешно завершено.");

            System.out.println("Запуск телеграм-бота...");

            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(telegramBotToken, telegramBot);

            System.out.println("Телеграм-бот успешно запущен.");
        } catch (Exception e) {
            System.out.printf("Возникла непредвиденная ошибка. %s", e.getMessage());
        }

//        try {
//            createTable();
//            Video video = new Video("https://youtube.com/akula", "akula", "youtube.com", 1);
//            repository.save(video);
//            video = new Video("https://vkvideo.ru/show", "show", "vkvideo.ru", 2);
//            repository.save(video);
//            getAllVideos(repository);
//        } catch (SQLException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
    }

    private static void setOutEncoding() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    private static void createTable(DatabaseConnection databaseConnection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS videos (
                id SERIAL PRIMARY KEY,
                url VARCHAR(100) NOT NULL UNIQUE,
                video_id VARCHAR(100) NOT NULL,
                platform VARCHAR(100) NOT NULL,
                user_id BIGINT NOT NULL
            )
        """;
        Connection conn = databaseConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
        finally {
            databaseConnection.releaseConnection(conn);
        }
    }
    
    private static void getAllVideos(VideoRepository repository) throws SQLException {
        List<Video> videos = repository.findAll();
        System.out.printf("%-3s | %-30s | %-20s | %-7s%n", "Url", "Video ID", "Platform", "User ID");            
        for (Video video : videos) {
            System.out.printf("%-30s | %-30s | %-20s | %-7s%n", 
                video.getUrl(), 
                video.getVideoID(), 
                video.getPlatform(), 
                video.getUserID()
            );
        }
    }
}
