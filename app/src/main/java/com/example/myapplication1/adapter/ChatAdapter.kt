package com.example.myapplication1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication1.R

/**
 * The adapter for our chat RecyclerView. This class is responsible for taking a list
 * of Message objects and displaying them as chat bubbles.
 */
class ChatAdapter(private val messageList: MutableList<Message>) : 
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    // These constants define the two different types of views (chat bubbles) we have.
    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_ALICE = 2

    /**
     * This is the "ViewHolder". It represents a single item (a single chat bubble)
     * in our list. It holds a reference to the TextView inside the bubble layout.
     */
    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
    }

    /**
     * This method is called by the RecyclerView when it needs to know what type of
     * view to create for a specific message in the list. We check the sender and
     * return the corresponding view type constant.
     */
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.sender == Message.SENDER_USER) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_ALICE
        }
    }

    /**
     * This method is called when the RecyclerView needs to create a new ViewHolder
     * (a new chat bubble). We check the viewType and inflate (create) the correct
     * XML layout file (either item_chat_user.xml or item_chat_alice.xml).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = if (viewType == VIEW_TYPE_USER) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_alice, parent, false)
        }
        return MessageViewHolder(view)
    }

    /**
     * This method is called to display the data for a specific message. It takes the
     * ViewHolder (the bubble) and the message's position in the list, finds the
     * correct message object, and sets the text in the bubble's TextView.
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.messageTextView.text = message.text
    }

    /**
     * This method simply tells the RecyclerView how many total items are in our list.
     */
    override fun getItemCount(): Int {
        return messageList.size
    }
}