package com.example.bookhelper

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText
    private lateinit var auth: FirebaseAuth

    val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

        // always do this
        if(result.resultCode == Activity.RESULT_OK){
            val data : Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            if(account != null){
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
            }
            Toast.makeText(this, "Horray", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.loginEmailEditText)
        password = findViewById(R.id.loginPasswordEditText)
        auth = Firebase.auth
    }

    fun onLoginClicked(view: View?){

        if(email.text.toString().isEmpty()){
            Toast.makeText(this, "¡Campo correo vacio!", Toast.LENGTH_SHORT).show()
        }else if (password.text.toString().isEmpty()) {
            Toast.makeText(this, "¡Campo contraseña vacio!", Toast.LENGTH_SHORT).show()
        }else{
            Firebase.auth.signInWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                    Toast.makeText(this, "¡Correo o contraseña inválida!", Toast.LENGTH_SHORT).show()
                //Log.e("FIREBASE", "Login fracaso: ${it.exception?.message}")
            }
        }
        //Firebase

    }

    fun onGoogleClicked(view: View?){
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1025093970150-jeb6bpjqjp2ucqkvdqkltusrd3juq94q.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        activityResultLauncher.launch(googleSignInClient.signInIntent)
    }

    fun onRegisterClicked(view : View?){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onForgotPasswordClicked(view : View?){
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account!=null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                            LoginMessage(true)
                        }else{
                            LoginMessage(false)
                        }
                    }
                }
            }catch (e:ApiException){
                Firebase.auth.signInWithEmailAndPassword(
                    "andres@gmail.com",
                    "asdfgh"
                ).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                        Toast.makeText(this, "¡Correo o contraseña inválida!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun LoginMessage(success:Boolean){
        if(success){
            Toast.makeText(this,"Login successfully",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Email or password incorrect",Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val RC_SIGN_IN = 100
    }
}