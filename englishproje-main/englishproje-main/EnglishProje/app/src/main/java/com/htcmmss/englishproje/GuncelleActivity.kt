package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.htcmmss.englishproje.databinding.ActivityGuncelleBinding


class GuncelleActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuncelleBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGuncelleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        //Kullanıcının email adresini direk edittext in içine yazdırıyoruz
        var currentUser = auth.currentUser
        //binding.guncelleEmail.setText(currentUser?.email)


        //Realtime ın içindeki verilere ulaşıp edittext te yazdırıyoruz
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.guncelleAd.setText(snapshot.child("adi").value.toString())
                binding.guncelleSoyad.setText(snapshot.child("soyadi").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //Bilgilerimi güncelle button çalıştırma
        binding.guncelleButton.setOnClickListener {


            //Parola güncelleme
            var guncelleParola = binding.guncelleParola.text.toString().trim()
            currentUser!!.updatePassword(guncelleParola)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"Parola güncellendi", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Parola güncelleme başarısız", Toast.LENGTH_LONG).show()
                    }
                }

            //Ad soyad güncelleme
            var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
            currentUserDb?.removeValue()
            currentUserDb?.child("adi")?.setValue(binding.guncelleAd.text.toString())
            Toast.makeText(applicationContext,"Adı güncellendi", Toast.LENGTH_LONG).show()
            currentUserDb?.child("soyadi")?.setValue(binding.guncelleSoyad.text.toString())
            Toast.makeText(applicationContext,"Soyadı güncellendi", Toast.LENGTH_LONG).show()
        }

        //Giriş sayfasına gitmek için
        binding.guncelleGirisYap.setOnClickListener {
            intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}