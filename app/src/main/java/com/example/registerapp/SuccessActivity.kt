package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton

/**
 * SuccessActivity ditampilkan setelah pendaftaran berhasil.
 *
 * Menampilkan:
 * - Animasi sukses dengan ikon centang
 * - Ringkasan data yang telah didaftarkan
 * - Tombol untuk kembali ke form atau ke halaman utama
 *
 * @author Student
 * @version 1.0
 */
class SuccessActivity : AppCompatActivity() {

    // ─── Constants ────────────────────────────────────────────────────────────
    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_KOTA = "extra_kota"
        const val EXTRA_HOBI = "extra_hobi"
    }

    // ─── Views ────────────────────────────────────────────────────────────────
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvKota: TextView
    private lateinit var tvHobi: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var btnKembali: MaterialButton
    private lateinit var btnDashboard: MaterialButton

    // ═════════════════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ═════════════════════════════════════════════════════════════════════════

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_success)

        initViews()
        loadData()
        setupAnimations()
        setupListeners()
    }

    // ═════════════════════════════════════════════════════════════════════════
    // INIT
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Menginisialisasi semua view dari layout.
     */
    private fun initViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        tvNama = findViewById(R.id.tvNamaSummary)
        tvEmail = findViewById(R.id.tvEmailSummary)
        tvGender = findViewById(R.id.tvGenderSummary)
        tvKota = findViewById(R.id.tvKotaSummary)
        tvHobi = findViewById(R.id.tvHobiSummary)
        btnKembali = findViewById(R.id.btnKembali)
        btnDashboard = findViewById(R.id.btnDashboard)
    }

    /**
     * Memuat data dari Intent extras dan menampilkannya di UI.
     */
    private fun loadData() {
        val nama = intent.getStringExtra(EXTRA_NAMA) ?: "Pengguna"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "-"
        val gender = intent.getStringExtra(EXTRA_GENDER) ?: "-"
        val kota = intent.getStringExtra(EXTRA_KOTA) ?: "-"
        val hobi = intent.getStringExtra(EXTRA_HOBI) ?: "-"

        tvWelcome.text = "Selamat Datang,\n$nama! 🎉"
        tvNama.text = nama
        tvEmail.text = email
        tvGender.text = gender
        tvKota.text = kota
        tvHobi.text = hobi
    }

    /**
     * Menerapkan animasi fade-in dan slide-up pada elemen UI.
     */
    private fun setupAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 800
        tvWelcome.startAnimation(fadeIn)
    }

    /**
     * Menyiapkan listener untuk tombol navigasi.
     */
    private fun setupListeners() {
        // Kembali ke form registrasi
        btnKembali.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        // Ke halaman Login setelah registrasi berhasil
        btnDashboard.setOnClickListener {
            val intent = android.content.Intent(this, LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    /**
     * Override back press untuk kembali ke RegisterActivity.
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
