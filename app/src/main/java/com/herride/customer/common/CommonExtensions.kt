package com.herride.customer.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.herride.customer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


var oneWayTripDate: Date? = null

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

var _clear: Boolean = false

/*
fun Boolean.clearBackStack(_context: Context): Boolean? {
    val manager = (_context as AppCompatActivity).supportFragmentManager
//    val manager = (AppCompatActivity()).getSupportFragmentManager()
    Log.e("SDfjkhgsdjkg", "" + manager.getBackStackEntryCount())
    if (manager.getBackStackEntryCount() > 0) {
        val first = manager.getBackStackEntryAt(0)
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        _clear = true
    }
    return _clear
}
*/


/**
 * Adds TextWatcher to the EditText
 */
fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun String.formatToServerDateTimeDefaults(
    date: String,
    serverformat: String,
    displayformat: String,
    locale_in: Locale,
    locale_out: Locale
): String? {
    var outputDate: String? = null
    val output = SimpleDateFormat(displayformat, locale_out)
    val input = SimpleDateFormat(serverformat, locale_in)
    try {

        oneWayTripDate = input.parse(date)// parse input

        //Crashlytics.logException(new Throwable("this is issue:"+oneWayTripDate.toString()));
        Log.e("", "datenewinfunction : $date")// format output
        outputDate = output.format(oneWayTripDate)
    } catch (e: ParseException) {
        outputDate = date
        e.printStackTrace()

        Log.e("SDgsdgsdgsdg", "" + e.message);
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//    return sdf.format(this)
    return outputDate
}


fun CompoundButton.check() {
    this.isChecked = true
}

fun CompoundButton.unCheck() {
    this.isChecked = false
}

fun CompoundButton.toggleCheck() {
    this.isChecked = !this.isChecked
}


fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeTransaprent() {
    this.setBackgroundResource(R.color.transparent)
}

/**
 * Toggle a view's visibility
 */
fun View.toggleVisibility(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun <V : View?> BottomSheetBehavior<V>.expand() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}

fun <V : View?> BottomSheetBehavior<V>.collapse() {
    this.state = BottomSheetBehavior.STATE_COLLAPSED
}

fun <V : View?> BottomSheetBehavior<V>.setPeek(height: Int) {
    this.peekHeight = height
}

fun <V : View?> BottomSheetBehavior<V>.expandWithPeekHeight(height: Int) {
    this.isHideable = false
    this.peekHeight = height
    this.state = BottomSheetBehavior.STATE_EXPANDED
}

fun <V : View?> BottomSheetBehavior<V>.hide() {
    this.isHideable = true
    this.state = BottomSheetBehavior.STATE_HIDDEN
}


fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}



fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}