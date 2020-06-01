package com.skyzone.foolpeachethics.util;


import com.skyzone.foolpeachethics.model.BaseBean;

import java.util.Comparator;

/**
 * Created by Skyzone on 1/13/2017.
 */

public class PrincipleCompare implements Comparator<BaseBean> {

    @Override
    public int compare(BaseBean lhs, BaseBean rhs) {
        try {
            if (lhs.seq < rhs.seq)
                return -1;
            else
                return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
