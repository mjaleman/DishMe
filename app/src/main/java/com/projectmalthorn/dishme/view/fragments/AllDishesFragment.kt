package com.projectmalthorn.dishme.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projectmalthorn.dishme.R
import com.projectmalthorn.dishme.databinding.FragmentAllDishesBinding
import com.projectmalthorn.dishme.view.activities.AddUpdateDishActivity
import com.projectmalthorn.dishme.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }


  private var _binding: FragmentAllDishesBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel =
      ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
    val root: View = binding.root

//    val textView: TextView = binding.textHome
//    homeViewModel.text.observe(viewLifecycleOwner, Observer {
//      textView.text = it
//    })
    return root
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_all_dishes, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.action_add_dish -> {
        startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
        return true
      }
    }
    return super.onOptionsItemSelected(item)

  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}