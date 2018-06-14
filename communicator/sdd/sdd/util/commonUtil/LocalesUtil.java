package com.kuangchi.sdd.util.commonUtil;

import java.util.Locale;

import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class LocalesUtil
{
  public static final String CHINESE_LANGUAGE_CODE = Locale.CHINESE.getLanguage();
  public static final String ENGLISH_LANGUAGE_CODE = Locale.ENGLISH.getLanguage();
  public static final String LOCALES_SESSION_KEY = "locales_sesson_key";
  private static ThreadLocal<Locale> localeHolder = new ThreadLocal();
  private static String localesCode;

  public static Locale getLocales()
  {
    return ((localeHolder.get() != null) ? (Locale)localeHolder.get() : Locale.getDefault());
  }

  public static String getLocalesCode()
  {
    return localesCode;
  }

  public static String getChineseLanguage()
  {
    return CHINESE_LANGUAGE_CODE;
  }

  public static String getEnglishLanguage()
  {
    return ENGLISH_LANGUAGE_CODE;
  }

  public static String getDefaultLanguage()
  {
    return Locale.getDefault().getLanguage();
  }

  public static String getI18nResourceFile(Locale locale)
  {
    return getI18nResourceFile(locale.getLanguage());
  }

  public static String getI18nResourceFile(String localeCode)
  {
    return "conf.properties.resources_" + localeCode;
  }

  public static void setLocales(Locale locale)
  {
    localeHolder.set(locale);
    setLocalesCode(locale.getLanguage());
  }

  public static void setLocalesCode(String localeCode)
  {
    if (localeCode == null)
      LocalizedTextUtil.addDefaultResourceBundle(getI18nResourceFile(getDefaultLanguage()));
    LocalizedTextUtil.addDefaultResourceBundle(getI18nResourceFile(localeCode));
    localesCode = localeCode;
  }
}