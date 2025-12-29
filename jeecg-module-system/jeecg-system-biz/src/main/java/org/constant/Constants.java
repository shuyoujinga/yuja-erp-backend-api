package org.constant;

import org.springframework.stereotype.Component;

/**
 * 通用常量类
 * 魔法值消除
 */
@Component
public class Constants {
    //来源类型
    public interface DICT_MOVE_TYPE {
        // 采购入库
        String CGRK = "101";
        // 采购入库_冲销
        String CGRK_CX = "102";

        // 调拨入库
        String DBRK = "111";
        // 调拨入库_冲销
        String DBRK_CX = "112";

        // 调拨出库
        String DBCK = "121";
        // 调拨出库_冲销
        String DBCK_CX = "122";
    }

    //出入库类型
    public interface DICT_STOCK_TYPE {
        // 入库
        int IN = 1;
        // 出库
        int OUT = -1;

    }
    //来源类型
    public interface DICT_SOURCE_DOC_TYPE {
        // 采购！
        int CG = 0;
        //销售
        int XS = 1;
        // 生产
        int SC = 2;

        // 财务
        int CW = 3;
        // 仓储
        int CC = 4;
    }

    public interface BIZ_TYPE {
        // 采购收货！
        int CGSH = 0;
        // 物资调拨
        int WZDB = 1;
    }


    public interface YN {
        int Y = 0;
        int N = 1;
    }
    public interface DICT_YN {

        String YES = "0";

        String NO = "1";
    }



    /**
     * SQL的常量
     */
    public interface CONST_SQL {
        /**
         * 查询一条
         */
        String LIMIT_ONE = "limit 1";

    }

    public interface DICT_AUDIT_STATUS {
        //已审核
        Integer YES = 1;


        // 未审核
        Integer NO = 0;
    }

    public interface DICT_PUR_STATUS {
        //已引用
        Integer YYY = 1;


        // 未引用
        Integer WYY = 0;
    }

    /**
     * del_flag常量类
     */
    public interface DEL_FLAG {
        /**
         * 有效
         */
        String YES_ZERO = "0";
        /**
         * 无效
         */
        String NO_ONE = "1";
    }

    /**
     * 序列号编码集
     */
    public interface DICT_SERIAL_NUM {
        // BOM单
        String BOM = "BOM";
        // 采购报价
        String CGBJ = "RT";
        //采购申请
        String CGSQ = "SQ";
        //采购订单
        String CGDD = "ZN";
        //采购收货
        String CGSH = "ZA";
        //物料凭证
        String WLPZ = "MV";
        //采购结算
        String CGJS = "JS";
        //采购付款
        String CGFK = "FK";
        //采购退货
        String CGTH = "ZL";
        //物资调拨
        String WZDB = "DB";
        //物料领用
        String WLLY = "LY";
    }
    /**
     * 退货蕾西
      */
    public interface DICT_RETURN_TYPE{
        // 未收货退货
        Integer  NO_RECEIVE= 0 ;

        // 已收退货
        Integer RECEIVED= 1 ;




    }

    /**
     * 审核状态为
     */
    public interface DICT_AUDIT_FLAG {
        /**
         * 审核
         */
        String AUDIT = "audit";


        /**
         * 反审核
         */
        String REVERSE = "reverse";
    }

}
