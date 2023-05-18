package com.example.webservices2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UsersFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_users_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        fetchUserList() // Call the function to fetch user data

        return view
    }

    private fun fetchUserList() {
        CoroutineScope(Dispatchers.IO).launch {
            val users = getDogBreeds()
            withContext(Dispatchers.Main) {
                users?.let {
                    userList.clear()
                    userList.addAll(users)
                    userAdapter.setUserList(userList)
                }
            }
        }
    }

    private fun getDogBreeds(): ArrayList<User>? {
        val url = URL("https://reqres.in/api/users?page=2")
        val httpUrlCon = url.openConnection() as HttpURLConnection
        httpUrlCon.connect()

        if (httpUrlCon.responseCode == 200) {
            val bufferedReader = BufferedReader(InputStreamReader(httpUrlCon.inputStream))
            val response = bufferedReader.readText()
            bufferedReader.close()
            httpUrlCon.disconnect()
            Log.e("res", response)

            val jObj = JSONObject(response)
            Log.e("res", "Page no: ${jObj.getInt("page")}")

            val jUsers = jObj.getJSONArray("data")

            val users = ArrayList<User>()

            for (i in 0 until jUsers.length()) {
                val jUser = jUsers.getJSONObject(i)

                users.add(
                    User(
                        jUser.getInt("id").toString(),
                        jUser.getString("email"),
                        jUser.getString("first_name"),
                        jUser.getString("last_name"),
                    )
                )
            }

            return users
        }

        return null
    }
}
