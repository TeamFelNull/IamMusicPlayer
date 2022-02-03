package dev.felnull.imp.client.music.subtitle;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import org.jetbrains.annotations.NotNull;

public enum YoutubeSubtitleEnum implements SelectionListEntry.Translatable {
    AUTO("Auto", ""),
    AFRIKAANS("Afrikaans", "af"),
    ALBANIAN("Albanian", "sq"),
    AMHARIC("Amharic", "am"),
    ARABIC("Arabic", "ar"),
    ARMENIAN("Armenian", "hy"),
    AZERBAIJANI("Azerbaijani", "az"),
    BASQUE("Basque", "eu"),
    BELARUSIAN("Belarusian", "be"),
    BENGALI("Bengali", "bn"),
    BOSNIAN("Bosnian", "bs"),
    BULGARIAN("Bulgarian", "bg"),
    CATALAN("Catalan", "ca"),
    CEBUANO("Cebuano", " ceb"),
    CHINESE_SIMPLIFIED("Chinese (Simplified)", "zh-CN"),
    CHINESE_TRADITIONAL("Chinese (Traditional)", "zh-TW"),
    CORSICAN("Corsican", "co"),
    CROATIAN("Croatian", "hr"),
    CZECH("Czech", "cs"),
    DANISH("Danish", "da"),
    DUTCH("Dutch", "nl"),
    ENGLISH("English", "en"),
    ESPERANTO("Esperanto", "eo"),
    ESTONIAN("Estonian", "et"),
    FINNISH("Finnish", "fi"),
    FRENCH("French", "fr"),
    FRISIAN("Frisian", "fy"),
    GALICIAN("Galician", "gl"),
    GEORGIAN("Georgian", "ka"),
    GERMAN("German", "de"),
    GREEK("Greek", "el"),
    GUJARATI("Gujarati", "gu"),
    HAITIAN_CREOLE("Haitian Creole", "ht"),
    HAUSA("Hausa", "ha"),
    HAWAIIAN("Hawaiian", "haw"),
    HEBREW("Hebrew", "he"),
    HINDI("Hindi", "hi"),
    HMONG("Hmong", "hmn"),
    HUNGARIAN("Hungarian", "hu"),
    ICELANDIC("Icelandic", "is"),
    IGBO("Igbo", "ig"),
    INDONESIAN("Indonesian", "id"),
    IRISH("Irish", "ga"),
    ITALIAN("Italian", "it"),
    JAPANESE("Japanese", "ja"),
    JAVANESE("Javanese", "jv"),
    KANNADA("Kannada", "kn"),
    KAZAKH("Kazakh", "kk"),
    KHMER("Khmer", "km"),
    KINYARWANDA("Kinyarwanda", "rw"),
    KOREAN("Korean", "ko"),
    KURDISH("Kurdish", "ku"),
    KYRGYZ("Kyrgyz", "ky"),
    LAO("Lao", "lo"),
    LATVIAN("Latvian", "lv"),
    LITHUANIAN("Lithuanian", "lt"),
    LUXEMBOURGISH("Luxembourgish", "lb"),
    MACEDONIAN("Macedonian", "mk"),
    MALAGASY("Malagasy", "mg"),
    MALAY("Malay", "ms"),
    MALAYALAM("Malayalam", "ml"),
    MALTESE("Maltese", "mt"),
    MAORI("Maori", "mi"),
    MARATHI("Marathi", "mr"),
    MONGOLIAN("Mongolian", "mn"),
    MYANMAR_BURMESE("Myanmar (Burmese)", "my"),
    NEPALI("Nepali", "ne"),
    NORWEGIAN("Norwegian", "no"),
    NYANJA_CHICHEWA("Nyanja (Chichewa)", "ny"),
    ODIA_ORIYA("Odia (Oriya)", "or"),
    PASHTO("Pashto", "ps"),
    PERSIAN("Persian", "fa"),
    POLISH("Polish", "pl"),
    PORTUGUESE_PORTUGAL_BRAZIL("Portuguese (Portugal, Brazil)", "pt"),
    PUNJABI("Punjabi", "pa"),
    ROMANIAN("Romanian", "ro"),
    RUSSIAN("Russian", "ru"),
    SAMOAN("Samoan", "sm"),
    SCOTS_GAELIC("Scots Gaelic", "gd"),
    SERBIAN("Serbian", "sr"),
    Sesotho("Sesotho", "st"),
    SHONA("Shona", "sn"),
    SINDHI("Sindhi", "sd"),
    Sinhala_Sinhalese("Sinhala (Sinhalese)", "si"),
    SLOVAK("Slovak", "sk"),
    SLOVENIAN("Slovenian", "sl"),
    SOMALI("Somali", "so"),
    SPANISH("Spanish", "es"),
    SUNDANESE("Sundanese", "su"),
    SWAHILI("Swahili", "sw"),
    Swedish("Swedish", "sv"),
    TAGALOG_FILIPINO("Tagalog (Filipino)", "tl"),
    TAJIK("Tajik", "tg"),
    TAMIL("Tamil", "ta"),
    TATAR("Tatar", "tt"),
    TELUGU("Telugu", "te"),
    THAI("Thai", "th"),
    TURKISH("Turkish", "tr"),
    TURKMEN("Turkmen", "tk"),
    UKRAINIAN("Ukrainian", "uk"),
    URDU("Urdu", "ur"),
    UYGHUR("Uyghur", "ug"),
    UZBEK("Uzbek", "uz"),
    VIETNAMESE("Vietnamese", "vi"),
    WELSH("Welsh", "cy"),
    XHOSA("Xhosa", "xh"),
    YIDDISH("Yiddish", "yi"),
    YORUBA("Yoruba", "yo"),
    ZULU("Zulu", "zu");

    private final String name;
    private final String code;

    private YoutubeSubtitleEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public @NotNull String getKey() {
        return getName();
    }
}
