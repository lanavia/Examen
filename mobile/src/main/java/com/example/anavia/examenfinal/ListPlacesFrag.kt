package com.example.anavia.examenfinal


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.google.android.gms.common.api.Api


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListPlacesFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_list_places2, container, false)
        var rcvListClients = view.findViewById(R.id.rcvListPlaces) as RecyclerView
        rcvListClients.layoutManager = LinearLayoutManager(context)

        var myDataBase = (context as MainActivity).myDB
        val testClients=myDataBase!!.listar()




        return view
    }


}
