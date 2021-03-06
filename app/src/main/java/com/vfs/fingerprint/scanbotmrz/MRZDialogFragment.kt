package com.vfs.fingerprint.scanbotmrz

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.vfs.fingerprint.AppClass
import com.vfs.fingerprint.R
import io.scanbot.mrzscanner.model.MRZRecognitionResult
import java.io.File
import java.util.*


class MRZDialogFragment : androidx.fragment.app.DialogFragment() {

    private val savePath = "/Veridium/TouchlessID_Export_Demo/"
    private var saveDir: File? = null // location to save biometric templates

    companion object {
        const val MRZ_DATA = "MRZ_DATA"
        const val NAME = "MRZDialogFragment"

        @JvmStatic
        fun newInstance(data: MRZRecognitionResult): MRZDialogFragment {
            val frag = MRZDialogFragment()
            val args = Bundle()
            args.putParcelable(MRZ_DATA, data)
            frag.arguments = args
            return frag
        }
    }

    private var mrzRecognitionResult: MRZRecognitionResult? = null

    private fun addContentView(inflater: LayoutInflater, container: ViewGroup?): View? {
        mrzRecognitionResult = arguments!!.getParcelable(MRZ_DATA)

        val view = inflater.inflate(R.layout.fragment_mrz_dialog, container)
        view.findViewById<TextView>(R.id.tv_data).text = extractData(mrzRecognitionResult!!)

        Log.d("TAG", "called ak check is going:")

        val appClass = AppClass.getInstance()
        Log.d("TAG", "called ak check passport name 2:" + mrzRecognitionResult!!.firstNameField().value)
        appClass.passportName = mrzRecognitionResult!!.firstNameField().value
        appClass.passportSurname = mrzRecognitionResult!!.lastNameField().value
        appClass.passportNumber = mrzRecognitionResult!!.documentCodeField().value
        appClass.passportDate = Calendar.getInstance().getTime().toString()

        createSubDirectory();

        return view
    }

    private fun createSubDirectory() {
        val sdCard = Environment.getExternalStorageDirectory()
        saveDir = File(sdCard.absolutePath + savePath + AppClass.getInstance().passportNumber)
        saveDir!!.mkdirs()
        AppClass.getInstance().setPath(saveDir!!.absolutePath)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.activity!!)

        val inflater = LayoutInflater.from(activity)

        val contentContainer = inflater.inflate(R.layout.holo_dialog_frame, null, false) as ViewGroup
        addContentView(inflater, contentContainer)

        builder.setView(contentContainer)


        builder.setPositiveButton(
                getString(R.string.cancel_dialog_button)) { _, _ ->
            run {
                dismiss()
            }
        }

        /*builder.setNegativeButton(
                R.string.copy_dialog_button) { _, _ ->
            run {
                var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (mrzRecognitionResult != null) {
                    val data = extractData(mrzRecognitionResult!!)

                    val clip = ClipData.newPlainText(data, data)

                    clipboard.prim = clip
                }
                dismiss()
            }
        }*/
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)

        return dialog
    }

    private fun extractData(result: MRZRecognitionResult): String {
        return StringBuilder()
                .append(getString(R.string.mrz_document_code)).append(result.documentCodeField().value).append("\n")
                .append(getString(R.string.mrz_first_name)).append(result.firstNameField().value).append("\n")
                .append(getString(R.string.mrz_last_name)).append(result.lastNameField().value).append("\n")
                .append(getString(R.string.mrz_issuing_organization)).append(result.issuingStateOrOrganizationField().value).append("\n")
                .append(getString(R.string.mrz_document_of_issue)).append(result.departmentOfIssuanceField().value).append("\n")
                .append(getString(R.string.mrz_nationality)).append(result.nationalityField().value).append("\n")
                .append(getString(R.string.mrz_dob)).append(result.dateOfBirthField().value).append("\n")
                .append(getString(R.string.mrz_gender)).append(result.genderField().value).append("\n")
                .append(getString(R.string.mrz_date_expiry)).append(result.dateOfExpiryField().value).append("\n")
                .append(getString(R.string.mrz_personal_number)).append(result.personalNumberField().value).append("\n")
                .append(getString(R.string.mrz_optional1)).append(result.optional1Field().value).append("\n")
                .append(getString(R.string.mrz_optional2)).append(result.optional2Field().value).append("\n")
                .append(getString(R.string.mrz_discreet_issuing_organization)).append(result.discreetIssuingStateOrOrganizationField().value).append("\n")
                .append(getString(R.string.mrz_valid_check_digits_count)).append(result.validCheckDigitsCount).append("\n")
                .append(getString(R.string.mrz_check_digits_count)).append(result.checkDigitsCount).append("\n")
                .append(getString(R.string.travel_doc_type)).append(result.travelDocTypeField().value).append("\n")
                .toString()
    }
}