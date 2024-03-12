package io.proj3ct.SpringDemoBot.service;

import com.sun.tools.javac.Main;
import com.vdurmont.emoji.EmojiParser;
import io.proj3ct.SpringDemoBot.config.BotConfig;
import io.proj3ct.SpringDemoBot.model.User;
import io.proj3ct.SpringDemoBot.model.UserRepos;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    @Autowired
    private UserRepos userRepos;

    public TelegramBot  (BotConfig config){
        this.config=config;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String message= update.getMessage().getText();
            String user_name=update.getMessage().getChat().getFirstName();
            long chatId=update.getMessage().getChatId();
            MainMenu(chatId);

            switch (message){
                case "/start":
                    registerUser(update.getMessage());
                    startCommand(chatId,user_name);
                    break;

                case "/help":
                    sendMessege(chatId,"Этот бот о Плане развития IT сферы и обучение "+EmojiParser.parseToUnicode(":desktop_computer:\n\n")+
                    "Команды\n"+
                    "/start начать  разговор с ботом\n"+
                    "/getRoadMap Консультация о Саморазвити в IT сфере\n"+
                    "/help краткая инструкция о командах\n" +
                            "/Get_Consultation Личная Консультатция от Сманова Нуржана CEO Google :)");
                    break;


                case "/getRoadMap":
                    GetRoadMap(chatId);
                    break;

                case "/Get_Consultation":
                    sendMessege(chatId,"Напишите @Nurzhanss для подробной консультации");
                    break;

                case "/Get Consultation":
                    sendMessege(chatId,"Напишите @Nurzhanss для подробной консультации");
                    break;


                default: sendMessege(chatId,"Введи существующую команду братишка");

            }

        } else if (update.hasCallbackQuery()) {
            String callBackData=update.getCallbackQuery().getData();
            long chatId=update.getCallbackQuery().getMessage().getChatId();
            if(callBackData.equals("frontButton")){
                sendMessege(chatId,"Базовые Знание в области Frontend\n" +
                        "HTML5,CSS,JS\n" +
                        "Git-для контроля (front and backend)\n" +
                        "Sql-Structured Query Language (front and backend)\n" +
                        "Архитектура ПО (в целом для понимания)\n" +
                        "Архитектура Приложении (для понимания)\n" +
                        "Frameworks:ReactJS,Angukar,VueJs,JQuery,Bootstrap:)\n" +
                        "Подробнее можете узнать на:https://roadmap.sh/frontend");
            } else if (callBackData.equals("backendButton")) {
                sendMessege(chatId,"Базовые Знание в области Backend\n" +
                        "Java,Js,Python,C#,Golang,PHP,Ruby,Rust\n" +
                        "Git-для контроля (front and backend)\n" +
                        "Sql-Structured Query Language (front and backend)\n" +
                        "Архитектура ПО (в целом для понимания)\n" +
                        "Архитектура Приложении (для понимания)\n" +
                        "Frameworks:Spring,Boost,Poco\n" +
                        "Подробнее можете узнать на:https://roadmap.sh/backend");
            }
        }
    }

    public void registerUser(Message message){
        if(userRepos.findById(message.getChatId()).isEmpty()){
                User user=new User();
                user.setChatId(message.getChatId());
                user.setFirst_name(message.getChat().getFirstName());
                user.setLast_name(message.getChat().getLastName());
                user.setUser_name(message.getChat().getUserName());
                user.setRegisteredTimeofUser(new TimeStamp());
                userRepos.save(user);
                sendMessege(user.getChatId(),"Вы зарегистрированы как "+user.getFirst_name());
                log.info("Adding User"+user.getFirst_name()+" "+user.getChatId());
        }
        }
    private void startCommand(Long chatId,String name){
        sendMessege(chatId, EmojiParser.parseToUnicode("Здравствуйте "+name+" :hugs:"));
    }

    private void sendMessege(Long chatId,String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessege(message);
    }

    private void MainMenu(Long chatId){
        SendMessage message=new SendMessage();
        ReplyKeyboardMarkup KeyboardMarkup=new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows= new ArrayList<>();
        KeyboardRow row=new KeyboardRow();
        row.add("/start");
        row.add("/getRoadMap");
        row.add("/help");
        keyboardRows.add(row);
        row =new KeyboardRow();

        row.add("/Get_Consultation");
        row.add("/settings");
        keyboardRows.add(row);

        KeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(KeyboardMarkup);

    }

    private void GetRoadMap(long chatId){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите, какую отрасаль хотите изучить?\n" +
                "Данный Раздел временно не работает\n" +
                "Обратитесь /Get_Consultation");
        InlineKeyboardMarkup inlineMarkup =new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsinline=new ArrayList<>();
        List<InlineKeyboardButton> rowinline=new ArrayList<>();

        var FrontButton=new InlineKeyboardButton();
        FrontButton.setText("Frontend Dev");
        FrontButton.setCallbackData("frontButton");
        rowinline.add(FrontButton);

        var BackendButton=new InlineKeyboardButton();
        BackendButton.setText("Backend Dev");
        BackendButton.setCallbackData("backendButton");
        rowinline.add(BackendButton);

        rowsinline.add(rowinline);
        inlineMarkup.setKeyboard(rowsinline);
        message.setReplyMarkup(inlineMarkup);
        executeMessege(message);
    }

    private void executeMessege(SendMessage message){
        try{
            execute(message);
        }catch (TelegramApiException e){
            log.error("ERROR send message"+e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
