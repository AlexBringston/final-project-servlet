package ua.training.model.dto;

import java.util.List;

public class Page<T> {
    private List<T> objects;
    private Integer firstPage;
    private Integer currentPage;
    private Integer lastPage;
    private Integer previousPage;
    private Integer nextPage;
    private Integer totalPages;
    private String sortField;
    private String sortDirection;
    private String query;

    private Page(Builder<T> builder) {
        this.objects = builder.objects;
        this.firstPage = builder.firstPage;
        this.currentPage = builder.currentPage;
        this.lastPage = builder.lastPage;
        if (lastPage - currentPage >= 1) {
            nextPage = currentPage + 1;
        }
        if (currentPage - firstPage >= 1) {
            previousPage = currentPage - 1;
        }
        this.totalPages = builder.totalPages;
        this.sortField = builder.sortField;
        this.sortDirection = builder.sortDirection;
        this.query = builder.query;
    }

    public static class Builder<T> {
        private List<T> objects;
        private Integer firstPage;
        private Integer currentPage;
        private Integer lastPage;
        private Integer totalPages;
        private String sortField;
        private String sortDirection;
        private String query;

        public Builder<T> objects(List<T> objects) {
            this.objects = objects;
            return this;
        }

        public Builder<T> firstPage(Integer firstPage) {
            this.firstPage = firstPage;
            return this;
        }

        public Builder<T> currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder<T> lastPage(Integer lastPage) {
            this.lastPage = lastPage;
            return this;
        }

        public Builder<T> totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> sortField(String sortField) {
            this.sortField = sortField;
            return this;
        }

        public Builder<T> sortDirection(String sortDirection) {
            this.sortDirection = sortDirection;
            return this;
        }

        public Builder<T> query(String query) {
            this.query = query;
            return this;
        }

        public Page<T> build() {
            return new Page<T>(this);
        }
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(Integer previousPage) {
        this.previousPage = previousPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
