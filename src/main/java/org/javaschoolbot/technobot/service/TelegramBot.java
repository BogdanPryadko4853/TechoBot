package org.javaschoolbot.technobot.service;

import org.javaschoolbot.technobot.KeyBoard.KeyBoard;
import org.javaschoolbot.technobot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static String previousState;
    public String classes;

    private final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if ("/start".equals(messageText)) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        // Получаем клавиатуру
        KeyBoard keyboard = new KeyBoard();
        ReplyKeyboardMarkup keyboardMarkup = keyboard.createKeyboard();

        String answer = "Привет, " + name;

        // Отправляем сообщение с клавиатурой
        sendMessage(chatId, answer, keyboardMarkup);
    }

    private void sendMessage(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            // Обработка ошибки при отправке сообщения
            e.printStackTrace();
        }
    }
}
