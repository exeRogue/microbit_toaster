/*
 * Copyright 2018 Fundação CERTI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package certi.org.br.jocdandroidtestapp

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.org.certi.jocd.Jocd
import br.org.certi.jocd.Jocd.ErrorCode
import br.org.certi.jocd.tools.ProgressUpdateInterface
import com.egonzalez.project_toaster.R
import com.egonzalez.project_toaster.ToasterMVP

class AsyncFlashToolFlashBoard(val mView: ToasterMVP.View,
                               val flashFilePath: String?, // Board uniqueId
                               val uniqueId: String) : AsyncTask<String, Int, Boolean>(), ProgressUpdateInterface {
    private val TAG = "AsyncFlashBoard"


    // Context (for USB Manager).
    private val context: Context? = null
    private var exceptionOccurred = false

    override fun onPreExecute() {
        super.onPreExecute()
        mView.startProgressBarToast()
    }

    override fun doInBackground(vararg params: String): Boolean {
        try {

            if (this.flashFilePath == null) {
                this.exceptionOccurred = true
                onException(Exception("Can't flash without a file."))
                return false
            }

            this.exceptionOccurred = false
            val flashBoardError = Jocd.flashBoard(this.flashFilePath, this, this.uniqueId)
            if (flashBoardError != ErrorCode.SUCCESS) {
                this.exceptionOccurred = true
                onException(Exception("Failed with error code: " + flashBoardError.toString()))
                return false
            }
        }catch (e:Exception){
            mView.showMessage(e.toString())
            return false
        }
        return true
    }

    override fun onPostExecute(result: Boolean) {
        if (result) {
            mView.showMessage(R.string.flash_succes)
        } else {
            mView.showMessage(R.string.flash_unsucces)
        }
        mView.endProgressBarToast()
    }

    override fun onProgressUpdate(vararg values: Int?) {
                if (this.exceptionOccurred) {
            return
        }

        Log.d(TAG, "Flashing device... " + values[0] + "%")
        mView.setProgressBarToastProgress(values[0])
    }

    protected fun onException(exception: Exception) {
        mView.showMessage(exception.message.toString())
    }

    /*
   * Callback to receive the current percentage and
   * publish the progress.
   */
    override fun progressUpdateCallback(percentage: Int) {
        publishProgress(percentage)
    }

}
