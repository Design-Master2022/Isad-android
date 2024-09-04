package com.design_master1.isad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design_master1.isad.databinding.ItemQuestionBinding
import com.design_master1.isad.model.network.response.FetchQuestionsResponseClasses

class QuestionsAdapter: RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    private var questions = mutableListOf<FetchQuestionsResponseClasses.Question>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.sessionName.text = questions[position].name // change it to session name
        holder.binding.question.text = questions[position].question
    }

    override fun getItemCount(): Int = questions.size

    fun setData(questions: List<FetchQuestionsResponseClasses.Question>){
        this.questions = questions.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root)
}

