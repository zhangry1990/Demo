package com.zhangry.demo.data.hibernate;

import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/16.
 */
class ResultToMap implements ResultTransformer {

    ResultToMap() {
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> result = new LinkedHashMap(aliases.length);

        for(int i = 0; i < aliases.length; ++i) {
            result.put(aliases[i], tuple[i]);
        }

        return result;
    }

    public List transformList(List collection) {
        return collection;
    }
}
