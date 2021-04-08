package com.university.gradetrak.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.university.gradetrak.R
import com.university.gradetrak.models.Module
import org.w3c.dom.Text

open class ModuleRecyclerAdapter (private val dataSet : List<Module>,
                             private val selectedModule: MutableLiveData<Module>,
                             private val listener: OnItemClickListener)
    : RecyclerView.Adapter<ModuleRecyclerAdapter.ViewHolder>() {

    var selectedIndex: Int = -1
    var selectedView: View? = null
    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_page_recycler_layout, parent, false)

        //Call the ViewHolder Constructor (inner class) to set up each Recycler view item
        return ViewHolder(adapterLayout)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.descriptionTextView.text = dataSet[position].name
        holder.moduleResult.text = dataSet[position].credits.toString()

        holder.changeBackgroundColour(holder.itemView, position == selectedIndex)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = dataSet.size

    fun getModuleAtPosition(position: Int): Module{
        return dataSet[position]
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener{
        val descriptionTextView : TextView = view.findViewById(R.id.tv_rv_module_description)
        val moduleResult : TextView = view.findViewById(R.id.tv_rv_module_result)

        init {
            view.setOnClickListener(this)
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View) {
            selectedModule.value = dataSet[adapterPosition]

            if(selectedView!=null){
                changeBackgroundColour(selectedView!!, false)
            }
            changeBackgroundColour(v, true)

            selectedView = v
            selectedIndex = adapterPosition
            listener.onItemClick(getModuleAtPosition(adapterPosition))
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun changeBackgroundColour(view: View, isSelected: Boolean){
            if(isSelected){
                view.setBackgroundResource(R.drawable.recycler_view_background_selected)
            } else {
                view.setBackgroundResource(R.drawable.recycler_view_background)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(module: Module)
    }
}