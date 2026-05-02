package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * HomeActivity - Dashboard Utama Setelah Login
 *
 * Fitur:
 * - [01] Header gradient Indigo dengan sapaan nama user
 * - [02] TabLayout 3 tab: Beranda | Seminar | Profil
 * - [03] ViewPager2 dengan 3 Fragment
 * - [04] Data user diterima dari LoginActivity via Intent
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private var userName:  String = "User"
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_home)

        // [04] Ambil data user dari LoginActivity
        userName  = intent.getStringExtra("USER_NAME")  ?: "User"
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        setupHeader()
        setupTabLayout()
        applyNavBarInset()
    }

    /**
     * [01] Tampilkan sapaan nama user di header
     */
    private fun setupHeader() {
        val firstName = userName.split(" ").firstOrNull() ?: "User"
        findViewById<TextView>(R.id.tv_greeting).text  = "Halo, $firstName! 👋"
        findViewById<TextView>(R.id.tv_sub_greet).text = "Seminar Registration App"
    }

    /**
     * [02] & [03] Setup TabLayout + ViewPager2 dengan 3 tab
     */
    private fun setupTabLayout() {
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        val adapter = HomePagerAdapter(this, userName, userEmail)
        viewPager.adapter = adapter

        // Hubungkan tab dengan viewpager dan set icon + label
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "Beranda"; tab.setIcon(R.drawable.ic_home) }
                1 -> { tab.text = "Seminar"; tab.setIcon(R.drawable.ic_event) }
                2 -> { tab.text = "Profil";  tab.setIcon(R.drawable.ic_profile) }
            }
        }.attach()
    }

    private fun applyNavBarInset() {
        ViewCompat.setOnApplyWindowInsetsListener(tabLayout) { view, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.setPadding(0, 0, 0, navBarHeight)
            insets
        }
    }

    // ─── ViewPager2 Adapter ───────────────────────────────────────────────────

    inner class HomePagerAdapter(
        activity: AppCompatActivity,
        private val name:  String,
        private val email: String
    ) : FragmentStateAdapter(activity) {

        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0    -> BerandaFragment.newInstance(name, email)
            1    -> SeminarListFragment.newInstance(name, email)
            2    -> ProfilFragment.newInstance(name, email)
            else -> BerandaFragment.newInstance(name, email)
        }
    }
}

// =============================================================================
// FRAGMENT 1: BERANDA
// =============================================================================

class BerandaFragment : Fragment() {

    companion object {
        private const val ARG_NAME  = "NAME"
        private const val ARG_EMAIL = "EMAIL"

        fun newInstance(name: String, email: String) = BerandaFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_EMAIL, email)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_beranda, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name  = arguments?.getString(ARG_NAME)  ?: "User"
        val email = arguments?.getString(ARG_EMAIL) ?: ""

        // Animasi staggered
        val heroCard = view.findViewById<View>(R.id.card_hero)
        val statsRow = view.findViewById<View>(R.id.row_stats)
        val infoCard = view.findViewById<View>(R.id.card_info)

        listOf(heroCard, statsRow, infoCard).forEach { it.alpha = 0f }

        heroCard.animate().alpha(1f).translationYBy(30f).setStartDelay(100).setDuration(500).start()
        statsRow.animate().alpha(1f).translationYBy(20f).setStartDelay(280).setDuration(450).start()
        infoCard.animate().alpha(1f).translationYBy(20f).setStartDelay(430).setDuration(400).start()

        // Tombol Daftar Seminar → ke FormActivity
        view.findViewById<MaterialButton>(R.id.btn_daftar_seminar).setOnClickListener {
            val intent = Intent(requireContext(), FormActivity::class.java).apply {
                putExtra("USER_NAME",  name)
                putExtra("USER_EMAIL", email)
            }
            startActivity(intent)
        }
    }
}

// =============================================================================
// FRAGMENT 2: DAFTAR SEMINAR
// =============================================================================

class SeminarListFragment : Fragment() {

    companion object {
        private const val ARG_NAME  = "NAME"
        private const val ARG_EMAIL = "EMAIL"

        fun newInstance(name: String, email: String) = SeminarListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_EMAIL, email)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_seminar_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name  = arguments?.getString(ARG_NAME)  ?: ""
        val email = arguments?.getString(ARG_EMAIL) ?: ""

        // Tombol daftar di tab Seminar
        view.findViewById<MaterialButton>(R.id.btn_daftar_from_list).setOnClickListener {
            val intent = Intent(requireContext(), FormActivity::class.java).apply {
                putExtra("USER_NAME",  name)
                putExtra("USER_EMAIL", email)
            }
            startActivity(intent)
        }
    }
}

// =============================================================================
// FRAGMENT 3: PROFIL
// =============================================================================

class ProfilFragment : Fragment() {

    companion object {
        private const val ARG_NAME  = "NAME"
        private const val ARG_EMAIL = "EMAIL"

        fun newInstance(name: String, email: String) = ProfilFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_EMAIL, email)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profil, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name  = arguments?.getString(ARG_NAME)  ?: "User"
        val email = arguments?.getString(ARG_EMAIL) ?: "-"

        // Tampilkan data profil
        view.findViewById<TextView>(R.id.tv_profil_name).text  = name
        view.findViewById<TextView>(R.id.tv_profil_email).text = email

        // Tombol Logout → kembali ke LoginActivity, hapus semua back stack
        view.findViewById<MaterialButton>(R.id.btn_logout).setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
