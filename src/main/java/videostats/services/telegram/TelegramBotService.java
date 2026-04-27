package videostats.services.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramBotService {
    private TelegramClient telegramClient;

    public TelegramBotService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void SendMessage(long chatId, String message) {
        var sendMessage = new SendMessage(String.valueOf(chatId), message);

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
