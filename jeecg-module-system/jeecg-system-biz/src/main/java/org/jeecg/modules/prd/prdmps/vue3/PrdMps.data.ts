import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '计划单号',
    align:"center",
    dataIndex: 'docCode'
   },
   {
    title: '单据日期',
    align:"center",
    dataIndex: 'docTime',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
   {
    title: '业务计划',
    align:"center",
    dataIndex: 'bizPlanCodes'
   },
   {
    title: '业务计划_IDS',
    align:"center",
    dataIndex: 'bizPlanIds'
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
    title: '状态',
    align:"center",
    dataIndex: 'status'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
   {
    title: '要求交期',
    align:"center",
    dataIndex: 'deliveryTime',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
   {
    title: '回复交期',
    align:"center",
    dataIndex: 'replyDeliveryTime',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '计划单号',
    field: 'docCode',
    component: 'Input',
  },
  {
    label: '单据日期',
    field: 'docTime',
    component: 'DatePicker',
  },
  {
    label: '业务计划',
    field: 'bizPlanCodes',
    component: 'Input',
  },
  {
    label: '业务计划_IDS',
    field: 'bizPlanIds',
    component: 'Input',
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
    label: '状态',
    field: 'status',
    component: 'InputNumber',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
  },
  {
    label: '要求交期',
    field: 'deliveryTime',
    component: 'DatePicker',
  },
  {
    label: '回复交期',
    field: 'replyDeliveryTime',
    component: 'DatePicker',
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
export const prdMpsDetailColumns: JVxeColumn[] = [
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
      title: '业务数量',
      key: 'bizQty',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '排产数量',
      key: 'qty',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '工序',
      key: 'sequenceCode',
      type: JVxeTypes.inputNumber,
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
export const prdMpsBomDetailColumns: JVxeColumn[] = [
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
      title: '单价',
      key: 'unitPrice',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '要求数量',
      key: 'qty',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '可用库存',
      key: 'stockQty',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '其他说明',
      key: 'remark',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


// 高级查询数据
export const superQuerySchema = {
  docCode: {title: '计划单号',order: 0,view: 'text', type: 'string',},
  docTime: {title: '单据日期',order: 1,view: 'date', type: 'string',},
  bizPlanCodes: {title: '业务计划',order: 2,view: 'text', type: 'string',},
  bizPlanIds: {title: '业务计划_IDS',order: 3,view: 'text', type: 'string',},
  audit: {title: '审核状态',order: 4,view: 'number', type: 'number',},
  auditBy: {title: '审核人',order: 5,view: 'text', type: 'string',},
  auditTime: {title: '审核时间',order: 6,view: 'date', type: 'string',},
  status: {title: '状态',order: 7,view: 'number', type: 'number',},
  remark: {title: '备注',order: 8,view: 'text', type: 'string',},
  deliveryTime: {title: '要求交期',order: 9,view: 'date', type: 'string',},
  replyDeliveryTime: {title: '回复交期',order: 10,view: 'date', type: 'string',},
  //子表高级查询
  prdMpsDetail: {
    title: '生产计划_明细',
    view: 'table',
    fields: {
        pid: {title: '主表ID',order: 0,view: 'text', type: 'string',},
        materialCode: {title: '物料',order: 1,view: 'text', type: 'string',},
        unit: {title: '单位',order: 2,view: 'text', type: 'string',},
        specifications: {title: '规格',order: 3,view: 'text', type: 'string',},
        bizQty: {title: '业务数量',order: 4,view: 'number', type: 'number',},
        qty: {title: '排产数量',order: 5,view: 'number', type: 'number',},
        sequenceCode: {title: '工序',order: 6,view: 'number', type: 'number',},
        remark: {title: '备注',order: 7,view: 'text', type: 'string',},
    }
  },
  prdMpsBomDetail: {
    title: '生产计划_材料清单',
    view: 'table',
    fields: {
        pid: {title: '主表ID',order: 0,view: 'text', type: 'string',},
        materialCode: {title: '物料',order: 1,view: 'text', type: 'string',},
        unit: {title: '单位',order: 2,view: 'text', type: 'string',},
        specifications: {title: '规格',order: 3,view: 'text', type: 'string',},
        unitPrice: {title: '单价',order: 4,view: 'number', type: 'number',},
        qty: {title: '要求数量',order: 5,view: 'number', type: 'number',},
        stockQty: {title: '可用库存',order: 6,view: 'number', type: 'number',},
        remark: {title: '其他说明',order: 7,view: 'text', type: 'string',},
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