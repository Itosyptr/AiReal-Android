package Capstone.tim.aireal.toko

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Capstone.tim.aireal.R

class TokoFragment : Fragment() {

    companion object {
        fun newInstance() = TokoFragment()
    }

    private lateinit var viewModel: TokoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toko, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TokoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}