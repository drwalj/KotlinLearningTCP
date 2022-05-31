package com.example.learningtcp

import android.icu.util.Output
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.socket
import android.view.View
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
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
    private val addy = "172.16.36.159"
    private val port = 7755

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ProgressBar>(R.id.LoaderSpinner).visibility = View.INVISIBLE;

        findViewById<TextView>(R.id.TextBox).text ="nice"

        findViewById<Button>(R.id.daBtn).setOnClickListener {
            active = true;

            //pw auslesen:
            var InputPassword: String = findViewById<EditText>(R.id.PasswordBox).text.toString();
            var InputUsername: String = findViewById<EditText>(R.id.UsernameBox).text.toString();

            if (";" in InputPassword || ";" in InputUsername){ //checking if delegator is used in password/username and wether length is appropriate
                Toast.makeText(this@MainActivity, "illegal character: ; ", Toast.LENGTH_LONG).show()
            }

            else if (InputPassword.length > 30 || InputUsername.length > 30){
                Toast.makeText(this@MainActivity, "input exceeded 30 characters", Toast.LENGTH_LONG).show()
            }

            else{
                CoroutineScope(IO).launch {  //async
                    findViewById<TextView>(R.id.recievedTextbox).text = client(InputPassword, InputUsername);
                }
            }

            findViewById<EditText>(R.id.PasswordBox).setText("");
            findViewById<EditText>(R.id.UsernameBox).setText("");

        }

    }

    private fun SendToServer(message: String = ""):Boolean{ //HELPERMETHOD TO SEND
        try{
            val connection = Socket(addy,port);
            val writer:OutputStream = connection.getOutputStream();

            writer.write(message.toByteArray());
            writer.flush();
            return true; //RETURNS TRUE IF SUCCESSFULL
        }
        catch(e: Exception) {
            return false;
        }
    }

    private fun RecieveFromServer():String{
        var recievedMessage:String = "";
        try {
            val connection = Socket(addy,port);
            val scanner = Scanner(connection.inputStream)
            while (scanner.hasNextLine()) {
                recievedMessage += scanner.nextLine();
            }

            connection.close();
            return recievedMessage;

        }
        catch(r:Exception){
            return "error occured: ${r.message} ";
        }

    }

    private fun client(pass:String = "", user:String = ""):String{
        var recievedThings:String = "recieved:\n";
        if (pass != "" && user != ""){
            try {

                val connection = Socket(addy, port);

                val writer:OutputStream = connection.getOutputStream();


                var e: ByteArray  = user.toByteArray() //THIS IS FOR DEBUG
                var sendung:String = "";
                for (i in e){
                    sendung+=i.toInt().toString();
                    sendung += " - ";
                }

                var ge: ByteArray  = pass.toByteArray()
                var senedeneng:String = "";
                for (d in ge){
                    senedeneng+=d.toInt().toString();
                    senedeneng += " - ";
                }

                findViewById<TextView>(R.id.TextBox).text ="Data Sent: \n$sendung --- ${user.toByteArray().size}\n---\n$senedeneng --- ${pass.toByteArray().size}\n"

                writer.write(user.toByteArray()); //Username versenden
                writer.write(";".toByteArray()); //delegator senden
                writer.write(pass.toByteArray());// password senden



                writer.flush();

                val scanner = Scanner(connection.inputStream)
                while (scanner.hasNextLine()) {
                    recievedThings += scanner.nextLine();
                    break
                }
                findViewById<TextView>(R.id.recievedTextbox).text = recievedThings;

                connection.close();
            }
            catch (e: Exception){

            }
        }
        return recievedThings;

    }
}