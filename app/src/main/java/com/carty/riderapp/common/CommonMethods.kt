package com.carty.riderapp.common

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

class CommonMethods {


    companion object {
        fun onTextChangedListener(editext: EditText): TextWatcher? {
            return object : TextWatcher {
                override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                ) {
                    beforeTextChangedSize = editext.text.toString().length
                    //editext.setSelection(beforeTextChangedSize)
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                var beforeLength = 0
                var digitsValue = ""
                var beforeTextChangedSize = 0
                override fun afterTextChanged(s: Editable) {

                    editext.removeTextChangedListener(this)
                    try {
                        var numbres = editext.text.toString()
                        Log.e("setMobileFormat", "111111 - numbres = $numbres - beforeTextChangedSize = $beforeTextChangedSize")
                        //numbres = numbres.replace("()".toRegex(), "")
                        numbres = numbres.replace("-".toRegex(), "")
                        numbres = numbres.replace(" ".toRegex(), "")
                        Log.e(
                                "setMobileFormat",
                                "222222 - numbres = $numbres && length = ${numbres.length}"
                        )

                        var firstThreeNum = ""
                        var secondThreeNum = ""
                        var thirdThreeNum = ""
//                    if (numbres.length < 4 && numbres.length > 2) {
                        if (numbres.length == 3 && beforeTextChangedSize < 3) {
                            if (numbres.length == 3) {

                                if (!numbres.contains("(")) {
                                    firstThreeNum = "(${numbres[0]}${numbres[1]}${numbres[2]}) "
                                }
                                if (numbres.contains("(")) {
                                    if (!numbres.contains(")")) {
                                        firstThreeNum =
                                                "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}) "
                                    }
                                } else {
//                                if (!numbres.contains(")")) {
//                                    firstThreeNum = "(${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}) "
//                                }
                                    firstThreeNum = "(${numbres[0]}${numbres[1]}${numbres[2]}) "
                                }
                            }
                            if (numbres.length == 4) {
                                if (numbres.contains("(")) {
                                    firstThreeNum =
                                            "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}) "
                                }

                            }

                            editext.setText("${firstThreeNum}")
                            editext.setSelection(editext.text.toString().length)
                        } else {
                            if (numbres.length == 4 && beforeTextChangedSize < 4) {
                                if (numbres.contains("(")) {
                                    firstThreeNum =
                                            "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}) "
                                }
                                editext.setText("${firstThreeNum}")
                                editext.setSelection(editext.text.toString().length)
                            }
                        }
//                    if (numbres.length < 7 && numbres.length > 5) {
//                        if ((numbres.length == 8 && beforeTextChangedSize < 8) || (numbres.length == 9 && beforeTextChangedSize < 9)) {
                        if ((numbres.length == 8) || (numbres.length == 9)) {
                            firstThreeNum =
                                    "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
                            if (numbres.length == 8) {
                                secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
                            } else {
                                secondThreeNum =
                                        " ${numbres[5]}${numbres[6]}${numbres[7]}-${numbres[8]}"
                            }

                            //edtMobileNumberLogin.setText("${secondThreeNum}")
                            editext.setText("${firstThreeNum}${secondThreeNum}")
                            editext.setSelection(editext.text.toString().length)
                        }


                        if (numbres.length == 12) {
                            firstThreeNum =
                                    "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
                            secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
                            thirdThreeNum =
                                    "-${numbres[8]}${numbres[9]}${numbres[10]}${numbres[11]}"
                            editext.setText("${firstThreeNum}${secondThreeNum}${thirdThreeNum}")
                            editext.setSelection(editext.text.toString().length)
                        }


                        Log.e(
                                "setMobileFormat",
                                "3333333 - numbres = ${editext.text.toString()} && length = ${editext.text.toString().length}"
                        )
                        Log.e(
                                "setMobileFormat",
                                "=================================================================="
                        )
                    } catch (e: Exception) {
                        Log.e("setMobileFormat", "4444444 - Exception = ${e.localizedMessage}")
                        e.printStackTrace()
                    }
                    editext.addTextChangedListener(this)
                }
            }
        }


//        fun onTextChangedListener(editext: EditText): TextWatcher? {
//            return object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//                override fun afterTextChanged(s: Editable) {
//                    editext.removeTextChangedListener(this)
//                    try {
//                        var originalString = s.toString()
//                        val longval: Long
//                        if (originalString.contains(" ")) {
//                            originalString = originalString.replace(" ".toRegex(), "")
//                        }
//                        longval = originalString.toLong()
//                        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
////                    formatter.applyPattern("#,###,###,###")
//                        formatter.applyPattern("###,###,####")
//                        val formattedString: String = formatter.format(longval)
//
//                        //setting text after format to EditText
//                        Log.e("editext", "formattedString = $formattedString")
//                        editext.setText(formattedString.replace(",".toRegex(), " "))
////                        editext.setText(formattedString.replace("@".toRegex(), "("))
////                        editext.setText(formattedString.replace("$".toRegex(), ")"))
//
//                        editext.setSelection(editext.text.toString().length)
//                    } catch (nfe: NumberFormatException) {
//                        nfe.printStackTrace()
//                    }
//                    editext.addTextChangedListener(this)
//                }
//            }
//        }
        ///////////////////////////////////////////////////////
//        edtMobileNumberLogin.addTextChangedListener(object : TextWatcher {
//            var beforeLength = 0
//            var digitsValue = ""
//            override fun afterTextChanged(s: Editable?) {
//                edtMobileNumberLogin.removeTextChangedListener(this)
//                try {
//                    var numbres = edtMobileNumberLogin.text.toString()
//                    Log.e("setMobileFormat", "111111 - numbres = $numbres")
//                    //numbres = numbres.replace("()".toRegex(), "")
//                    numbres = numbres.replace("-".toRegex(), "")
//                    numbres = numbres.replace(" ".toRegex(), "")
//                    Log.e("setMobileFormat", "222222 - numbres = $numbres && length = ${numbres.length}")
//
//                    var firstThreeNum = ""
//                    var secondThreeNum = ""
//                    var thirdThreeNum = ""
////                    if (numbres.length < 4 && numbres.length > 2) {
//                    if (numbres.length == 3 && beforeTextChangedSize < 3) {
//                        firstThreeNum = "(${numbres[0]}${numbres[1]}${numbres[2]}) "
//                        edtMobileNumberLogin.setText("${firstThreeNum}")
//                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
//                    }
////                    if (numbres.length < 7 && numbres.length > 5) {
//                    if (numbres.length == 8 || numbres.length == 9) {
//                        firstThreeNum = "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
//                        if (numbres.length == 8) {
//                            secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
//                        }else{
//                            secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}-${numbres[8]}"
//                        }
//
//                        //edtMobileNumberLogin.setText("${secondThreeNum}")
//                        edtMobileNumberLogin.setText("${firstThreeNum}${secondThreeNum}")
//                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
//                    }
//
//
//                    if (numbres.length == 12) {
//                        firstThreeNum = "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
//                        secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
//                        thirdThreeNum = "-${numbres[8]}${numbres[9]}${numbres[10]}${numbres[11]}"
//                        edtMobileNumberLogin.setText("${firstThreeNum}${secondThreeNum}${thirdThreeNum}")
//                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
//                    }
//
//
//                    Log.e("setMobileFormat", "3333333 - numbres = ${edtMobileNumberLogin.text.toString()} && length = ${edtMobileNumberLogin.text.toString().length}")
//                    Log.e("setMobileFormat", "==================================================================")
//                } catch (e: Exception) {
//                    Log.e("setMobileFormat", "4444444 - Exception = ${e.localizedMessage}")
//                    e.printStackTrace()
//                }
//                edtMobileNumberLogin.addTextChangedListener(this)
//            }
//
//            var beforeTextChangedSize = 0
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                beforeTextChangedSize = edtMobileNumberLogin.text.toString().length
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//
//            }
//
//        })

    }


}