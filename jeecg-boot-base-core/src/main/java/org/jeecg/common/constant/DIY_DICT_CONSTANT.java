package org.jeecg.common.constant;

public class DIY_DICT_CONSTANT {
    /**
     * 自定义物料物料
     */
    public static  final String MATERIAL = "Material";


    /**
     * 自定义物料物料的SQL语句
     */
    public static  final String MATERIAL_SQL = "SELECT CONCAT_WS( '_', material_code, material_name,specifications ) AS text, material_code AS `value`  FROM yujiakeji_materials ";

    /**
     * 自定义供应商
     */
    public static  final String SUPPLIER = "Supplier";


    /**
     * 自定义供应商的SQL语句
     */
    public static  final String SUPPLIER_SQL = "SELECT CONCAT_WS( '_', code, name ) AS text, code AS `value`  FROM yujiakeji_suppliers ";
    /**
     * 自定义供应商
     */
    public static  final String USER = "User";


    /**
     * 自定义供应商的SQL语句
     */
    public static  final String USER_SQL = "SELECT CONCAT_WS( '_', username, realname ) AS text, username AS `value`  FROM sys_user ";

    /**
     * 自定义部门数据-仓位
     */
    public static  final String WAREHOUSE = "Warehouse";


    /**
     * 自定义部门数据-仓位的SQL语句
     */
    public static  final String WAREHOUSE_SQL = "SELECT depart_name AS text, org_code AS `value`  FROM sys_depart  where org_type=4 AND  org_code like 'A01A03A04%' ";

}
