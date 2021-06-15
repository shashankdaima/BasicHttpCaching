package com.finals.foodrunner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.finals.foodrunner.R
import com.finals.foodrunner.adapter.RestaurantListAdapter
import com.finals.foodrunner.databinding.FragmentHomeBinding
import com.finals.foodrunner.objects.Restaurant
import com.finals.foodrunner.ui.MainActivity
import com.finals.foodrunner.volley.ALL_RESTAURANTS_URL
import com.finals.foodrunner.volley.HEADER
import com.finals.foodrunner.volley.VolleySingleton
import org.json.JSONException

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)


        val list = ArrayList<Restaurant>();
        val accessTokenRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, ALL_RESTAURANTS_URL, null,
            Response.Listener {
                try {
                    val obj = it.getJSONObject("data")
                    val success = obj.getBoolean("success")
                    if (success) {
                        binding.progressBar.visibility=View.INVISIBLE
                        val data = obj.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val restaurantJsonObject = data.getJSONObject(i)
                            val Restaurant = Restaurant(

                                id = restaurantJsonObject.getInt("id"),
                                name = restaurantJsonObject.getString("name"),
                                rating = restaurantJsonObject.getDouble("rating"),
                                cost_for_one = restaurantJsonObject.getInt("cost_for_one"),
                                isFavourite = false,
                                image_url = restaurantJsonObject.getString("image_url")

                            )
                            list.add(Restaurant)

                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }, Response.ErrorListener {
                Log.e("Home Fragment", it.message.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return HEADER
            }
        }

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(accessTokenRequest)
        val homeAdapter=RestaurantListAdapter()
        homeAdapter.submitList(list)
        binding.apply {
            listAllRestaurants.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                setHasFixedSize(true)
                adapter = homeAdapter

            }
        }

    }


}
