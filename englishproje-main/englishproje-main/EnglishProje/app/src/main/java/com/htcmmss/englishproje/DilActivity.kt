package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.htcmmss.englishproje.databinding.ActivityDilBinding

class DilActivity : AppCompatActivity() {
    lateinit var binding: ActivityDilBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        var yazilimDili = resources.getStringArray(R.array.Diller)


        if (binding.dilAnaSpinner != null){
            val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,yazilimDili)
            binding.dilAnaSpinner.adapter = adapter
            binding.dilAnaSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.dilAnaGizliText.text = yazilimDili[position]
                    binding.dilAnaGizliText.isVisible= false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }



        var currentUser = auth.currentUser
        //Kullanıcı Id sini alıp o ID adı altında adımızı ve soyadımızı kaydedelim. Bu adımlar standart.
        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
        //currentUserDb?.child("dil")?.setValue(binding.dilAnaGizliText.text.toString())
        //Toast.makeText(this@DilActivity, "Kayıt Başarılı", Toast.LENGTH_LONG).show()

        //Kaydet butonu ile kaydetme adımları
        binding.dilKayitOlButton.setOnClickListener {
            var dilana= binding.dilAnaGizliText.text.toString()

            //Realtime database deki ID ye ulaşıp altındaki childların içindeki veriyi sayfaya aktarma
            var userReference = databaseReference?.child(currentUser?.uid!!)
            userReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUserDb?.child("dilana")?.setValue(binding.dilAnaGizliText.text.toString())
                    Toast.makeText(this@DilActivity, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            //Hedef dil sayfasına gitmek için
            intent = Intent(applicationContext, HedefActivity::class.java)
            intent.putExtra("anadil",dilana)
            startActivity(intent)
            finish()

        }






    }
}