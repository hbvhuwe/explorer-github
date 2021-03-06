package com.hbvhuwe.explorergithub.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.adapters.ReposAdapter
import com.hbvhuwe.explorergithub.viewmodel.ReposViewModel

class ReposFragment : Fragment() {
    private var mode = 0
    private lateinit var user: String
    private lateinit var listLoading: LinearLayout
    private lateinit var listLoadingText: TextView
    private lateinit var noContentText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var reposViewModel: ReposViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.content_list)
        listLoading = view.findViewById(R.id.list_loading)
        listLoadingText = view.findViewById(R.id.list_loading_text)
        noContentText = view.findViewById(R.id.no_content_text)

        mode = arguments?.getInt(Const.REPOS_MODE_KEY) ?: 0
        user = arguments?.getString(Const.USER_KEY) ?: Const.USER_LOGGED_IN

        if (mode == Const.REPOS_MODE_REPOS) {
            listLoadingText.text = getString(R.string.repos_loading_text)
            noContentText.text = getString(R.string.no_repos_text)
        } else {
            listLoadingText.text = getString(R.string.starred_repos_loading_text)
            noContentText.text = getString(R.string.no_starred_repos_text)
        }

        reposAdapter = ReposAdapter(emptyList())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = reposAdapter
        }

        reposViewModel = ViewModelProviders.of(this).get(ReposViewModel::class.java)
        App.netComponent?.inject(reposViewModel)
        reposViewModel.multipleInit(mode, user)

        reposViewModel.getRepos()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listLoading.visibility = View.GONE
                reposAdapter.dataset = it
                if (it.isEmpty()) {
                    noContentText.visibility = View.VISIBLE
                }
            }
        })
    }

    fun addRepo() {
        // TODO: show repo adding dialog
        Toast.makeText(this.activity, "Adding repo", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = ReposFragment()
    }
}
