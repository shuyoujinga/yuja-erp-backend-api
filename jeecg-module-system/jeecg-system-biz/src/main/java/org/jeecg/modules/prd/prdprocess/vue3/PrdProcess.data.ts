import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '工序编码',
    align: 'left',
    dataIndex: 'processCode'
   },
   {
    title: '工序名称',
    align: 'center',
    dataIndex: 'processName'
   },
   {
    title: '用工方式',
    align: 'center',
    dataIndex: 'workType'
   },
   {
    title: '备注',
    align: 'center',
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '工序编码',
    field: 'processCode',
    component: 'Input',
  },
  {
    label: '工序名称',
    field: 'processName',
    component: 'Input',
  },
  {
    label: '用工方式',
    field: 'workType',
    component: 'InputNumber',
  },
  {
    label: '父级节点',
    field: 'pid',
    component: 'JTreeSelect',
    componentProps: {
      dict: "prd_process,process_code,id",
      pidField: "pid",
      pidValue: "0",
      hasChildField: "has_child",
    },
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

// 高级查询数据
export const superQuerySchema = {
  processCode: {title: '工序编码',order: 0,view: 'text', type: 'string',},
  processName: {title: '工序名称',order: 1,view: 'text', type: 'string',},
  workType: {title: '用工方式',order: 2,view: 'number', type: 'number',},
  remark: {title: '备注',order: 4,view: 'text', type: 'string',},
};


/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
