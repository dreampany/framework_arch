package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.http.HttpManager;
import com.dreampany.framework.data.model.Language;
import com.dreampany.framework.data.model.Translate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by air on 11/1/17.
 */

public class Yandex {

    private static final String TRANSLATE_KEY = "trnsl.1.1.20171125T130252Z.8ff1d3ed787f661f.ecf250fc1de53066961e1e8103e3570df5c69020";
    private static final String DICTIONARY_KEY = "dict.1.1.20161031T020600Z.9747bcb853f6fd75.e7453ea7d4a2ef9892627ea87465f0c460c71140";

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_TRANSLATE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    private static final String TRANSLATE_GET_HOST = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final String DICTIONARY_HOST = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?";

    private static final String TRANSLATE_POST_HOST = "translate.yandex.net";

    private static final int CODE_SUCCESS = 200;

    private static final String KEY = "key";
    private static final String KEY_LANG = "lang";
    private static final String KEY_TEXT = "text";
    private static final String KEY_CODE = "code";

    private HttpManager http;
    private JsonParser parser;
    private BiMap<String, String> languages;

    public Yandex() {
        http = new HttpManager();
        parser = new JsonParser();
        languages = HashBiMap.create();
        loadLanguages();
    }

    public Translate getTranslate(String source, String target, String sourceText) {

        Map<String, String> params = Maps.newHashMap();
        params.put(KEY, TRANSLATE_KEY);
        params.put(KEY_LANG, source + "-" + target);
        params.put(KEY_TEXT, sourceText);

        String result = http.post(TRANSLATE_GET_HOST, params);

        try {
            JsonObject json = parser.parse(result).getAsJsonObject();

            if (!json.has(KEY_CODE)) {
                return null;
            }

            int code = json.get(KEY_CODE).getAsInt();

            if (code != CODE_SUCCESS) {
                return null;
            }

            JsonArray array = json.getAsJsonArray(KEY_TEXT);

            if (array == null || array.size() <= 0) {
                return null;
            }

            for (JsonElement element : array) {
                return new Translate(source, target, sourceText, element.getAsString());
            }
        } catch (NullPointerException | JsonParseException ignored) {

        }
        return null;
    }

/*    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }*/

    public List<String> getLanguageCodes() {
        return new ArrayList<>(languages.keySet());
    }

    public List<String> getLanguageNames() {
        return new ArrayList<>(languages.values());
    }

    public String getLanguageCode(String name) {
        return languages.inverse().get(name);
    }

    public String getLanguageName(String code) {
        return languages.get(code);
    }

    private void loadLanguages() {
        languages.put(AZ.getCode(), AZ.getName());
        languages.put(SQ.getCode(), SQ.getName());
        languages.put(AM.getCode(), AM.getName());
        languages.put(EN.getCode(), EN.getName());
        languages.put(AR.getCode(), AR.getName());
        languages.put(HY.getCode(), HY.getName());
        languages.put(AF.getCode(), AF.getName());
        languages.put(EU.getCode(), EU.getName());
        languages.put(BA.getCode(), BA.getName());
        languages.put(BE.getCode(), BE.getName());

        languages.put(BN.getCode(), BN.getName());
        languages.put(MY.getCode(), MY.getName());
        languages.put(BG.getCode(), BG.getName());
        languages.put(BS.getCode(), BS.getName());
        languages.put(CY.getCode(), CY.getName());
        languages.put(HU.getCode(), HU.getName());
        languages.put(VI.getCode(), VI.getName());
        languages.put(HT.getCode(), HT.getName());
        languages.put(GL.getCode(), GL.getName());
        languages.put(NL.getCode(), NL.getName());

        languages.put(MRJ.getCode(), MRJ.getName());
        languages.put(EL.getCode(), EL.getName());
        languages.put(KA.getCode(), KA.getName());
        languages.put(GU.getCode(), GU.getName());
        languages.put(DA.getCode(), DA.getName());
        languages.put(HE.getCode(), HE.getName());
        languages.put(YI.getCode(), YI.getName());
        languages.put(ID.getCode(), ID.getName());
        languages.put(GA.getCode(), GA.getName());
        languages.put(IT.getCode(), IT.getName());

        languages.put(IS.getCode(), IS.getName());
        languages.put(ES.getCode(), ES.getName());
        languages.put(KK.getCode(), KK.getName());
        languages.put(KN.getCode(), KN.getName());
        languages.put(CA.getCode(), CA.getName());
        languages.put(KY.getCode(), KY.getName());
        languages.put(ZH.getCode(), ZH.getName());
        languages.put(KO.getCode(), KO.getName());
        languages.put(XH.getCode(), XH.getName());
        languages.put(KM.getCode(), KM.getName());

        languages.put(LO.getCode(), LO.getName());
        languages.put(LA.getCode(), LA.getName());
        languages.put(LV.getCode(), LV.getName());
        languages.put(LT.getCode(), LT.getName());
        languages.put(LB.getCode(), LB.getName());
        languages.put(MG.getCode(), MG.getName());
        languages.put(MS.getCode(), MS.getName());
        languages.put(ML.getCode(), ML.getName());
        languages.put(MT.getCode(), MT.getName());
        languages.put(MK.getCode(), MK.getName());

        languages.put(MI.getCode(), MI.getName());
        languages.put(MR.getCode(), MR.getName());
        languages.put(MHR.getCode(), MHR.getName());
        languages.put(MN.getCode(), MN.getName());
        languages.put(DE.getCode(), DE.getName());
        languages.put(NE.getCode(), NE.getName());
        languages.put(NO.getCode(), NO.getName());
        languages.put(PA.getCode(), PA.getName());
        languages.put(PAP.getCode(), PAP.getName());
        languages.put(FA.getCode(), FA.getName());

        languages.put(PL.getCode(), PL.getName());
        languages.put(PT.getCode(), PT.getName());
        languages.put(RO.getCode(), RO.getName());
        languages.put(RU.getCode(), RU.getName());
        languages.put(CEB.getCode(), CEB.getName());
        languages.put(SR.getCode(), SR.getName());
        languages.put(SI.getCode(), SI.getName());
        languages.put(SK.getCode(), SK.getName());
        languages.put(SL.getCode(), SL.getName());
        languages.put(SW.getCode(), SW.getName());

        languages.put(SU.getCode(), SU.getName());
        languages.put(TG.getCode(), TG.getName());
        languages.put(TH.getCode(), TH.getName());
        languages.put(TL.getCode(), TL.getName());
        languages.put(TA.getCode(), TA.getName());
        languages.put(TT.getCode(), TT.getName());
        languages.put(TE.getCode(), TE.getName());
        languages.put(TR.getCode(), TR.getName());
        languages.put(UDM.getCode(), UDM.getName());
        languages.put(UZ.getCode(), UZ.getName());

        languages.put(UK.getCode(), UK.getName());
        languages.put(UR.getCode(), UR.getName());
        languages.put(FI.getCode(), FI.getName());
        languages.put(FR.getCode(), FR.getName());
        languages.put(HI.getCode(), HI.getName());
        languages.put(HR.getCode(), HR.getName());
        languages.put(CS.getCode(), CS.getName());
        languages.put(SV.getCode(), SV.getName());
        languages.put(GD.getCode(), GD.getName());
        languages.put(ET.getCode(), ET.getName());

        languages.put(EO.getCode(), EO.getName());
        languages.put(JV.getCode(), JV.getName());
        languages.put(JA.getCode(), JA.getName());

    }


    public static final Language AZ = new Language("az", "Azerbaijan");
    public static final Language SQ = new Language("sq", "Albanian");
    public static final Language AM = new Language("am", "Amharic");
    public static final Language EN = new Language("en", "English");
    public static final Language AR = new Language("ar", "Arabic");
    public static final Language HY = new Language("hy", "Armenian");
    public static final Language AF = new Language("af", "Afrikaans");
    public static final Language EU = new Language("eu", "Basque");
    public static final Language BA = new Language("ba", "Bashkir");
    public static final Language BE = new Language("be", "Belarusian");
    public static final Language BN = new Language("bn", "Bengali");
    public static final Language MY = new Language("my", "Burmese");
    public static final Language BG = new Language("bg", "Bulgarian");
    public static final Language BS = new Language("bs", "Bosnian");
    public static final Language CY = new Language("cy", "Welsh");
    public static final Language HU = new Language("hu", "Hungarian");
    public static final Language VI = new Language("vi", "Vietnamese");
    public static final Language HT = new Language("ht", "Haitian (Creole)");
    public static final Language GL = new Language("gl", "Galician");
    public static final Language NL = new Language("nl", "Dutch");
    public static final Language MRJ = new Language("mrj", "Hill Mari");
    public static final Language EL = new Language("el", "Greek");
    public static final Language KA = new Language("ka", "Georgian");
    public static final Language GU = new Language("gu", "Gujarati");
    public static final Language DA = new Language("da", "Danish");
    public static final Language HE = new Language("he", "Hebrew");
    public static final Language YI = new Language("yi", "Yiddish");
    public static final Language ID = new Language("id", "Indonesian");
    public static final Language GA = new Language("ga", "Irish");
    public static final Language IT = new Language("it", "Italian");
    public static final Language IS = new Language("is", "Icelandic");
    public static final Language ES = new Language("es", "Spanish");
    public static final Language KK = new Language("kk", "Kazakh");
    public static final Language KN = new Language("kn", "Kannada");
    public static final Language CA = new Language("ca", "Catalan");
    public static final Language KY = new Language("ky", "Kyrgyz");
    public static final Language ZH = new Language("zh", "Chinese");
    public static final Language KO = new Language("ko", "Korean");
    public static final Language XH = new Language("xh", "Xhosa");
    public static final Language KM = new Language("km", "Khmer");
    public static final Language LO = new Language("lo", "Laotian");
    public static final Language LA = new Language("la", "Latin");
    public static final Language LV = new Language("lv", "Latvian");
    public static final Language LT = new Language("lt", "Lithuanian");
    public static final Language LB = new Language("lb", "Luxembourgish");
    public static final Language MG = new Language("mg", "Malagasy");
    public static final Language MS = new Language("ms", "Malay");
    public static final Language ML = new Language("ml", "Malayalam");
    public static final Language MT = new Language("mt", "Maltese");
    public static final Language MK = new Language("mk", "Macedonian");
    public static final Language MI = new Language("mi", "Maori");
    public static final Language MR = new Language("mr", "Marathi");
    public static final Language MHR = new Language("mhr", "Mari");
    public static final Language MN = new Language("mn", "Mongolian");
    public static final Language DE = new Language("de", "German");
    public static final Language NE = new Language("ne", "Nepali");
    public static final Language NO = new Language("no", "Norwegian");
    public static final Language PA = new Language("pa", "Punjabi");
    public static final Language PAP = new Language("pap", "Papiamento");
    public static final Language FA = new Language("fa", "Persian");
    public static final Language PL = new Language("pl", "Polish");
    public static final Language PT = new Language("pt", "Portuguese");
    public static final Language RO = new Language("ro", "Romanian");
    public static final Language RU = new Language("ru", "Russian");
    public static final Language CEB = new Language("ceb", "Cebuano");
    public static final Language SR = new Language("sr", "Serbian");
    public static final Language SI = new Language("si", "Sinhala");
    public static final Language SK = new Language("sk", "Slovakian");
    public static final Language SL = new Language("sl", "Slovenian");
    public static final Language SW = new Language("sw", "Swahili");
    public static final Language SU = new Language("su", "Sundanese");
    public static final Language TG = new Language("tg", "Tajik");
    public static final Language TH = new Language("th", "Thai");
    public static final Language TL = new Language("tl", "Tagalog");
    public static final Language TA = new Language("ta", "Tamil");
    public static final Language TT = new Language("tt", "Tatar");
    public static final Language TE = new Language("te", "Telugu");
    public static final Language TR = new Language("tr", "Turkish");
    public static final Language UDM = new Language("udm", "Udmurt");
    public static final Language UZ = new Language("uz", "Uzbek");
    public static final Language UK = new Language("uk", "Ukrainian");
    public static final Language UR = new Language("ur", "Urdu");
    public static final Language FI = new Language("fi", "Finnish");
    public static final Language FR = new Language("fr", "French");
    public static final Language HI = new Language("hi", "Hindi");
    public static final Language HR = new Language("hr", "Croatian");
    public static final Language CS = new Language("cs", "Czech");
    public static final Language SV = new Language("sv", "Swedish");
    public static final Language GD = new Language("gd", "Scottish");
    public static final Language ET = new Language("et", "Estonian");
    public static final Language EO = new Language("eo", "Esperanto");
    public static final Language JV = new Language("jv", "Javanese");
    public static final Language JA = new Language("ja", "Japanese");

}
