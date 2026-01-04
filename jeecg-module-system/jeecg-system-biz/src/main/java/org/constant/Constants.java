package org.constant;

import org.springframework.stereotype.Component;

/**
 * 通用常量类
 * 魔法值消除
 */
@Component
public class Constants {

    /**
     * 其他入库类型（含冲销）
     * 编码规则：
     * - String 类型
     * - 奇数：正向入库
     * - 偶数：冲销入库
     * - 一正一反成对
     */
    public interface DICT_OTHER_IN_TYPE {

        /**
         * 报废返库
         */
        String SCRAP_RETURN = "271";
        /**
         * 报废返库-冲销
         */
        String SCRAP_RETURN_REVERSE = "272";

        /**
         * 借出归还
         */
        String BORROW_RETURN = "273";
        /**
         * 借出归还-冲销
         */
        String BORROW_RETURN_REVERSE = "274";

        /**
         * 委外退料
         */
        String OUTSOURCE_RETURN = "275";
        /**
         * 委外退料-冲销
         */
        String OUTSOURCE_RETURN_REVERSE = "276";

        /**
         * 资产转库存
         */
        String ASSET_TO_STOCK = "277";
        /**
         * 资产转库存-冲销
         */
        String ASSET_TO_STOCK_REVERSE = "278";

        //手工调整入库
        String MANUAL_ADJUST = "279";
    }


    /**
     * 其他出库类型（含冲销）
     * 编码规则：
     * - String 类型
     * - 奇数：正向出库
     * - 偶数：冲销出库
     * - 一正一反成对
     */
    public interface DICT_OTHER_OUT_TYPE {

        /**
         * 报废出库
         */
        String SCRAP_OUT = "281";
        /**
         * 报废出库-冲销
         */
        String SCRAP_OUT_REVERSE = "282";

        /**
         * 借出出库
         */
        String BORROW_OUT = "283";
        /**
         * 借出出库-冲销
         */
        String BORROW_OUT_REVERSE = "284";

        /**
         * 委外发料出库
         */
        String OUTSOURCE_ISSUE = "285";
        /**
         * 委外发料出库-冲销
         */
        String OUTSOURCE_ISSUE_REVERSE = "286";

        /**
         * 资产转出库
         */
        String ASSET_OUT = "287";
        /**
         * 资产转出库-冲销
         */
        String ASSET_OUT_REVERSE = "288";

        /**
         * 手工调整出库（兜底）
         */
        String MANUAL_ADJUST = "289";

    }


    public interface ISSUE_PURPOSE {
        String PROD = "PROD";     // 生产
        String RND = "RND";       // 研发
        String MRO = "MRO";       // 维修
        String ADMIN = "ADMIN";   // 行政
        String SAMPLE = "SAMPLE"; // 样品
    }


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

        // 生产领用
        String SCLY = "201";
        // 生产领用_冲销
        String SCLY_CX = "202";

        // 研发领用
        String YFLY = "211";
        // 研发领用_冲销
        String YFLY_CX = "212";


        // 维修领用
        String WXLY = "221";
        // 维修领用_冲销
        String WXLY_CX = "222";

        // 行政领用
        String XZLY = "231";
        // 行政领用_冲销
        String XZLY_CX = "232";

        // 样品领用
        String YPLY = "241";
        // 样品领用_冲销
        String YPLY_CX = "242";

        // 盘盈入库
        String PYRK = "251";
        // 盘盈入库_冲销
        String PYRK_CX = "252";

        // 盘亏出库
        String PKCK = "261";
        // 盘亏出库_冲销
        String PKCK_CX = "262";

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
        Integer Y = 0;
        Integer N = 1;
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
        //物资盘点
        String WLPD = "PD";
        //其他入库
        String QTRK = "RK";
        //其他出库
        String QTCK = "CK";
        //销售报价
        String XSBJ = "RU";
        //其他出库
        String XSDD = "ZP";
        //其他出库
        String YWJH = "JH";
    }

    /**
     * 退货蕾西
     */
    public interface DICT_RETURN_TYPE {
        // 未收货退货
        Integer NO_RECEIVE = 0;

        // 已收退货
        Integer RECEIVED = 1;


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