package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.htcmmss.englishproje.databinding.ActivityUyeBinding

class UyeActivity : AppCompatActivity() {
    lateinit var binding: ActivityUyeBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUyeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        //Kaydet butonu ile kaydetme adımları
        binding.uyeDevamEt.setOnClickListener {
            var uyead= binding.uyeAd.text.toString()
            var uyesoyad= binding.uyeSoyad.text.toString()
            var uyeemail= binding.uyeEmail.text.toString()
            var uyeparola= binding.uyeParola.text.toString()

            //İçerisi boşsa kullanıcıya hata ver
            if (TextUtils.isEmpty(uyead)){
                binding.uyeAd.error = "Lütfen adınızı giriniz"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(uyesoyad)){
                binding.uyeSoyad.error = "Lütfen soyadınızı giriniz"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(uyeemail)){
                binding.uyeEmail.error = "Lütfen E-mail adresinizi giriniz"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(uyeparola)){
                binding.uyeParola.error = "Lütfen parolanızı giriniz"
                return@setOnClickListener
            }

            //Email, parola ve kullanıcı bilgilerini veritabanına ekleme
            auth.createUserWithEmailAndPassword(binding.uyeEmail.text.toString(), binding.uyeParola.text.toString())
                .addOnCompleteListener(this){ task->
                    if (task.isSuccessful){
                        //Şuanki kullanıcı bilgilerini alalım
                        var currentUser = auth.currentUser
                        //Kullanıcı Id sini alıp o ID adı altında adımızı ve soyadımızı kaydedelim. Bu adımlar standart.
                        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                        currentUserDb?.child("adi")?.setValue(binding.uyeAd.text.toString())
                        currentUserDb?.child("soyadi")?.setValue(binding.uyeSoyad.text.toString())
                        Toast.makeText(this@UyeActivity, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                        intent = Intent(applicationContext,DilActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@UyeActivity, "Kayıt Hatalı", Toast.LENGTH_LONG).show()
                    }

                }

        }

    }
}