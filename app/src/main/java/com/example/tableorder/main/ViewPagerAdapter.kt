package com.example.tableorder.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tableorder.vo.main.MainTabCodeVO

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    tabList: List<MainTabCodeVO>,
    map: HashMap<String, Any>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var tabList: List<MainTabCodeVO>

    var map : HashMap<String, Any> = HashMap()

    init {
        this.tabList = tabList
        this.map = map
    }

    override fun getItemCount(): Int {
        return tabList.size
    }

    //리사이클러 뷰를 사용하기 위해 일단은 소메뉴를 관리하는 아이템 프래그먼트를 반환하는데 선택한 대메뉴를 파라미터로 보낸다.
    override fun createFragment(i: Int): Fragment {
        when (i) {
            i -> return ItemFragment(tabList[i], map)
        }
        return ItemFragment(tabList[i], map)
    }
}