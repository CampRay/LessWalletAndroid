package com.campray.lesswalletandroid.model;

import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.service.LanguageDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 语言对象业务处理模型
 * @author :Phills
 * @project:LanguageModel
 * @date :2017-08-22-18:09
 */
public class LanguageModel extends BaseModel {

    private static LanguageModel ourInstance = new LanguageModel();

    public static LanguageModel getInstance() {
        return ourInstance;
    }

    private LanguageModel() {
    }

    /**
     * 从服务器获取所有语言信息数据
     * @param listener
     */
    public void getAllLanguagesFromServer(final OperationListener< List<Language>> listener) {
        this.httpPostAPI(LanguageModel.URL_API_GETALLLANGUAGES, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Language> languageList = gson.fromJson(jArr, new TypeToken<List<Language>>() {}.getType());
                                if(languageList!=null) {
                                    for (Language language : languageList) {
//                                for(JsonElement item:jArr){
//                                    Language language = gson.fromJson(item, Language.class);
                                        try {
                                            long row = LanguageDaoService.getInstance(getContext()).insertOrUpdateLanguage(language);
                                        } catch (Exception exe) {
                                            listener.done(null, new AppException("E_1005"));
                                            break;
                                        }
                                    }
                                }
                            }
                            listener.done(null, null);
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 查询所有语言数据对象
     * @return
     */
    public List<Language> getAllLanguages() {
        try {
            return LanguageDaoService.getInstance(getContext()).getAllLanguages();
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据记录ID查询语言数据对象
     * @param languageId
     * @return
     */
    public Language getLanguageById(int languageId) {
        try {
            return LanguageDaoService.getInstance(getContext()).getLanguage(languageId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据语言名称查询语言数据对象
     * @param name
     * @return
     */
    public Language getLanguageByName(String name) {
        try {
            return LanguageDaoService.getInstance(getContext()).geLanguageByName(name);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据语言Code查询语言数据对象
     * @param code
     * @return
     */
    public Language getLanguageByCode(String code) {
        try {
            return LanguageDaoService.getInstance(getContext()).getLanguageByCode(code);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据语言culture查询语言数据对象
     * @param culture
     * @return
     */
    public Language getLanguageByCulture(String culture) {
        try {
            return LanguageDaoService.getInstance(getContext()).getLanguageByCulture(culture);
        }
        catch (Exception e){ }
        return null;
    }


}
