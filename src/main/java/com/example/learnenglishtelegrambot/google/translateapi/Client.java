package com.example.learnenglishtelegrambot.google.translateapi;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Client {
    public String translate(String str) {


        Translate translate = TranslateOptions.newBuilder()
                .setApiKey("").build().getService();
        Translation translation = translate.translate(
                str,
                Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage("uk"));
        return translation.getTranslatedText();
    }
}
