package com.carty.riderapp.ui.newFlow

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.carty.riderapp.R


class FragmentBottomSheet: BottomSheetDialogFragment(), View.OnClickListener {
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var btn: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottSheetDialog)

    }


    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState)

        //inflating layout
        val view = View.inflate(context, R.layout.bottom_sheet_looking, null)
        btn = view.findViewById<Button>(R.id.btnCancel)


        bottomSheet.setContentView(view)

        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

       // btn!!.setOnClickListener(new)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior?.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO


        //setting max height of bottom sheet
//        binding.extraSpace.minimumHeight = windowHeight / 2


        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss()
                }
            }

            override fun onSlide(view: View, v: Float) {}
        })
       // btn!!.setOnClickListener(this)
        return bottomSheet
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnCancel ->{
                DialogCancel().show(childFragmentManager, "MyCustomFragment")
            }
        }
    }
}