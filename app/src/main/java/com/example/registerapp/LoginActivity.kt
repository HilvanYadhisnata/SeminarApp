package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * LoginActivity - Halaman Login Utama
 *
 * Fitur:
 * - [01] Form login dengan email & password
 * - [02] Real-time validation (TextWatcher)
 * - [03] Hardcode credential (email: user@email.com, password: 123456)
 * - [04] Navigasi ke HomeActivity setelah login sukses
 * - [05] Navigasi ke RegisterActivity untuk daftar baru
 */
class LoginActivity : AppCompatActivity() {

    // ─── Views ───────────────────────────────────────────────────────────────
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvSignUp: TextView
    private lateinit var tvForgot: TextView

    // ─── Hardcoded Credentials ───────────────────────────────────────────────
    /** Data user yang diperbolehkan login (hardcode sesuai ketentuan UTS) */
    private val validUsers = mapOf(
        "user@email.com" to Pair("User Demo", "123456"),
        "hilvan@email.com" to Pair("Hilvan Yadhisnata", "hilvan123"),
        "admin@utb.ac.id" to Pair("Administrator UTB", "admin2025")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen immersive
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        initViews()
        setupAnimations()
        setupListeners()
    }

    private fun initViews() {
        tilEmail    = findViewById(R.id.til_email)
        tilPassword = findViewById(R.id.til_password)
        etEmail     = findViewById(R.id.et_email)
        etPassword  = findViewById(R.id.et_password)
        btnLogin    = findViewById(R.id.btn_login)
        tvSignUp    = findViewById(R.id.tv_signup)
        tvForgot    = findViewById(R.id.tv_forgot)
    }

    private fun setupAnimations() {
        val logo   = findViewById<View>(R.id.iv_logo)
        val title  = findViewById<View>(R.id.tv_welcome)
        val sub    = findViewById<View>(R.id.tv_subtitle)
        val card   = findViewById<View>(R.id.card_login)
        val signup = findViewById<View>(R.id.ll_signup)

        // Sembunyikan semua elemen terlebih dahulu
        listOf(logo, title, sub, card, signup).forEach { it.alpha = 0f }

        // Staggered fade-in animation
        logo.animate().alpha(1f).translationYBy(20f).setStartDelay(100).setDuration(500).start()
        title.animate().alpha(1f).translationYBy(20f).setStartDelay(250).setDuration(500).start()
        sub.animate().alpha(1f).setStartDelay(350).setDuration(400).start()
        card.animate().alpha(1f).translationYBy(30f).setStartDelay(450).setDuration(600).start()
        signup.animate().alpha(1f).setStartDelay(700).setDuration(400).start()
    }

    private fun setupListeners() {
        // [01] Real-time validation: bersihkan error saat user mulai mengetik
        etEmail.doOnTextChanged { _, _, _, _ ->
            val email = etEmail.text.toString().trim()
            if (email.contains("@")) tilEmail.error = null
        }
        etPassword.doOnTextChanged { _, _, _, _ ->
            if ((etPassword.text?.length ?: 0) >= 6) tilPassword.error = null
        }

        // [02] Tombol Login
        btnLogin.setOnClickListener {
            if (validateForm()) performLogin()
        }

        // [03] Navigasi ke RegisterActivity
        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // [04] Forgot password (placeholder)
        tvForgot.setOnClickListener {
            Toast.makeText(this, "Fitur lupa password belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * [VALIDASI] Memeriksa kelengkapan dan format input form login
     * @return true jika semua valid, false jika ada error
     */
    private fun validateForm(): Boolean {
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        var isValid  = true

        // Validasi email: tidak boleh kosong & harus mengandung "@"
        when {
            email.isEmpty() -> {
                tilEmail.error = "Email tidak boleh kosong"
                isValid = false
            }
            !email.contains("@") -> {
                tilEmail.error = "Format email tidak valid"
                isValid = false
            }
        }

        // Validasi password: tidak boleh kosong & minimal 6 karakter
        when {
            password.isEmpty() -> {
                tilPassword.error = "Password tidak boleh kosong"
                isValid = false
            }
            password.length < 6 -> {
                tilPassword.error = "Password minimal 6 karakter"
                isValid = false
            }
        }

        return isValid
    }

    /**
     * [LOGIN LOGIC] Memeriksa kredensial terhadap data hardcode
     * Jika cocok, navigasi ke HomeActivity dengan membawa nama user
     */
    private fun performLogin() {
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // Loading state
        btnLogin.isEnabled = false
        btnLogin.text = "Masuk..."

        btnLogin.postDelayed({
            val userData = validUsers[email]

            if (userData != null && userData.second == password) {
                // LOGIN SUKSES: bawa nama user ke HomeActivity
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("USER_NAME", userData.first)
                    putExtra("USER_EMAIL", email)
                }
                startActivity(intent)
                finish() // Hapus LoginActivity dari back stack

            } else {
                // LOGIN GAGAL
                btnLogin.isEnabled = true
                btnLogin.text = "Sign In"
                tilEmail.error = "Email atau password salah"
                tilPassword.error = "Email atau password salah"
                Toast.makeText(this, "Login gagal! Periksa email dan password Anda.", Toast.LENGTH_LONG).show()
            }
        }, 1000) // Simulasi proses autentikasi
    }
}
