package com.zhangry.demo.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangry on 2017/3/17.
 */
public class Page<T> extends QueryParameter {
    private List<T> result = new ArrayList();
    private int totalCount = -1;
    private int totalRows = 0;

    public Page() {
    }

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    public Page(int pageSize, boolean autoCount) {
        this.pageSize = pageSize;
        this.autoCount = autoCount;
    }

    public List<T> getResult() {
        return this.result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        if(this.totalCount == -1) {
            return -1;
        } else {
            int count = this.totalCount / this.pageSize;
            if(this.totalCount % this.pageSize > 0) {
                ++count;
            }

            return count;
        }
    }

    public int getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public boolean isHasNext() {
        return this.pageNo + 1 <= this.getTotalPages();
    }

    public int getNextPage() {
        return this.isHasNext()?this.pageNo + 1:this.pageNo;
    }

    public boolean isHasPre() {
        return this.pageNo - 1 >= 1;
    }

    public int getPrePage() {
        return this.isHasPre()?this.pageNo - 1:this.pageNo;
    }
}

