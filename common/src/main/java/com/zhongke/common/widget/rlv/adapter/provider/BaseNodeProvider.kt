package com.zhongke.common.widget.rlv.adapter.provider

import com.zhongke.common.widget.rlv.adapter.BaseNodeAdapter
import com.zhongke.common.widget.rlv.adapter.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }

}