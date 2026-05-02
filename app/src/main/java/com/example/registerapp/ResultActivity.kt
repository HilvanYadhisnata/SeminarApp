package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton

/**
 * ResultActivity - Halaman Hasil Pendaftaran Seminar
 *
 * Fitur:
 * - [01] Menampilkan "Pendaftaran Berhasil" dengan icon centang beranimasi
 * - [02] CardView summary: Nama, Email, No HP, Gender, Seminar dipilih
 * - [03] Tombol kembali ke HomeActivity (clear back stack form)
 * - [04] Tombol "Daftar Seminar Lain" kembali ke FormActivity
 * - [05] Staggered animation saat halaman dibuka
 */
class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_result)

        // [01] Ambil data dari FormActivity via Intent
        val nama    = intent.getStringExtra("NAMA")    ?: "-"
        val email   = intent.getStringExtra("EMAIL")   ?: "-"
        val phone   = intent.getStringExtra("PHONE")   ?: "-"
        val gender  = intent.getStringExtra("GENDER")  ?: "-"
        val seminar = intent.getStringExtra("SEMINAR") ?: "-"

        displayData(nama, email, phone, gender, seminar)
        setupAnimations()
        setupClickListeners()
    }

    /**
     * [02] Isi semua TextView dengan data dari Intent
     */
    private fun displayData(
        nama: String, email: String,
        phone: String, gender: String, seminar: String
    ) {
        findViewById<TextView>(R.id.tv_result_nama).text    = nama
        findViewById<TextView>(R.id.tv_result_email).text   = email
        findViewById<TextView>(R.id.tv_result_phone).text   = phone
        findViewById<TextView>(R.id.tv_result_gender).text  = gender
        findViewById<TextView>(R.id.tv_result_seminar).text = seminar
    }

    /**
     * [05] Staggered fade-in + scale animation untuk semua elemen
     */
    private fun setupAnimations() {
        val iconSuccess  = findViewById<View>(R.id.iv_success_icon)
        val tvBerhasil   = findViewById<View>(R.id.tv_berhasil)
        val tvSub        = findViewById<View>(R.id.tv_sub_berhasil)
        val cardSummary  = findViewById<View>(R.id.card_summary)
        val btnHome      = findViewById<View>(R.id.btn_home)
        val btnLagi      = findViewById<View>(R.id.btn_daftar_lagi)

        // Set alpha awal ke 0 + sedikit scale down
        listOf(iconSuccess, tvBerhasil, tvSub, cardSummary, btnHome, btnLagi).forEach {
            it.alpha = 0f
            it.scaleX = 0.85f
            it.scaleY = 0.85f
        }

        // Staggered animation berurutan
        iconSuccess.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setStartDelay(100).setDuration(600).start()

        tvBerhasil.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setStartDelay(300).setDuration(500).start()

        tvSub.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setStartDelay(420).setDuration(400).start()

        cardSummary.animate()
            .alpha(1f).scaleX(1f).scaleY(1f).translationYBy(40f)
            .setStartDelay(550).setDuration(600).start()

        btnHome.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setStartDelay(800).setDuration(400).start()

        btnLagi.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setStartDelay(900).setDuration(400).start()
    }

    private fun setupClickListeners() {
        // [03] Kembali ke HomeActivity, bersihkan semua activity di atasnya
        findViewById<MaterialButton>(R.id.btn_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // [04] Daftar seminar lain → kembali ke FormActivity
        findViewById<MaterialButton>(R.id.btn_daftar_lagi).setOnClickListener {
            // Pop back stack ke FormActivity agar bisa isi form baru
            val intent = Intent(this, FormActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    /**
     * Override back press → arahkan ke HomeActivity
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }
}
