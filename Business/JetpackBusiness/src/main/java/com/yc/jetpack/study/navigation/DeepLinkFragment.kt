package com.yc.jetpack.study.navigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yc.jetpack.R

class DeepLinkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation_deeplink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = arguments?.getString("myarg")
        val editArgs = view.findViewById<EditText>(R.id.args_edit_text)
        val notificationButton = view.findViewById<Button>(R.id.send_notification_button)
        notificationButton.setOnClickListener(View.OnClickListener {
            val args = Bundle()
            args.putString("myarg", editArgs.text.toString())
            val deeplink = findNavController().createDeepLink()
                .setDestination(R.id.deeplink_dest)
                .setArguments(args)
                .createPendingIntent()

            createNotify(deeplink)
        })
    }

    private fun createNotify(deeplink: PendingIntent) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                "deeplink", "Deep Links", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val builder = NotificationCompat.Builder(
            context!!, "deeplink")
            .setContentTitle("Navigation")
            .setContentText("Deep link to Android")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(deeplink)
            .setAutoCancel(true)
        notificationManager.notify(0, builder.build())
    }

}