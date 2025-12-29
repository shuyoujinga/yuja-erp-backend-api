package org.jeecg.modules.inv.invissue.util;

import cn.hutool.core.lang.Assert;
import org.constant.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IssuePurposeMoveTypeMapping {

    private static final Map<String, String> PURPOSE_TO_MOVE_TYPE;

    static {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.ISSUE_PURPOSE.PROD, Constants.DICT_MOVE_TYPE.SCLY);
        map.put(Constants.ISSUE_PURPOSE.RND, Constants.DICT_MOVE_TYPE.YFLY);
        map.put(Constants.ISSUE_PURPOSE.MRO, Constants.DICT_MOVE_TYPE.WXLY);
        map.put(Constants.ISSUE_PURPOSE.ADMIN, Constants.DICT_MOVE_TYPE.XZLY);
        map.put(Constants.ISSUE_PURPOSE.SAMPLE, Constants.DICT_MOVE_TYPE.YPLY);
        PURPOSE_TO_MOVE_TYPE = Collections.unmodifiableMap(map);
    }

    public static String getMoveType(String purpose) {
        String moveType = PURPOSE_TO_MOVE_TYPE.get(purpose);
        Assert.notNull(moveType, "不支持的领用目的 purpose=" + purpose);
        return moveType;
    }

    private IssuePurposeMoveTypeMapping() {}
}
