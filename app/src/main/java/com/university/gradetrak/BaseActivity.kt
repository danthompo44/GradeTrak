package com.university.gradetrak

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private lateinit var progressDialog: Dialog

    /**
     * A method for showing a snack bar at the bottom of the UI, length is set to LONG
     * @param   message    The message to be displayed in the snack bar
     * @param   isError     Denotes if there is an error or not
     */
    fun showSnackBar(message: String, isError: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(isError){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.red)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.green)
            )
        }
        snackBar.show()
    }

    /**
     * Creates a progress dialog window with a custom message
     * @param   message     The dialogues message
     */
    fun showProgressDialog(message: String){
        //Initialise the Dialogue with the Base Activity Context
        progressDialog = Dialog(this)

        //Set the view to that of the progress bar xml layout
        progressDialog.setContentView(R.layout.progress_bar)

        //Retrieve the text view from within dialog and set the text to the passed in method
        val textView: AppCompatTextView = progressDialog.findViewById(R.id.tv_progress_bar)
        textView.text = message

        //Set the dialogue to not user cancelable
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        // Show the dialogue on screen
        progressDialog.show()
    }

    /**
     * Hides the progress bar from the UI
     */
    fun hideProgressDialog(){
        progressDialog.dismiss()
    }
}