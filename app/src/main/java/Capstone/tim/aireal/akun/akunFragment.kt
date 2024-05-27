package Capstone.tim.aireal.akun

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Capstone.tim.aireal.R

class akunFragment : Fragment() {

    companion object {
        fun newInstance() = akunFragment()
    }

    private lateinit var viewModel: AkunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AkunViewModel::class.java)
        // TODO: Use the ViewModel
    }

}