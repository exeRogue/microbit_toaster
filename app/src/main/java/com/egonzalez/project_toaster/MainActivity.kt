package com.egonzalez.project_toaster

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import br.org.certi.jocd.board.MbedBoard
import br.org.certi.jocdconnandroid.JocdConnAndroid
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_board.*
import java.io.File

class MainActivity : AppCompatActivity(), ToasterMVP.View{
    var TAG:String = MainActivity::class.toString()
    var MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE:Int = 1;
    lateinit var recyclerViewFiles: RecyclerView;
    private lateinit var viewFileAdapter: FileAdapter
    lateinit var recyclerViewBoards: RecyclerView;
    private lateinit var viewBaordsAdapter: BoardAdapter
    lateinit var progressbar:ProgressBar
    lateinit var permissionIntent:PendingIntent;
    var ACTION_USB_PERMISSION: String? = null
    val ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED"
    val ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED"

    var broadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.getAction()
            Log.d(TAG, "broadcastReceiver onReceive() $action")
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
//                    val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                    val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            presenter.loadBoards()
                    } else {
                        Log.d("fefe", "Permission denied for device.")
                        //            textViewConnectedBoards.setText("Permission denied for device.");
                        //            fsm = Fsm.INIT;
                    }
                }
            }

            if (ACTION_USB_ATTACHED == action || ACTION_USB_DETACHED == action) {
                presenter.loadBoards()
            }
        }

    }

    override fun showMessage(messageId: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(messageId)
        val dialog = builder.create()
        dialog.show()
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage(message)
            val dialog = builder.create()
            dialog.show()
        }

    }
    override fun startProgressBar() {
        Log.d(TAG, "startProgressBar")
        progressbar.setVisibility(View.VISIBLE)

    }

    override fun endProgressBar() {
        Log.d(TAG, "endProgressBar")
        progressbar.setVisibility(View.GONE)
    }

    override fun showHexFiles(files: ArrayList<File>) {
        viewFileAdapter.clear()
        for (file in files){
            viewFileAdapter.add(file)
        }
        viewFileAdapter.notifyDataSetChanged()
    }

    override fun showBoards(mBoards: List<MbedBoard>) {
        viewBaordsAdapter.clear()
        for (mBoard in mBoards){
            viewBaordsAdapter.add(mBoard)
        }
        if (mBoards.size > 0){
            no_board.visibility = View.GONE
            viewBaordsAdapter.notifyDataSetChanged()
        }else{
            no_board.visibility = View.VISIBLE
        }
    }

    override fun startProgressBarToast() {
        progressBarToast.visibility = View.VISIBLE
        progressBarToast.max = 100
        progressBarToast.progress = 0


    }
    override fun setProgressBarToastProgress(progress: Int?) {
        progressBarToast.progress = progress?:0
    }

    override fun endProgressBarToast() {
        progressBarToast.visibility = View.GONE
    }

    lateinit var presenter: ToasterMVP.Presenter



    private fun init(){
        progressbar = findViewById(R.id.progressbar)
        presenter = ToasterPresenter()
        presenter.setView(this)
        recyclerViewFiles = findViewById(R.id.recycler_view_files)
        viewFileAdapter = FileAdapter(presenter)
        recyclerViewFiles.setAdapter(viewFileAdapter)
        recyclerViewFiles.setLayoutManager(LinearLayoutManager(this));
        recyclerViewBoards = findViewById(R.id.recycler_view_baords)
        viewBaordsAdapter = BoardAdapter(presenter)
        recyclerViewBoards.setAdapter(viewBaordsAdapter)
        recyclerViewBoards.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ACTION_USB_PERMISSION = this.packageName + ".USB_PERMISSION"
        permissionIntent = PendingIntent.getBroadcast(this, 0,
                Intent(ACTION_USB_PERMISSION), 0)
        var filter = IntentFilter()
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(ACTION_USB_ATTACHED)
        filter.addAction(ACTION_USB_DETACHED)
        registerReceiver(broadcastReceiver, filter)
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val devices = usbManager.deviceList
        for (key in devices.keys) {
            val usbDevice = devices[key]
            usbManager.requestPermission(usbDevice, permissionIntent)
        }


    }
    fun ask_for_usb_permission() {
    }

    override fun onResume() {
        super.onResume()
        ask_for_permision()
        presenter.loadHexsFiles()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        init()


        fab.setOnClickListener(View.OnClickListener {

            view -> presenter.loadHexsFiles()
        })

        JocdConnAndroid.init(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.go_to_make_codej -> web_page_open("https://makecode.microbit.org/")
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun web_page_open(urls: String):Boolean { // for more than one url
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        startActivity(intents)
        return true
    }

    private fun ask_for_permision(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun getCurrentBoardUId():String{
        try {
            var llm = recyclerViewBoards.layoutManager as LinearLayoutManager
            var linearLayout = recyclerViewBoards.getChildAt(llm.findFirstCompletelyVisibleItemPosition()) as LinearLayout
            var textViewUId = linearLayout.findViewById<TextView>(R.id.board_uid)
            var uid = textViewUId.text.toString()
            Log.d(TAG, "uid is $uid")
            return textViewUId.text.toString()
        }catch (e:Exception){
            Log.d(TAG, "exception is $e")
        }

        return ""
    }

    override fun onPause() {
        super.onPause()
        try {

            unregisterReceiver(broadcastReceiver)
        }catch (e: Exception)
        {

        }
    }
}
