package com.guoyi.github.utils;

import android.content.Context;
import android.text.TextUtils;

import com.guoyi.github.bean.Language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lao on 15/9/25.
 */
public class LanguageHelper {


    static LanguageHelper instance;
    static OfflineACache aCache;

    public static synchronized LanguageHelper getInstance() {
        return instance;
    }


    public static void init(Context application) {

        instance = new LanguageHelper(application);

    }


    List<Language> allLanguages;
    HashMap<String, Language> languageMap = new HashMap<>();
    List<Language> selectedLanguages = new ArrayList<>();

    Context context;


    private LanguageHelper(Context context) {
        this.context = context;
        aCache = OfflineACache.get(context);
        String selected_languages = aCache.getAsString("selected_languages");
        if (TextUtils.isEmpty(selected_languages)) {
            selectedLanguages = getDefaultSelectedLanguage();
        } else {
            selectedLanguages = GsonTools.changeGsonToSafeList(selected_languages, Language.class);
            if (selectedLanguages == null || selectedLanguages.size() == 0) {
                selectedLanguages = getDefaultSelectedLanguage();
            }
        }
    }


    public List<Language> getAllLanguages() {
        if (allLanguages != null) {
            return allLanguages;
        }

        try {
            StringBuilder buf = new StringBuilder();

            InputStream inputStream = context.getAssets().open("langs.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            allLanguages = GsonTools.changeGsonToSafeList(buf.toString(), Language.class);
        } catch (Exception e) {

        }
        return allLanguages;
    }

    public synchronized void setSelectedLanguages(List<Language> languageList) {
        selectedLanguages = languageList;
        saveToPref();
    }

    public synchronized void addSelectedLanguages(List<Language> languageList) {
        for (Language language : languageList) {
            if (!selectedLanguages.contains(language)) {
                selectedLanguages.add(language);
            }
        }
        saveToPref();
    }

    public synchronized List<Language> getSelectedLanguages() {


        return selectedLanguages;
    }


    public synchronized List<Language> getUnselectedLanguages() {
        List<Language> unselectedLanguages = new ArrayList<>();
        for (Language language : getAllLanguages()) {
            if (!selectedLanguages.contains(language)) {
                unselectedLanguages.add(language);
            }
        }

        return unselectedLanguages;
    }


    public Language getLanguageByName(String languageName) {
        if (languageMap.size() == 0) {

            for (Language language : getAllLanguages()) {
                languageMap.put(language.name, language);
            }
        }

        return languageMap.get(languageName);

    }


    private void saveToPref() {
        String languagesJson = GsonTools.createGsonString(selectedLanguages);
        aCache.put("selected_languages", languagesJson);
    }

    private List<Language> getDefaultSelectedLanguage() {
        String[] defaultLanguagesName = new String[]{"All Language", "JavaScript", "Java", "Go", "CSS", "Objective-C", "Python", "Swift", "HTML"};

        List<Language> defaultLanguages = new ArrayList<>();
        for (String langNAme : defaultLanguagesName) {
            defaultLanguages.add(getLanguageByName(langNAme));
        }
        return defaultLanguages;
    }
}
