package com.example.tableorder.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tableorder.R
import com.example.tableorder.SizingDialog
import com.example.tableorder.databinding.ItemRecvInnerBinding
import com.example.tableorder.vo.basket.BasketVO
import com.example.tableorder.vo.main.MainItemVO
import java.net.URL
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

    lateinit var basketDialog :Dialog


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
        h.uPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(itemList[i].getuPrice())

        var url = URL("http://192.168.0.8/static/TableOrderItemImage/"+itemList[i].getbColor()+".png")
        Glide.with(context).load(url).into(h.itemImage)

        h.menu.setOnClickListener{

            //메뉴 선택 시 애니메이션 효과
            val animSelect = AnimationUtils.loadAnimation(context, R.anim.anim_select)
            h.menu.startAnimation(animSelect)

            //메뉴 선택 시 배경색 변화 효과
            var transColor = arrayOf(ColorDrawable(Color.parseColor("#97A1E1")), ColorDrawable(Color.WHITE))
            val transitionDrawable = TransitionDrawable(transColor)
            h.menu.background = transitionDrawable
            transitionDrawable.startTransition(300)

            //장바구니 추가를 위해 액티비티를 띄운다
            basketDialog = Dialog(context)

            basketDialog.setContentView(R.layout.activity_basket)
            basketDialog.show()

            //화면 해상도를 가져와서 비율로 다이얼로그 크기 조절
            SizingDialog().sizingDialog(basketDialog, context, 0.8, 0.7)

            val itemPrice = basketDialog.findViewById<TextView>(R.id.itemPrice)
            val itemCancel = basketDialog.findViewById<Button>(R.id.itemCancel)
            val removeQuantity = basketDialog.findViewById<Button>(R.id.removeQuantity)
            val addQuantity = basketDialog.findViewById<Button>(R.id.addQuantity)
            val itemQuantity = basketDialog.findViewById<TextView>(R.id.itemQuantity)
            val itemConfirm = basketDialog.findViewById<Button>(R.id.itemConfirm)
            val dialogImage = basketDialog.findViewById<ImageView>(R.id.dialogImage)
            val itemNameInDialog = basketDialog.findViewById<TextView>(R.id.itemNameInDialog)
            val itemPriceInDialog = basketDialog.findViewById<TextView>(R.id.itemPriceInDialog)

            var url = URL("http://192.168.0.8/static/TableOrderItemImage/"+itemList[i].getbColor()+".png")
            Glide.with(context).load(url).into(dialogImage)

            itemNameInDialog.text = itemList[i].itemName2
            itemPriceInDialog.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(itemList[i].getuPrice())


            itemPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(itemList[i].getuPrice())

            itemCancel.setOnClickListener{
                basketDialog.dismiss()
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
            }

            addQuantity.setOnClickListener{

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
                        itemList[i].itemName1,
                        itemList[i].itemName2,
                        itemList[i].pos,
                        itemList[i].getpCode(),
                        itemList[i].tabNo,
                        itemList[i].itemCode,
                        itemList[i].getbColor(),
                        itemList[i].tax,
                        itemList[i].subUse,
                        itemList[i].sex,
                        itemList[i].area,
                        itemList[i].stockUse,
                        itemList[i].getuPrice(),
                        itemList[i].stock,
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
                            itemList[i].itemName1,
                            itemList[i].itemName2,
                            itemList[i].pos,
                            itemList[i].getpCode(),
                            itemList[i].tabNo,
                            itemList[i].itemCode,
                            itemList[i].getbColor(),
                            itemList[i].tax,
                            itemList[i].subUse,
                            itemList[i].sex,
                            itemList[i].area,
                            itemList[i].stockUse,
                            itemList[i].getuPrice(),
                            itemList[i].stock,
                            temp.quantity
                                    + Integer.parseInt(itemQuantity.text.toString())
                        )   //BasketVO
                    )   //map.replace
                }   //if-else
                Toast.makeText(context, itemList[i].itemName2+" "+itemQuantity.text+"개가 장바구니에 담겼습니다", Toast.LENGTH_LONG).show()
                basketDialog.dismiss()
            }   //itemConfirm.setOnClickListener
        }   //h.menu.setOnClickListener

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
}   //OuterAdapter-class



