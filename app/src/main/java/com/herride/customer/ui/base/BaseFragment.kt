package com.herride.customer.ui.base


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.herride.customer.R
import com.herride.customer.common.Constants
import com.herride.customer.ui.base.listeners.BaseView
import com.herride.customer.utils.RepoModel
import org.koin.android.ext.android.inject

/**
 *
 */


abstract class BaseFragment : Fragment(), BaseView {

    val repo: RepoModel by inject()
//    abstract val layoutId: Int
//    protected abstract fun initializeDagger()
//    protected abstract fun initializePresenter()
//    protected abstract fun initializeViewModel()
//    abstract fun observeViewModel()
//    protected abstract fun initViewBinding()

    var baseActivity: BaseActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity

        //AndroidInjection.inject(activity)


//        initializeDagger()
//        initializePresenter()
//
//        initViewBinding()
//        initializeViewModel()
//        observeViewModel()
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(layoutId, container, false)
//    }


    fun isInternetAvailable(msg: String): Boolean {
        return baseActivity!!.isInternetAvailable(msg)
    }

    fun replaceFragment(
            @NonNull fragment: Fragment,
            backStackName: Boolean = false,
            popStack: Boolean = false,
            @IdRes containerViewId: Int = R.id.main_content
    ) {

        Log.e("SDfjkjksdhkfjsf", "" + popStack)
        baseActivity!!.replaceFragment(fragment, backStackName, popStack, containerViewId)
    }


    fun addFragment(
            @NonNull fragment: Fragment,
            backStackName: Boolean = false,
            aTAG: String = "",
            @IdRes containerViewId: Int = R.id.main_content

    ) {
        baseActivity!!.addFragment(fragment, backStackName, aTAG, containerViewId)
    }


    fun permissionCheckingOne(
            permissionArray: Array<String>,
            isForceFullyPermission: Boolean = true,
            isCalling: Boolean = false,
            contactNumber: String = "0",
            isGetAddress: Boolean = false,
            isMarkerPlace: Boolean = false,
            isAnimateCamera: Boolean = false,
            isEnableGPS: Boolean = false,
            mMap: GoogleMap? = null,//
            mMarker: Marker? = null,//
            latLng: LatLng? = null,
            markerImg: Int = 0,
            markerHeight: Int = 20,
            markerWidth: Int = 20,
            cameraZoomLavel: Float = baseActivity!!.cameraZoomLavel15_0_f,
            wedgets: Any? = null
    ): Any? {

        return baseActivity!!.permissionCheckingOne(
                permissionArray,
                isForceFullyPermission,
                isCalling,
                contactNumber,
                isGetAddress,
                isMarkerPlace,
                isAnimateCamera,
                isEnableGPS,
                mMap,
                mMarker,
                latLng,
                markerImg,
                markerHeight,
                markerWidth,
                cameraZoomLavel,
                wedgets
        )
    }


    fun msgDialog(msg: String, dialogTye: String? = Constants.ERROR) {
        if (activity != null) {
            (activity as BaseActivity).msgDialog(msg, dialogTye)
        }
    }

    fun showLoading() {
        baseActivity?.showLoading();
    }

    fun hideLoading() {
        baseActivity?.hideLoading();
    }


    fun placeMarkerOnMapWithSize(
            mMap: GoogleMap,
            location: LatLng,
            markerImg: Int,
            markerHeight: Int,
            markerWeight: Int,
            comeFrom: String
    ): Marker {


        val bitmapdraw =
                this.getResources().getDrawable(markerImg) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, markerWeight, markerHeight, false)

        val markerOptions =
                MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        val marker = mMap!!.addMarker(markerOptions)

        //val titleStr = getAddress(location)  // add these two lines
        markerOptions.title(comeFrom)

        // mMap!!.addMarker(markerOptions)

        return marker
    }

}
