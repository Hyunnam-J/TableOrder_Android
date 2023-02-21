package com.example.tableorder.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tableorder.R
import com.example.tableorder.databinding.ItemRecvInnerBinding
import com.example.tableorder.vo.basket.BasketVO
import com.example.tableorder.vo.main.MainItemVO
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class InnerAdapter(
    context: Context,
    itemList: List<MainItemVO>,
    map: HashMap<String, Any>
) : Adapter<InnerAdapter.InnerViewHolder>(){

    val TAG = "로그"

    private var itemList: List<MainItemVO>
    var context: Context

    var toast: Toast? = null

    var map : HashMap<String, Any> = HashMap()

    lateinit var dialog :Dialog


    init {
        this.itemList = itemList
        this.context = context
        this.map = map
    }

    class InnerViewHolder(binding : ItemRecvInnerBinding) : ViewHolder(binding.root){

        var itemName: TextView
        var uPrice: TextView
        var itemImage: ImageView
        var menu: LinearLayout

        init {
            itemName = binding.itemName
            uPrice = binding.uPrice
            itemImage = binding.itemImage
            menu = binding.menu
        }
    }   //InnerViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemRecvInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(h: InnerViewHolder, i: Int) {

        h.itemName.text = itemList[i].itemName2
        h.uPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(itemList[i].getuPrice()))

        var url = URL("http://192.168.0.8/static/TableOrderItemImage/"+itemList[i].getbColor()+".png")
        Glide.with(context).load(url).into(h.itemImage)

        h.menu.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    h.menu.setBackgroundColor(Color.parseColor("#75ABB8"))
                }
                else -> {
                    h.menu.setBackgroundColor(Color.WHITE)

                    //장바구니 추가를 위해 액티비티를 띄운다
                    dialog = Dialog(context)

                    dialog.setContentView(R.layout.activity_basket)
                    dialog.show()

                    //화면 해상도를 가져와서 비율로 다이얼로그 크기 조절
                    dialogSizing()

                    val itemPrice = dialog.findViewById<TextView>(R.id.itemPrice)
                    val itemCancel = dialog.findViewById<Button>(R.id.itemCancel)
                    val removeQuantity = dialog.findViewById<Button>(R.id.removeQuantity)
                    val addQuantity = dialog.findViewById<Button>(R.id.addQuantity)
                    val itemQuantity = dialog.findViewById<TextView>(R.id.itemQuantity)
                    val itemConfirm = dialog.findViewById<Button>(R.id.itemConfirm)
                    val dialogImage = dialog.findViewById<ImageView>(R.id.dialogImage)

                    var url = URL("http://192.168.0.8/static/TableOrderItemImage/"+itemList[i].getbColor()+".png")
                    Glide.with(context).load(url).into(dialogImage)

                    itemPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(itemList[i].getuPrice()))

                    itemCancel.setOnClickListener{
                        dialog.dismiss()
                    }

                    removeQuantity.setOnClickListener{
                        if(itemQuantity.text == "1") return@setOnClickListener

                        val temp = Integer.parseInt(itemQuantity.text.toString())-1
                        itemQuantity.text = temp.toString()

                        itemPrice.text =
                            (
                                ( Integer.parseInt(itemList[i].getuPrice().toString()) )
                                    * ( Integer.parseInt(itemQuantity.text.toString()) )
                            ).toString()

                        if(itemQuantity.text == "1") removeQuantity.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24_unactivate)
                    }

                    addQuantity.setOnClickListener{
                        if(itemQuantity.text == "1") removeQuantity.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24)

                        val temp = Integer.parseInt(itemQuantity.text.toString())+1
                        itemQuantity.text = temp.toString()

                        itemPrice.text =
                            (
                                ( Integer.parseInt(itemList[i].getuPrice().toString()) )
                                    * ( Integer.parseInt(itemQuantity.text.toString()) )
                            ).toString()
                    }

                    itemConfirm.setOnClickListener{

                        if(map[itemList[i].itemCode] == null){
                            map[itemList[i].itemCode] = BasketVO(
                                itemList[i].comId,
                                itemList[i].getpName(),
                                itemList[i].itemName2,
                                itemList[i].pos,
                                itemList[i].getpCode(),
                                itemList[i].tabNo,
                                itemList[i].itemCode,
                                itemList[i].getuPrice(),
                                itemList[i].getbColor(),
                                Integer.parseInt(itemQuantity.text.toString())
                            )   //map.put
                        }else{

                            //다른 메뉴를 고르고 다시 돌아올 수 있으니까 전 개수를 더해주기 위해 임시 변수 이용
                            var temp : BasketVO = map[itemList[i].itemCode] as BasketVO

                            map.replace(
                                itemList[i].itemCode,
                                BasketVO(
                                    itemList[i].comId,
                                    itemList[i].getpName(),
                                    itemList[i].itemName2,
                                    itemList[i].pos,
                                    itemList[i].getpCode(),
                                    itemList[i].tabNo,
                                    itemList[i].itemCode,
                                    itemList[i].getuPrice(),
                                    itemList[i].getbColor(),
                                    temp.quantity
                                            + Integer.parseInt(itemQuantity.text.toString())
                                )   //BasketVO
                            )   //map.replace
                        }   //if-else

                        dialog.dismiss()

                    }   //itemConfirm.setOnClickListener
               }    //when - else
            }   //when event?.action
            //리턴값이 false면 seekbar 동작 안됨
            true //or false
        }   //menu.setOnTouchListener
    }   //onBindViewHolder

    override fun getItemCount(): Int {
        return itemList.size
    }

    //리사이클러 뷰 아이템 재활용으로 인한 데이터 꼬임 현상 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun showToast(string: String){
        toast?.cancel()
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun dialogSizing(){

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceHeight * 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

}   //OuterAdapter-class