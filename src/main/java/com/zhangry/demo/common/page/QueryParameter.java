package com.zhangry.demo.common.page;

import com.google.common.collect.Lists;
import com.zhangry.demo.common.enums.AscDesc;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangry on 2017/3/17.
 */
public class QueryParameter {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    protected int pageNo = 1;
    protected int pageSize = 10;
    protected boolean autoCount = true;
    private List<Sort> sortList = Lists.newArrayList();

    public QueryParameter() {
    }

    public List<Sort> getSortList() {
        return this.sortList;
    }

    public void setSortList(List<Sort> sortList) {
        this.sortList = sortList;
    }

    public QueryParameter addSort(String fieldName, AscDesc ascOrDesc) {
        this.sortList.add(new Sort(fieldName, ascOrDesc));
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPageSizeSetted() {
        return this.pageSize > -1;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getFirst() {
        return this.pageNo >= 1 && this.pageSize >= 1?(this.pageNo - 1) * this.pageSize:0;
    }

    public boolean isFirstSetted() {
        return this.pageNo > 0 && this.pageSize > 0;
    }

    public boolean isOrderBySetted() {
        return !this.sortList.isEmpty();
    }

    public boolean isAutoCount() {
        return this.autoCount;
    }

    public void setAutoCount(boolean autoCount) {
        this.autoCount = autoCount;
    }

    public static class Sort implements Serializable {
        private String fieldName;
        private AscDesc ascOrDesc;

        public Sort(String fieldName, AscDesc ascOrDesc) {
            this.fieldName = fieldName;
            this.ascOrDesc = ascOrDesc;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public boolean isAsc() {
            return this.ascOrDesc == AscDesc.ASC;
        }

        public String toString() {
            return this.fieldName + " " + this.ascOrDesc;
        }

        public boolean equals(Object o) {
            if(this == o) {
                return true;
            } else if(!(o instanceof Sort)) {
                return false;
            } else {
                Sort sort = (Sort)o;
                if(this.fieldName != null) {
                    if(this.fieldName.equals(sort.fieldName)) {
                        return this.ascOrDesc == sort.ascOrDesc;
                    }
                } else if(sort.fieldName == null) {
                    return this.ascOrDesc == sort.ascOrDesc;
                }

                return false;
            }
        }

        public int hashCode() {
            int result = this.fieldName != null?this.fieldName.hashCode():0;
            result = 31 * result + (this.ascOrDesc != null?this.ascOrDesc.hashCode():0);
            return result;
        }
    }
}

