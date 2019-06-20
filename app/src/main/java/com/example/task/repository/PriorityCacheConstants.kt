package com.example.task.repository

import com.example.task.entities.PriorityEntity

class PriorityCacheConstants private constructor() {
    companion object {

        private val mPriorityCache = hashMapOf<Int, String>()

        fun getPriorityDescription(id:Int):String{
            if(mPriorityCache[id] == null)
                return ""

            return mPriorityCache[id].toString()
        }

        fun setCache(list: List<PriorityEntity>) {

            for (item in list) {
                mPriorityCache.put(item.id, item.description)
            }
        }
    }
}