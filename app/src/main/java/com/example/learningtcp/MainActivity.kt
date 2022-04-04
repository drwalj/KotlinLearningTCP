package com.example.learningtcp

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.socket
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.lang.Exception
import java.net.PasswordAuthentication
import java.net.Socket
import java.util.*
import javax.security.auth.callback.PasswordCallback
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private var active: Boolean = false;
    private var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ProgressBar>(R.id.LoaderSpinner).visibility = View.INVISIBLE;

        findViewById<TextView>(R.id.TextBox).text ="nice"
        val addy = "192.168.0.87"
        val port =
        findViewById<Button>(R.id.daBtn).setOnClickListener {
            active = true;

            //pw auslesen:
            var InputPassword: String = findViewById<EditText>(R.id.PasswordBox).text.toString();
            var InputUsername: String = findViewById<EditText>(R.id.UsernameBox).text.toString();

            CoroutineScope(IO).launch {
                client(addy,7755, InputPassword, InputUsername)

            }
            findViewById<EditText>(R.id.PasswordBox).setText("");
            findViewById<EditText>(R.id.UsernameBox).setText("");

        }



    }

    private fun client(address: String, port: Int, pass:String = "", user:String = ""){
        if (pass != "" && user != ""){
            try {

                val connection = Socket(address, port);

                val writer:OutputStream = connection.getOutputStream();


                var e: ByteArray  = user.toByteArray()
                var sendung:String = "";
                for (i in e){
                    sendung+=i.toInt().toString();
                    sendung += " - ";
                }

                var ge: ByteArray  = pass.toByteArray()
                var senedeneng:String = "";
                for (i in ge){
                    senedeneng+=i.toInt().toString();
                    senedeneng += " - ";
                }

                findViewById<TextView>(R.id.TextBox).text ="Data Sent: \n$sendung --- ${user.toByteArray().size}\n---\n$senedeneng --- ${pass.toByteArray().size}\n"

                writer.write(user.toByteArray().size);
                writer.write(user.toByteArray());

                writer.write(user.toByteArray().size);
                writer.write(pass.toByteArray());

                writer.close();
                connection.close();
            }
            catch (e: Exception){

            }
        }
    }
}