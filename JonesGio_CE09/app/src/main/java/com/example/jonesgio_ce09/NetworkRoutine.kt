// Gio Jones
// GPL - Term C202604
// NetworkRoutine.kt

package com.example.jonesgio_ce09

import android.util.Log
import kotlinx.coroutines.*
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class NetworkRoutine(private val listener: NetworkListener) {

    // Interface to communicate with Java MainActivity
    interface NetworkListener {
        fun onPreExecute()
        fun onPostExecute(books: ArrayList<Book>)
    }

    /**
     * Entry point of the data fetch process
     */
    fun startDataFetch(urlQuery: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Shows the indeterminate ProgressBar before task begins
            listener.onPreExecute()

            // Pulls down data on a background thread
            val books = withContext(Dispatchers.IO) {
                performNetworkCall(urlQuery)
            }

            // Hides the ProgressBar and update UI with retrieved data
            listener.onPostExecute(books)
        }
    }

     // Background networking logic using URLConnection & Apache Commons-IO
    private fun performNetworkCall(urlString: String): ArrayList<Book> {
        val booksList = ArrayList<Book>()
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null

        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection

            // Connects to the API
            connection.connect()

            // Checks if the response is successful (HTTP 200)
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream

                // Utilizes a stream reading library from the .jar file
                val jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8)

                // Parses data into objects
                val root = JSONObject(jsonString)
                if (root.has("items")) {
                    val itemsArray = root.getJSONArray("items")

                    for (i in 0 until itemsArray.length()) {
                        val item = itemsArray.getJSONObject(i)
                        val volumeInfo = item.getJSONObject("volumeInfo")

                        // Parses the title and thumbnail URL safely
                        val title = volumeInfo.optString("title", "No Title Available")

                        var thumbnailUrl = ""
                        if (volumeInfo.has("imageLinks")) {
                            thumbnailUrl = volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "")
                        }
                        // Create & add the custom Book object to the list
                        booksList.add(Book(title, thumbnailUrl))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NETWORK_ROUTINE", "Error fetching data: ${e.message}")
            e.printStackTrace()
        } finally {
            // Properly closes InputStream & disconnects URLConnection
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            connection?.disconnect()
        }

        return booksList
    }
}