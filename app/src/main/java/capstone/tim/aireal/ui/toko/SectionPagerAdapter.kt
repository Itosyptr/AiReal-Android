package capstone.tim.aireal.ui.toko

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: TokoFragment) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ViewPagerFragment()
        fragment.arguments = Bundle().apply {
            putInt(ViewPagerFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }
}