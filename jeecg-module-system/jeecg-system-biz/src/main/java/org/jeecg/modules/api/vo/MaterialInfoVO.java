package org.jeecg.modules.api.vo;


import lombok.Data;

import java.util.List;

@Data
public class MaterialInfoVO {



    private String materialCode;

    private String unit;

    private String specifications;

    private Double unitPrice;

    private Double stockQty;


    private List<MaterialBomInfoVO> bomInfoList;

}
