<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
          <a-tab-pane tab="生产计划_明细" key="prdMpsDetail" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="prdMpsDetail"
              v-if="prdMpsDetailTable.show"
              :loading="prdMpsDetailTable.loading"
              :columns="prdMpsDetailTable.columns"
              :dataSource="prdMpsDetailTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
          <a-tab-pane tab="生产计划_材料清单" key="prdMpsBomDetail" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="prdMpsBomDetail"
              v-if="prdMpsBomDetailTable.show"
              :loading="prdMpsBomDetailTable.loading"
              :columns="prdMpsBomDetailTable.columns"
              :dataSource="prdMpsBomDetailTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
    </a-tabs>

    <div style="width: 100%;text-align: center" v-if="!formDisabled">
      <a-button @click="handleSubmit" pre-icon="ant-design:check" type="primary">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts">

  import {BasicForm, useForm} from '/@/components/Form/index';
  import { computed, defineComponent, reactive, ref, unref } from 'vue';
  import {defHttp} from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import {getBpmFormSchema,prdMpsDetailColumns,prdMpsBomDetailColumns} from '../PrdMps.data';
  import {saveOrUpdate,prdMpsDetailList,prdMpsBomDetailList} from '../PrdMps.api';

  export default defineComponent({
    name: "PrdMpsForm",
    components:{
      BasicForm,
    },
    props:{
      formData: propTypes.object.def({}),
      formBpm: propTypes.bool.def(true),
    },
    setup(props){
      const [registerForm, { setFieldsValue, setProps }] = useForm({
        labelWidth: 150,
        schemas: getBpmFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: {span: 8}
      });

      const formDisabled = computed(()=>{
        if(props.formData.disabled === false){
          return false;
        }
        return true;
      });

      const refKeys = ref(['prdMpsDetail', 'prdMpsBomDetail', ]);
      const activeKey = ref('prdMpsDetail');
      const prdMpsDetail = ref();
      const prdMpsBomDetail = ref();
      const tableRefs = {prdMpsDetail, prdMpsBomDetail, };
      const prdMpsDetailTable = reactive({
        loading: false,
        dataSource: [],
        columns:prdMpsDetailColumns,
        show: false
      })
      const prdMpsBomDetailTable = reactive({
        loading: false,
        dataSource: [],
        columns:prdMpsBomDetailColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          prdMpsDetailList: allValues.tablesValue[0].tableData,
          prdMpsBomDetailList: allValues.tablesValue[1].tableData,
        }
      }

      //表单提交事件
      async function requestAddOrEdit(values) {
        await saveOrUpdate(values, true);
      }

      const queryByIdUrl = '/prdmps/prdMps/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        requestSubTableData(prdMpsDetailList, {id: data.id}, prdMpsDetailTable, ()=>{
          prdMpsDetailTable.show = true;
        });
        requestSubTableData(prdMpsBomDetailList, {id: data.id}, prdMpsBomDetailTable, ()=>{
          prdMpsBomDetailTable.show = true;
        });
        //默认是禁用
        await setProps({disabled: formDisabled.value})
      }

      initFormData();

      return {
        registerForm,
        formDisabled,
        formRef,
        handleSubmit,
        activeKey,
        handleChangeTabs,
        prdMpsDetail,
        prdMpsBomDetail,
        prdMpsDetailTable,
        prdMpsBomDetailTable,
      }
    }
  });
</script>