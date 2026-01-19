package com.labs.copilot.dto;

import java.util.List;

/**
 * Paginated response wrapper for product queries.
 */
public class PaginatedResponse<T> {
    private List<T> data;
    private PaginationInfo pagination;
    private ResponseMetadata _metadata;

    public PaginatedResponse() {
    }

    public PaginatedResponse(List<T> data, PaginationInfo pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public ResponseMetadata get_metadata() {
        return _metadata;
    }

    public void set_metadata(ResponseMetadata _metadata) {
        this._metadata = _metadata;
    }

    /**
     * Pagination information.
     */
    public static class PaginationInfo {
        private Integer page;
        private Integer pageSize;
        private Long totalCount;
        private Integer totalPages;

        public PaginationInfo() {
        }

        public PaginationInfo(Integer page, Integer pageSize, Long totalCount, Integer totalPages) {
            this.page = page;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.totalPages = totalPages;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }
    }
}
