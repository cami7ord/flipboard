package com.camilobaquero.flipboardgame.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.camilobaquero.flipboardgame.R
import com.camilobaquero.flipboardgame.databinding.MainFragmentBinding
import com.camilobaquero.flipboardgame.ui.playground.PlaygroundView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.board.fieldSelectedListener = object : PlaygroundView.FieldSelectedListener {
            override fun onFieldSelected(x: Int, y: Int) {
                viewModel.selectField(x, y)
            }
        }
        binding.reset.setOnClickListener {
            viewModel.reset()
        }
        observe()
        return binding.root
    }

    private fun observe() {
        viewModel.boardState.observe(viewLifecycleOwner, { boardState ->
            binding.board.state = boardState
        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
