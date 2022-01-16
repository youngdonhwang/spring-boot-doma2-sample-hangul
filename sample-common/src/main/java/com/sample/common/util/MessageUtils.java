package com.sample.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageUtils {

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 메시지를 취득한다.
     * 
     * @param key
     * @param args
     * @return
     */
    public static String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return MessageUtils.messageSource.getMessage(key, args, locale);
    }

    /**
     * 로케일을 지정하여 메시지를 취득한다.
     *
     * @param key
     * @param locale
     * @param args
     * @return
     */
    public static String getMessage(String key, Locale locale, Object... args) {
        return MessageUtils.messageSource.getMessage(key, args, locale);
    }

    /**
     * 메시지를 취득한다.
     * 
     * @param resolvable
     * @return
     */
    public static String getMessage(MessageSourceResolvable resolvable) {
        Locale locale = LocaleContextHolder.getLocale();
        return MessageUtils.messageSource.getMessage(resolvable, locale);
    }
}
