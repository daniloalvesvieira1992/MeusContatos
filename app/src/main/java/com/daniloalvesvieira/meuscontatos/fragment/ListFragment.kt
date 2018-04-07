package com.daniloalvesvieira.meuscontatos.fragment


import android.arch.persistence.room.Room
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import com.daniloalvesvieira.meuscontatos.DetalheActivity

import com.daniloalvesvieira.meuscontatos.R
import com.daniloalvesvieira.meuscontatos.adapter.ContatosAdapter
import com.daniloalvesvieira.meuscontatos.listener.OnItemClickListener
import com.daniloalvesvieira.meuscontatos.model.Contato
import com.daniloalvesvieira.meuscontatos.room.AppDatabase
import kotlinx.android.synthetic.main.fragment_list.*
import java.io.Serializable


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), Serializable {

    private var mAdapter: ContatosAdapter? = null
    var recyclerView: RecyclerView? = null
    var db: AppDatabase? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById(R.id.rvListaContatos)

        db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
                .allowMainThreadQueries().build()

        atualizarLista()

        return view
    }

    fun atualizarLista() {
        val data = db!!.contatoDao().getAll()
        setUpRecyclerView(data)
    }

    private fun setUpRecyclerView(data: List<Contato>) {

        mAdapter = ContatosAdapter(context, data)
        recyclerView!!.adapter = mAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        mAdapter!!.setClickListener(object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val contato = mAdapter!!.getItem(position)
                val i = Intent(activity, DetalheActivity::class.java)
                i.putExtra("ID", contato.contatoId)
                i.putExtra("NOME", contato.nome)
                i.putExtra("EMAIL", contato.email)
                i.putExtra("TELEFONE", contato.telefone)
                i.putExtra("ENDERECO", contato.endereco)
//                i.putExtra("LISTFRAGMENT",this@ListFragment)
                startActivityForResult(i, 100)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        atualizarLista()
    }
}