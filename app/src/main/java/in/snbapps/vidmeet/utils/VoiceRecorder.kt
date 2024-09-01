package `in`.snbapps.vidmeet.utils

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class VoiceRecorder(private val context: Context, private val name: String) {

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    fun startRecording() {
        audioFile = File.createTempFile(name, ".3gp", context.cacheDir)

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFile?.absolutePath)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    fun getRecordedFile(): File? {
        return audioFile
    }

    fun cleanUp() {
        audioFile?.delete()
        audioFile = null
    }
}
