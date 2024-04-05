package com.kjc.cms.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjc.cms.adapter.AboutAdapter
import com.kjc.cms.databinding.FragmentAboutBinding
import com.kjc.cms.model.AboutData
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var aboutItemList: ArrayList<AboutData>
    private lateinit var aboutAdapter: AboutAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutRecycler.layoutManager = LinearLayoutManager(context)
        aboutAdapter = AboutAdapter()
        binding.aboutRecycler.adapter = aboutAdapter
        aboutItemList = arrayListOf()

        val jsonObject = context?.resources?.openRawResource(
            requireContext().resources.getIdentifier(
                "about", "raw", context?.packageName
            )
        )?.bufferedReader().use { it?.readText() }
        val outputJsonString = jsonObject?.let { JSONObject(it) }

        val about = outputJsonString?.getJSONObject("about")
        binding.aboutDetails.text = about?.get("description") as String
        val groups = about.get("details") as JSONArray
        Log.d("json", groups.length().toString())
        for(g in 0 until groups.length()){
            val techJSON =  groups.getJSONObject(g).get("tech") as JSONArray
            val contriJSON =  groups.getJSONObject(g).get("contributor") as JSONArray
            val tech: ArrayList<String> = arrayListOf()
            val contri: ArrayList<String> = arrayListOf()
            for(x in 0 until techJSON.length()){
                tech.add(techJSON.getString(x))
            }
            for(x in 0 until contriJSON.length()){
                contri.add(contriJSON.getString(x))
            }
            val i = AboutData(
                title = groups.getJSONObject(g).get("title") as String,
                tech = tech,
                contributor = contri
            )
            aboutItemList.add(i)
        }
        aboutAdapter.saveData(aboutItemList)
    }
}
