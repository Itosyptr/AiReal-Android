package capstone.tim.aireal.ui.toko

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import capstone.tim.aireal.R

@Suppress("UNREACHABLE_CODE")
class SectionPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val context: Context
) : FragmentStateAdapter(fragmentManager, lifecycle), Parcelable {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)

    constructor(parcel: TokoFragment) : this(
        TODO("fragmentManager"),
        TODO("lifecycle"),
        TODO("context")
    )


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProdukFragment()
            1 -> PenjualanFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Implement Parcelable.writeToParcel()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SectionPagerAdapter> {
        override fun createFromParcel(parcel: Parcel): SectionPagerAdapter {
            return SectionPagerAdapter(
                TODO("fragmentManager"),
                TODO("lifecycle"),
                TODO("context")
            )
        }

        override fun newArray(size: Int): Array<SectionPagerAdapter?> {
            return arrayOfNulls(size)
        }
    }
}