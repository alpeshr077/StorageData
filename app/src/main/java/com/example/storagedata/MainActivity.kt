package com.example.storagedata

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storagedata.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var Uri: Uri
    lateinit var storage: StorageReference
    lateinit var refDB : DatabaseReference
    var Image_Code = 12


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance().reference
        refDB = FirebaseDatabase.getInstance().reference

        binding.btnSelect.setOnClickListener {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, Image_Code)
        }
        binding.btnUpload.setOnClickListener {
            val ref = storage.child("images/${Uri.lastPathSegment}.jpg")
            var uploadTask = ref.putFile(Uri)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    var Key = refDB.root.push().key
                    refDB.root.child("Images").child(Key!!).child("image").setValue(downloadUri.toString())


                } else {

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == Image_Code) {


                Uri = data?.data!!
                binding.imgPoster.setImageURI(Uri)

            }
        }
    }
}