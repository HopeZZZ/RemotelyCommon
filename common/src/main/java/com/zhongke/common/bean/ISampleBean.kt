package com.zhongke.common.bean

/**
 * @author: shuYang
 * @date: 2021/7/28
 * @description Sample组件想对外暴露的抽象bean$
 */
interface ISampleBean {

     /**
      * sample组件 bean对象对外暴露的属性
      */
     fun getSampleName() : String

     /**
      * sample组件 bean对象对外暴露的属性
      */
     fun getSampleAge() : Int
}