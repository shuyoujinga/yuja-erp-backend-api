import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '入库单号',
    align:"center",
    dataIndex: 'docCode'
   },
   {
    title: '制单日期',
    align:"center",
    dataIndex: 'docTime',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
   {
    title: '入库类型',
    align:"center",
    dataIndex: 'inType'
   },
   {
    title: '仓库',
    align:"center",
    dataIndex: 'warehouseCode'
   },
   {
    title: '库区',
    align:"center",
    dataIndex: 'areaCode'
   },
   {
    title: '仓位',
    align:"center",
    dataIndex: 'locationCode'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status'
   },
   {
    title: '审核状态',
    align:"center",
    dataIndex: 'audit'
   },
   {
    title: '审核人',
    align:"center",
    dataIndex: 'auditBy'
   },
   {
    title: '审核时间',
    align:"center",
    dataIndex: 'auditTime',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '入库单号',
    field: 'docCode',
    component: 'Input',
  },
  {
    label: '制单日期',
    field: 'docTime',
    component: 'DatePicker',
  },
  {
    label: '入库类型',
    field: 'inType',
    component: 'InputNumber',
  },
  {
    label: '仓库',
    field: 'warehouseCode',
    component: 'Input',
  },
  {
    label: '库区',
    field: 'areaCode',
    component: 'Input',
  },
  {
    label: '仓位',
    field: 'locationCode',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'InputNumber',
  },
  {
    label: '审核状态',
    field: 'audit',
    component: 'InputNumber',
  },
  {
    label: '审核人',
    field: 'auditBy',
    component: 'Input',
  },
  {
    label: '审核时间',
    field: 'auditTime',
    component: 'DatePicker',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表单数据
//子表表格配置
export const invMiscInDetailColumns: JVxeColumn[] = [
    {
      title: '主表ID',
      key: 'pid',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '物料',
      key: 'materialCode',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '单位',
      key: 'unit',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '规格',
      key: 'specifications',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '数量',
      key: 'qty',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'remark',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


// 高级查询数据
export const superQuerySchema = {
  docCode: {title: '入库单号',order: 0,view: 'text', type: 'string',},
  docTime: {title: '制单日期',order: 1,view: 'date', type: 'string',},
  inType: {title: '入库类型',order: 2,view: 'number', type: 'number',},
  warehouseCode: {title: '仓库',order: 3,view: 'text', type: 'string',},
  areaCode: {title: '库区',order: 4,view: 'text', type: 'string',},
  locationCode: {title: '仓位',order: 5,view: 'text', type: 'string',},
  status: {title: '状态',order: 6,view: 'number', type: 'number',},
  audit: {title: '审核状态',order: 7,view: 'number', type: 'number',},
  auditBy: {title: '审核人',order: 8,view: 'text', type: 'string',},
  auditTime: {title: '审核时间',order: 9,view: 'date', type: 'string',},
  remark: {title: '备注',order: 10,view: 'text', type: 'string',},
  //子表高级查询
  invMiscInDetail: {
    title: '其他入库_明细',
    view: 'table',
    fields: {
        pid: {title: '主表ID',order: 0,view: 'text', type: 'string',},
        materialCode: {title: '物料',order: 1,view: 'text', type: 'string',},
        unit: {title: '单位',order: 2,view: 'text', type: 'string',},
        specifications: {title: '规格',order: 3,view: 'text', type: 'string',},
        qty: {title: '数量',order: 4,view: 'text', type: 'string',},
        remark: {title: '备注',order: 5,view: 'text', type: 'string',},
    }
  },
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
// 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}