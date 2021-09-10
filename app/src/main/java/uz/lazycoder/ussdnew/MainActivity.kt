package uz.lazycoder.ussdnew

import android.Manifest
import android.view.View
import android.os.Bundle
import android.content.Intent
import androidx.core.view.GravityCompat
import uz.lazycoder.ussdnew.utils.Utils
import androidx.navigation.NavController
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import uz.lazycoder.ussdnew.databinding.ActivityMainBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(this, R.id.my_nav_host_fragment)

        binding.apply {
            ibMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

            ibTelegramChannel.setOnClickListener { Utils.openTelegramChannel(this@MainActivity) }

            ibShare.setOnClickListener { Utils.shareApp(this@MainActivity) }

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.m_telegram_channel -> Utils.openTelegramChannel(this@MainActivity)
                    R.id.m_contact_us -> Utils.createContactUsDialog(this@MainActivity)
                    R.id.m_share -> Utils.shareApp(this@MainActivity)
                    R.id.m_rate -> Utils.rateApp(this@MainActivity)
                    R.id.m_more_apps -> Utils.moreApps(this@MainActivity)
                    R.id.m_about -> Utils.createAboutDialog(this@MainActivity)
                }

                drawerLayout.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }

            cvHome.setOnClickListener {
                if (navController.currentDestination?.label != "fragment_home") {
                    navController.popBackStack()
                    navController.navigate(R.id.homeFragment)
                }
            }

            cvOperator.setOnClickListener {
                if (navController.currentDestination?.label != "fragment_operator") {
                    navController.popBackStack()
                    navController.navigate(R.id.operatorFragment)
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = getString(R.string.tv_connect_with_operator)
                }
            }

            cvBalance.setOnClickListener {
                askPermission(Manifest.permission.CALL_PHONE) {
                    Utils.callTo(this@MainActivity, "*105#")
                }.onDeclined {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle(getString(R.string.tv_info))

                    if (it.hasDenied()) {
                        builder.setMessage(getString(R.string.tv_per_denied))
                        builder.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            it.askAgain()
                        }
                    }

                    if (it.hasForeverDenied()) {
                        builder.setMessage(getString(R.string.tv_per_forever_denied))
                        builder.setPositiveButton(getString(R.string.tv_settings)) { dialog, _ ->
                            dialog.dismiss()
                            it.goToSettings()
                        }
                    }

                    builder.show()
                }
            }

            cvNews.setOnClickListener {
                if (navController.currentDestination?.label != "fragment_news") {
                    navController.popBackStack()
                    navController.navigate(R.id.newsFragment)
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = getString(R.string.tv_news)
                }
            }

            cvSettings.setOnClickListener {
                startActivity(Intent(this@MainActivity, LanguageActivity::class.java))
                finish()
            }
        }

    }

    fun changeToolbarState(title: String) {
        if (::binding.isInitialized) {
            binding.apply {
                tvTitle.text = title
                cvShare.visibility = View.GONE
                tvTitle.visibility = View.VISIBLE
                cvOpenTelegram.visibility = View.GONE
                ibMenu.setImageResource(R.drawable.ic_arrow_back)
                ibMenu.setOnClickListener { navController.popBackStack() }
            }
        }
    }

    fun restoreToolbarState() {
        if (::binding.isInitialized) {
            binding.apply {
                tvTitle.text = ""
                tvTitle.visibility = View.GONE
                cvShare.visibility = View.VISIBLE
                cvOpenTelegram.visibility = View.VISIBLE
                ibMenu.setImageResource(R.drawable.ic_menu)
                ibMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val label = navController.currentDestination?.label
            if (label == "fragment_home" || label == "fragment_operator" || label == "fragment_news") {
                finishAffinity()
            } else super.onBackPressed()
        }
    }

}