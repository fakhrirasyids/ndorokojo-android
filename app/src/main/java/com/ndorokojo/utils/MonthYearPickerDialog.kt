package com.ndorokojo.utils

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.ndorokojo.R
import java.util.Calendar


class MonthYearPickerDialog : DialogFragment() {
    private var listener: OnDateSetListener? = null

    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val cal: Calendar = Calendar.getInstance()
        val dialog: View = inflater.inflate(R.layout.date_picker_dialog, null)
        val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH)+1
        val year: Int = cal.get(Calendar.YEAR)
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year
        builder.setView(dialog)
            .setPositiveButton("Submit",
                DialogInterface.OnClickListener { dialog, id ->
                    listener!!.onDateSet(
                        null,
                        yearPicker.value,
                        monthPicker.value,
                        0
                    )
                })
//            .setNegativeButton(
//                "Batal"
//            ) { _, _ ->
//                this@MonthYearPickerDialog.dialog?.cancel()
//            }
        return builder.create()
    }

    companion object {
        private const val MIN_YEAR = 1900
        private const val MAX_YEAR = 2099
    }
}