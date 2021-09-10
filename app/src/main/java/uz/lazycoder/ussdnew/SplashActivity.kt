package uz.lazycoder.ussdnew

import android.os.Looper
import android.view.View
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.app.AlertDialog
import uz.lazycoder.ussdnew.utils.Utils
import uz.lazycoder.ussdnew.utils.Consts
import androidx.lifecycle.MutableLiveData
import androidx.appcompat.app.AppCompatActivity
import uz.lazycoder.ussdnew.utils.NetworkHelper
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import uz.lazycoder.ussdnew.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val rebootTime = 20
    private val splashTime: Long = 1500
    private val numberList = ArrayList<Int>()

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var sharedPref: MySharedPrefs
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = MySharedPrefs.getInstance(this)

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val intent = if (MySharedPrefs.getInstance(this).getLang() == Consts.LANG_NO) {
                Intent(this, LanguageActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }

            sharedPref.setNumber(sharedPref.getNumber() + 1)
            startActivity(intent)
            finish()
        }

        if (sharedPref.getLang() != Consts.LANG_NO) Utils.changeLang(this, sharedPref.getLang())

        if (NetworkHelper(this).isNetworkConnected()) {
            if (sharedPref.getNumber() == -1 || sharedPref.getNumber() >= rebootTime) {
                // Get Data From Firebase
                binding.pbLoad.visibility = View.VISIBLE
                Utils.createToast(this, getString(R.string.data_loading))

                val liveData = MutableLiveData<Int>()
                Utils.setAllDataToDb(this, liveData)

                liveData.observe(this, {
                    numberList.add(it)

                    when (checkNumbers()) {
                        -1 -> {
                            if (sharedPref.getNumber() == -1) {
                                binding.pbLoad.visibility = View.GONE
                                val builder = AlertDialog.Builder(this)

                                builder.setTitle(resources.getString(R.string.tv_info))
                                builder.setMessage(resources.getString(R.string.error_something_went_wrong))
                                builder.setPositiveButton(resources.getString(R.string.tv_try_again)) { dialog, _ ->
                                    dialog.dismiss()
                                    recreate()
                                }

                                builder.show()
                            } else handler.postDelayed(runnable, splashTime)
                        }
                        1 -> {
                            val intent =
                                if (MySharedPrefs.getInstance(this).getLang() == Consts.LANG_NO) {
                                    Intent(this, LanguageActivity::class.java)
                                } else {
                                    Intent(this, MainActivity::class.java)
                                }

                            sharedPref.setNumber(0)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            } else handler.postDelayed(runnable, splashTime)
        } else {
            if (sharedPref.getNumber() == -1) {
                val builder = AlertDialog.Builder(this)

                builder.setTitle(resources.getString(R.string.tv_info))
                builder.setMessage(resources.getString(R.string.tv_requare_internet_first_time))
                builder.setPositiveButton(resources.getString(R.string.tv_try_again)) { dialog, _ ->
                    dialog.dismiss()
                    recreate()
                }

                builder.show()
            } else handler.postDelayed(runnable, splashTime)
        }

    }

    private fun checkNumbers(): Int {
        if (numberList.contains(-1)) return -1

        val n1 = numberList.filter { it == 1 }.size == 3
        val n2 = numberList.filter { it == 2 }.size == 3
        val n3 = numberList.filter { it == 3 }.size == 3
        val n4 = numberList.filter { it == 4 }.size == 3
        val n5 = numberList.filter { it == 5 }.size == 3
        val n6 = numberList.filter { it == 6 }.size == 3
        val n7 = numberList.filter { it == 7 }.size == 3
        val n8 = numberList.filter { it == 8 }.size == 3

        return if (n1 && n2 && n3 && n4 && n5 && n6 && n7 && n8) 1 else 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacks(runnable)
    }

}