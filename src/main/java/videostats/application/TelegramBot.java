package videostats.application;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import videostats.services.telegram.TelegramBotService;
import videostats.services.video.VideoService;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramBotService telegramBotService;
    private VideoService videoService;

    public TelegramBot(TelegramBotService telegramBotService, VideoService videoService) {
        this.telegramBotService = telegramBotService;
        this.videoService = videoService;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        System.out.println(update.getMessage().getText());

        var chatId = update.getMessage().getChatId();
        var message = update.getMessage().getText();
        var messageWords = message.split(" ");

        if (messageWords.length == 2 && messageWords[0].equals("/track")) {
            handlerTrackCommand(chatId, messageWords[1]);
        } else if (messageWords.length == 1 && messageWords[0].equals("/list")) {
            handlerListCommand(chatId);
        } else if (messageWords.length == 1 && messageWords[0].equals("/aggregation")) {
            handlerAggregationCommand(chatId);
        } else if (messageWords.length == 1 && messageWords[0].equals("/refresh")) {
            handlerRefreshCommand(chatId);
        } else {
            telegramBotService.SendMessage(chatId, "Не удалось распознать команду. Воспользуйтесь меню для подсказки.");
        }
    }

    private void handlerTrackCommand(Long chatId, String videoUrl) {
        try {
            videoService.TrackVideo(chatId, videoUrl);
        } catch (Exception e) {
            telegramBotService.SendMessage(chatId, e.getMessage());
        }
    }

    private void handlerListCommand(long chatId) {
        try {
            var statisticsForVideos = videoService.GetStatisticsForVideos(chatId);
            if (statisticsForVideos.isEmpty()) {
                telegramBotService.SendMessage(chatId, "Не найдено отслеживаемых видео. Для отслеживания видео воспользуйтесь командой '/track', подробнее об этой команде в меню.");
                return;
            }

            var messageHeader = String.format("Подготовлена статистика по %d видео:\n", statisticsForVideos.size());
            var messageBody = String.join(
                    "\n",
                    statisticsForVideos
                            .stream()
                            .map(x -> String.format(
                                    "Платформа: %s\t URL: %s\t Количество просмотров: %d",
                                    x.getPlatform(),
                                    x.getUrl(),
                                    x.getViewCount()))
                            .toList());

            telegramBotService.SendMessage(chatId, messageHeader + messageBody);
        } catch (Exception e) {
            telegramBotService.SendMessage(chatId, e.getMessage());
        }
    }

    private void handlerAggregationCommand(long chatId) {
        try {
            var aggregatedVideoStatisticsInfo = videoService.GetAggregatedVideoStatisticsInfo(chatId);

            var messageHeader = "Сводная статистика по отслеживаемым видео:\n";
            var messageBody = String.format(
                    "Всего отслеживается видео: %d\n" +
                            "Всего просмотров на отслеживаемых видео: %d",
                    aggregatedVideoStatisticsInfo.getVideosCount(),
                    aggregatedVideoStatisticsInfo.getVideosViewsCount());

            telegramBotService.SendMessage(chatId, messageHeader + messageBody);
        } catch (Exception e) {
            telegramBotService.SendMessage(chatId, e.getMessage());
        }
    }

    private void handlerRefreshCommand(long chatId) {
        try {
            videoService.GetStatisticsForVideos(chatId);

            telegramBotService.SendMessage(chatId, "Статистика была обновлена.");
        } catch (Exception e) {
            telegramBotService.SendMessage(chatId, e.getMessage());
        }
    }
}
