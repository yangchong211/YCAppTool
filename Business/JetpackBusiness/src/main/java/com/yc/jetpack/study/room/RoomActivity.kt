package com.yc.jetpack.study.room

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yc.jetpack.R
import com.yc.jetpack.study.binding.SolutionActivity
import com.yc.jetpack.study.model.HasParamsViewModel
import com.yc.jetpack.study.model.SavedDataRepository
import com.yc.jetpack.study.model.ViewModelActivity

class RoomActivity : AppCompatActivity() {

    private var container: ConstraintLayout? = null
    private var recyclerview: RecyclerView? = null
    private var fab: FloatingActionButton? = null
    val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    private var viewModel: WordViewModel? = null
    private var adapter: WordListAdapter? = null

    companion object{
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RoomActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        initView()
        initRecyclerView()
        initViewModel()
    }

    private fun initView() {
        container = findViewById(R.id.container)
        recyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        fab?.setOnClickListener {
            val intent = Intent(this@RoomActivity, NewWordActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun initRecyclerView() {
        val wordDiff = WordListAdapter.WordDiff()
        adapter = WordListAdapter(wordDiff)
        recyclerview?.adapter = adapter
        recyclerview?.layoutManager = LinearLayoutManager(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        // 获取带有参数的ViewModel
        //val viewModeFactory = HasParamsFactory(this.application)
        //viewModel = ViewModelProvider(this,viewModeFactory).get(WordViewModel::class.java)
        viewModel?.allWords?.observe(this,{ words->
            adapter?.submitList(words)
        })
    }

    //用户获取有参数model
    class HasParamsFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WordViewModel(application) as T
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val stringExtra = data?.getStringExtra(NewWordActivity.EXTRA_REPLY)
            val word = Word(stringExtra+"")
            viewModel?.insert(word)
        } else {
            Toast.makeText(applicationContext, "保存失败", Toast.LENGTH_LONG).show()
        }
    }

}