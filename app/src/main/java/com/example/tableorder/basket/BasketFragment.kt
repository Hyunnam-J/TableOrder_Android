package com.example.tableorder.basket

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bxl.BXLConst
import com.bxl.config.editor.BXLConfigLoader
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentBasketBinding
import com.example.tableorder.main.MainFragment
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.BasketApiInterface
import com.example.tableorder.vo.basket.BasketVO
import com.example.tableorder.vo.basket.SendOrderVO
import jpos.JposConst
import jpos.POSPrinter
import jpos.POSPrinterConst
import jpos.config.JposEntry
import jpos.events.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.ByteBuffer
import java.text.NumberFormat
import java.util.*

class BasketFragment(map: HashMap<String, Any>) : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentBasketBinding? = null
    private val binding get() = _binding!!

    var map : HashMap<String, Any> = HashMap()
    var basketList : List<BasketVO>? = null

    var coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job
    val apiInterface : BasketApiInterface = ApiClient.getApiClient().create(
        BasketApiInterface::class.java)

    init {
        this.map = map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBasketBinding.inflate(inflater, container, false)

        val context = requireContext()

        binding.back.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
        }

        basketList = map.values.toList() as List<BasketVO>

        if(basketList!!.isEmpty()){
            displayImgv()
        }else{
            refreshBasketList()
        }

        binding.order.setOnClickListener{

            if(basketList!!.isEmpty()){
                Toast.makeText(context, "장바구니에 물건이 없습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            job = coroutineScopeIO.launch {

                val call : Call<String> = apiInterface.order(SendOrderVO(basketList!!, 1, 0))
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            if(response.body()=="success"){
                                Toast.makeText(context, "주문이 완료되었습니다", Toast.LENGTH_LONG).show()


















                                //프린트
                                //33p - 장치 설정 정보 저장 객체를 통해 기존 저장된 설정 파일을 연다. 없으면 새로 생성
                                val bxlConfigLoader = BXLConfigLoader(context)
                                try {
                                    bxlConfigLoader.openFile()
                                }catch (e : java.lang.Exception){
                                    e.printStackTrace()
                                    bxlConfigLoader.newFile()
                                }

                                //34p - 저장된 설정 정보를 얻어온다
                                try {
                                    for(entry : Any in bxlConfigLoader.entries){
                                        val jposEntry : JposEntry = entry as JposEntry
                                        val strLogicalName : String = jposEntry.logicalName

                                        //이전 것 삭제
                                        bxlConfigLoader.removeEntry(strLogicalName)
                                    }
                                }catch (e : java.lang.Exception){
                                    e.printStackTrace()
                                }

                                //35p - 장치 연결 정보 추가
                                try {
                                    bxlConfigLoader.addEntry("SRP-350III",
                                        BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER,
                                        BXLConfigLoader.PRODUCT_NAME_SRP_350III,
                                        BXLConfigLoader.DEVICE_BUS_WIFI,
                                        "address-ex-74:F0:7D:E4:11:AF")

                                    //새로 저장한 정보 저장
                                    bxlConfigLoader.saveFile()

                                }catch (e : Exception){
                                    e.printStackTrace()
                                }

                                //39p - 프린터 오픈, port 오픈, device 사용을 위해 반드시 선행되어야 함
                                try {
                                    var posPrinter : POSPrinter = POSPrinter(context)
                                    posPrinter.open("SRP-350III")
                                    posPrinter.claim(5000)
                                    posPrinter.deviceEnabled = true

                                    //장비 건강한지 상태 체크
                                    posPrinter.checkHealth(JposConst.JPOS_CH_INTERNAL)

                                    //비동기 모드
                                    posPrinter.asyncMode = true

                                    //프린터의 character set
                                    posPrinter.characterSet = BXLConst.CE_UTF8

                                    //프린터로 전송할 데이터의 인코딩 설정
                                    posPrinter.characterEncoding = BXLConst.CE_UTF8

                                    //p48 -- 100 : Full cut, 90 : Partial cut
                                    posPrinter.cutPaper(100)

                                    //p58 - 인쇄 위치 지정
                                    posPrinter.pageModeHorizontalPosition = 0
                                    posPrinter.pageModeVerticalPosition = 0

                                    //p58 - 인쇄할 데이터 전송(텍스트)
                                    posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, "Print Data\n")

                                    //p58 - 인쇄 시작
//                                    posPrinter.pageModePrint(POSPrinterConst.PTR_PM_PAGE_NOMAL) --페이지 노멀이 없음...
//                                    이건 페이지모드로 인쇄라는데..

                                    //다음 인쇄 위치까지 용지 feedding
                                    posPrinter.markFeed(0)

                                    //디바이스 종료
//                                    posPrinter.release()
//                                    posPrinter.close()
//                                    posPrinter.deviceEnabled = false
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }






























                                //장바구니 비우고
                                map.clear()

                                //프래그먼트 지운다
                                val manager = activity?.supportFragmentManager
                                manager?.beginTransaction()?.remove(this@BasketFragment)?.commit()
                                manager?.popBackStack()

                            }else{
                                Toast.makeText(context, "통신 장애", Toast.LENGTH_SHORT).show()
                                Toast.makeText(context, "다시 주문해 주십시오", Toast.LENGTH_SHORT).show()
                                parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
                            }
                        }else{
                            Toast.makeText(context, "통신 장애", Toast.LENGTH_LONG)
                            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
                        }
                        job.cancel()
                    }   //onResponse

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context, "통신 장애", Toast.LENGTH_LONG)
                        job.cancel()
                    }
                })  //call.enqueue
            }   //job = coroutineScopeIO.launch
        }   //binding.order.setOnClickListener
        return binding.root
    }   //onCreateView

    public fun calcTotalPrice() {
        var tempSum : Int = 0

        for(i in 0 until map.size){
            tempSum += basketList!![i].quantity * basketList!![i].getuPrice()
        }

        binding.totalPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(tempSum.toString()))
    }

    fun createImgv() : View{
        val imgv = ImageView(requireContext())
        imgv.setBackgroundResource(R.drawable.teong)

        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        imgv.layoutParams = lp

        //imgv.id = ViewCompat.generateViewId()
        return imgv
    }

    public fun displayImgv(){
        binding.basketFrameLayout.addView(createImgv())
    }

    fun refreshBasketList(){
        basketList = map.values.toList() as List<BasketVO>

        val adapter = BasketAdapter(requireContext(), map, basketList!!, this)
        val manager = LinearLayoutManager(requireContext(), VERTICAL, false)

        binding.basketRecv.adapter = adapter
        binding.basketRecv.layoutManager = manager

        calcTotalPrice()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}   //class - BasketFragment

