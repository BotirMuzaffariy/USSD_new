package uz.lazycoder.ussdnew.utils

import android.content.Context
import android.content.SharedPreferences

class MySharedPrefs {

    companion object {
        private const val KEY_NUM = "num"
        private const val KEY_LANG = "language"
        private const val SHARED_PREF_NAME = "ussd_shared_prefs"

        private var mySharedPrefs = MySharedPrefs()
        private lateinit var sharedPrefs: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor

        fun getInstance(context: Context): MySharedPrefs {
            if (!::sharedPrefs.isInitialized) {
                sharedPrefs =
                    context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            }

            return mySharedPrefs
        }

    }

    fun setLang(lang: String) {
        editor = sharedPrefs.edit()
        editor.putString(KEY_LANG, lang)
        editor.commit()
    }

    fun getLang(): String {
        return sharedPrefs.getString(KEY_LANG, Consts.LANG_NO) ?: Consts.LANG_NO
    }

    fun setNumber(num: Int) {
        editor = sharedPrefs.edit()
        editor.putInt(KEY_NUM, num)
        editor.commit()
    }

    fun getNumber(): Int {
        return sharedPrefs.getInt(KEY_NUM, -1)
    }

}