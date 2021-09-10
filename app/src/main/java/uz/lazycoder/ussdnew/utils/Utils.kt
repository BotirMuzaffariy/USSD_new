package uz.lazycoder.ussdnew.utils

import java.util.*
import android.net.Uri
import android.widget.Toast
import uz.lazycoder.ussdnew.R
import android.content.Intent
import android.app.AlertDialog
import android.content.Context
import java.lang.StringBuilder
import kotlin.collections.ArrayList
import androidx.lifecycle.MutableLiveData
import uz.lazycoder.ussdnew.database.AppDbRu
import uz.lazycoder.ussdnew.database.AppDbUz
import uz.lazycoder.ussdnew.database.entity.*
import uz.lazycoder.ussdnew.database.AppDbUzKr
import com.google.firebase.firestore.FirebaseFirestore

object Utils {

    fun callTo(context: Context, phone: String) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val intent = Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tel:${Uri.encode(phone)}")
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun goToLink(context: Context, link: String) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(link)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun createAboutDialog(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(context.resources.getString(R.string.tv_about))
        builder.setMessage(context.resources.getString(R.string.tv_about_the_app))
        builder.setNegativeButton(context.resources.getString(R.string.tv_back)) { dialog, _ -> dialog.dismiss() }

        builder.show()
    }

    fun createContactUsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(context.resources.getString(R.string.contact_us))
        builder.setMessage(context.resources.getString(R.string.tv_contact_us_text))

        builder.setNegativeButton(context.resources.getString(R.string.tv_back)) { dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton(context.resources.getString(R.string.tv_send_message)) { dialog, _ ->
            dialog.dismiss()
            val errorMessage = context.getString(R.string.error_something_went_wrong)

            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "text/plain"
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("uzmobiler@gmail.com"))
                context.startActivity(intent)
            } catch (e: Exception) {
                createToast(context, e.message ?: errorMessage)
            }
        }

        builder.show()
    }

    fun shareApp(context: Context) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val intent = Intent().apply {
                this.type = "text/plain"
                this.action = Intent.ACTION_SEND
                this.putExtra(
                    Intent.EXTRA_TEXT,
                    "${context.resources.getString(R.string.share_link_text)} https://play.google.com/store/apps/details?id=${context.packageName}"
                )
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun rateApp(context: Context) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val uri =
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun moreApps(context: Context) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val uri =
                Uri.parse("https://play.google.com/store/apps/developer?id=Mobiler+Company")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun openTelegramChannel(context: Context) {
        val errorMessage = context.getString(R.string.error_something_went_wrong)

        try {
            val uri =
                Uri.parse("https://t.me/mobiler_blog")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            createToast(context, e.message ?: errorMessage)
        }
    }

    fun changeLang(context: Context, language: String) {
        val locale = Locale(language)
        val config = context.resources.configuration

        Locale.setDefault(locale)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        MySharedPrefs.getInstance(context).setLang(language)
    }

    fun createToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setAllDataToDb(context: Context, liveData: MutableLiveData<Int>) {
        setDataFromFirestore(Consts.LANG_UZ, context, liveData)
        setDataFromFirestore(Consts.LANG_RU, context, liveData)
        setDataFromFirestore(Consts.LANG_UZ_KRILL, context, liveData)
    }

    private fun setDataFromFirestore(
        lang: String,
        context: Context,
        liveData: MutableLiveData<Int>
    ) {
        getDataFromInternet(context, lang, liveData)
        getDataFromMinute(context, lang, liveData)
        getDataFromNewsAndSale(context, lang, liveData)
        getDataFromOperator(context, lang, liveData)
        getDataFromService(context, lang, liveData)
        getDataFromSms(context, lang, liveData)
        getDataFromTariff(context, lang, liveData)
        getDataFromUssd(context, lang, liveData)
    }

    private fun getDataFromInternet(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<InternetEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/internet/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(InternetEntity::class.java)
                        item.preview = filterString(item.preview!!)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).internetDao().addInternet(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).internetDao().addInternet(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).internetDao().addInternet(list)
                        }
                    }

                    liveData.value = 1
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }

    }

    private fun getDataFromMinute(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<MinuteEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/minute/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(MinuteEntity::class.java)
                        item.preview = filterString(item.preview!!)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).minuteDao().addMinute(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).minuteDao().addMinute(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).minuteDao().addMinute(list)
                        }
                    }

                    liveData.value = 2
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromNewsAndSale(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<NewsAndSaleEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/news/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(NewsAndSaleEntity::class.java)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).newsAndSaleDao().addNewsAndSale(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).newsAndSaleDao().addNewsAndSale(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).newsAndSaleDao().addNewsAndSale(list)
                        }
                    }

                    liveData.value = 3
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromOperator(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<OperatorEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/operator/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(OperatorEntity::class.java)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).operatorDao().addOperators(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).operatorDao().addOperators(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).operatorDao().addOperators(list)
                        }
                    }

                    liveData.value = 4
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromService(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<ServiceEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/service/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(ServiceEntity::class.java)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).serviceDao().addServices(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).serviceDao().addServices(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).serviceDao().addServices(list)
                        }
                    }

                    liveData.value = 5
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromSms(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<SmsEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/sms/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(SmsEntity::class.java)
                        item.preview = filterString(item.preview!!)
                        item.desc = filterString(item.desc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).smsDao().addSms(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).smsDao().addSms(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).smsDao().addSms(list)
                        }
                    }

                    liveData.value = 6
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromTariff(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<TariffEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/tariff/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(TariffEntity::class.java)
                        item.longDesc = filterString(item.longDesc!!)
                        item.shortDesc = filterString(item.shortDesc!!)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).tariffDao().addTariff(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).tariffDao().addTariff(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).tariffDao().addTariff(list)
                        }
                    }

                    liveData.value = 7
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun getDataFromUssd(
        context: Context,
        lang: String,
        liveData: MutableLiveData<Int>
    ) {
        val list = ArrayList<UssdEntity>()
        val fbFirestore = FirebaseFirestore.getInstance()

        fbFirestore.collection("app/ussd/$lang")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result

                    result?.forEach {
                        val item = it.toObject(UssdEntity::class.java)
                        item.id = it.id.toInt()
                        list.add(item)
                    }

                    when (lang) {
                        Consts.LANG_UZ -> {
                            AppDbUz.getInstance(context).ussdDao().addUssd(list)
                        }
                        Consts.LANG_RU -> {
                            AppDbRu.getInstance(context).ussdDao().addUssd(list)
                        }
                        Consts.LANG_UZ_KRILL -> {
                            AppDbUzKr.getInstance(context).ussdDao().addUssd(list)
                        }
                    }

                    liveData.value = 8
                } else if (task.isCanceled) {
                    liveData.value = -1
                }
            }
    }

    private fun filterString(str: String): String {
        val result = StringBuilder()
        str.split(Consts.SPLIT_CHAR).forEach { result.append(it).append("\n") }
        return result.toString()
    }

}