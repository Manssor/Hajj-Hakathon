package com.example.abdullah.mutmaen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.example.abdullah.mutmaen.MainActivity.Companion.LANG_CURRENT
import kotlinx.android.synthetic.main.activity_cam.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CamActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    val REQUEST_IMAGE = 100
    val REQUEST_VOISE = 300
    val REQUEST_PERMISSION = 200
    private var imageFilePath = ""
    //    private val TAG = "CamActivity"
    private var textToSpeech: TextToSpeech? = null

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private var credentialsProvider: AWSCredentialsProvider? = null
    private var awsConfiguration: AWSConfiguration? = null

    val myStringArray = arrayOfNulls<String>(6)
    var counter = 0
    var speckOut = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        textToSpeech = TextToSpeech(this, this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION)
        }

        openCameraIntent()

        confirmButton.setOnClickListener {
            startActivity(Intent(this, SuccessActivity::class.java))
        }

        toVideoCallButton.setOnClickListener {
            startActivity(Intent(this, FireVideoActivity::class.java))
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        LANG_CURRENT = preferences.getString("Language", "en")

        super.attachBaseContext(MyContextWrapper.Companion.wrap(newBase, LANG_CURRENT!!))
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = textToSpeech!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {

            val photoFile: File?
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

            val photoUri: Uri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            pictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            startActivityForResult(pictureIntent, REQUEST_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(Uri.parse(imageFilePath))
                when (speckOut) {
                    0 -> {
                        speakOut(getString(R.string.firstname_Question))
                        speckOut += 1
                    }
                }
                VoiceOn()

//                val myThread = object : Thread() {
//                    override fun run() {
//                        try {
//                            Thread.sleep(2000)
//                            genderTextView.text = getString(R.string.male)
//                            ageTextView.text = getString(R.string.number31)
//                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
//                        }
//
//                    }
//                }
//                myThread.start()
//                AWSMobileClient.getInstance().initialize(this) {
//                    credentialsProvider = AWSMobileClient.getInstance().credentialsProvider
//                    awsConfiguration = AWSMobileClient.getInstance().configuration
//
//                    IdentityManager.getDefaultIdentityManager().getUserID(object : IdentityHandler {
//                        override fun handleError(exception: Exception?) {
//                            Log.e(TAG, "Retrieving identity: ${exception?.message}")
//                        }
//
//                        override fun onIdentityId(identityId: String?) {
//                            Log.d(TAG, "Identity = $identityId")
//                            val cachedIdentityId = IdentityManager.getDefaultIdentityManager().cachedUserID
//                            // Do something with the identity here
//                        }
//                    })
//                }.execute()
//
//
//                val request = DetectFacesRequest().withImage(Image().withBytes(ByteBuffer.wrap(Files.readAllBytes(Paths.get(imageFilePath)))))
//                        .withAttributes("ALL")
//                val client = AmazonRekognitionClient(credentialsProvider)
//                val result = client.detectFaces(request)
//
//                val faceDetails =  result.faceDetails
//                faceDetails.forEach {
//                    Toast.makeText(this, it.ageRange.high.toString(),Toast.LENGTH_SHORT).show()
//                }
//
//                if (request.attributes.contains("ALL")){
//                    faceDetails.forEach {
//                        Toast.makeText(this, it.ageRange.high.toString(),Toast.LENGTH_SHORT).show()
//                    }
//                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == REQUEST_VOISE) {
            if (resultCode == RESULT_OK && data != null) {
                val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                myStringArray[counter] = res[0]
                counter += 1
                if (counter < 5) {
                    when (speckOut) {
                        1 -> {
                            speakOut(getString(R.string.lastname_Question))
                            speckOut += 1
                        }
                        2 -> {
                            speakOut(getString(R.string.diabetes_Question))
                            speckOut += 1
                        }
                        3 -> {
                            speakOut(getString(R.string.blood_and_pressure_Question))
                            speckOut += 1
                        }
                        4 -> {
                            speakOut(getString(R.string.medications_Question))
                            speckOut += 1
                        }
                    }
                    VoiceOn()
                }
            }
            firstNameTextView.text = myStringArray[0]
            lastNameTextView.text = myStringArray[1]
            genderTextView.text = getString(R.string.male)
            ageTextView.text = getString(R.string.number31)
            diabetesTextView.text = myStringArray[2]
            bloodTextView.text = myStringArray[3]
            medicationTextView.text = myStringArray[4]
        }
    }

    private fun VoiceOn() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        startActivityForResult(intent, REQUEST_VOISE)
    }

    override fun onBackPressed() {
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = image.absolutePath

        return image
    }

    private fun speakOut(Question: String?) {
        val text = Question.toString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }
}
