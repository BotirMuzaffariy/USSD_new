package uz.lazycoder.ussdnew

import android.os.Bundle
import android.content.Intent
import uz.lazycoder.ussdnew.utils.Utils
import uz.lazycoder.ussdnew.utils.Consts
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import androidx.appcompat.app.AppCompatActivity
import uz.lazycoder.ussdnew.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {

    private lateinit var sharedPref: MySharedPrefs
    private lateinit var binding:ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = MySharedPrefs.getInstance(this)

        binding.apply {

            btnUz.setOnClickListener {
                Utils.changeLang(this@LanguageActivity, Consts.LANG_UZ)
                goToMainActivity()
            }

            btnRu.setOnClickListener {
                Utils.changeLang(this@LanguageActivity, Consts.LANG_RU)
                goToMainActivity()
            }

            btnUzKrill.setOnClickListener {
                Utils.changeLang(this@LanguageActivity, Consts.LANG_UZ_KRILL)
                goToMainActivity()
            }

        }

    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

}