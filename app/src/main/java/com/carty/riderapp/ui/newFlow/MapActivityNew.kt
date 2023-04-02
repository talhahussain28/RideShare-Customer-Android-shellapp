package com.carty.riderapp.ui.newFlow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.carty.riderapp.R


class MapActivityNew : AppCompatActivity(),View.OnClickListener {
    var btNext: Button  ? =null
    var btBack: ImageView  ? =null
    var homeView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_map_new)
        btNext = findViewById<Button>(R.id.btnNext)
        btNext!!.setOnClickListener(this)
        btBack = findViewById<ImageView>(R.id.imgIconBack)
        btBack!!.setOnClickListener(this)
      /*  tvTitle.text = "Map"
        imgIconBack.setOnClickListener(this)
        imgIconBack.visibility = View.GONE*/
      /*  bsllAcceptJob = homeView!!.bottom_sheet_looking
        bsAcceptJobBehaviour = BottomSheetBehavior.from(bsllAcceptJob)*/

    }


    private  fun openRideBottomSheet() {
        /*delay(2000)*/
        val bottomSheet = FragmentBottomSheet()
        bottomSheet.show(this.supportFragmentManager,bottomSheet.tag)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnNext ->{
               // DialogCancel().show(this.supportFragmentManager, "MyCustomFragment")
              openRideBottomSheet()
                //coordinatorOnBoarding.visibility = View.VISIBLE
            }
            R.id.imgIconBack ->{
                var intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                //openRideBottomSheet()
            }

        }
    }

/*    private lateinit var bsllAcceptJob: ConstraintLayout
    private lateinit var bsAcceptJobBehaviour: BottomSheetBehavior<ConstraintLayout>*/
}