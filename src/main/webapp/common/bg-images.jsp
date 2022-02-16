<style>
    .body {
        background: url('${pageContext.request.contextPath}/static/img/mainBg.jpg') center/cover no-repeat fixed;
    }
    .search input[type="submit"]{
        background: url('${pageContext.request.contextPath}/static/img/search.jpg') center / cover;
    }

    .error-p p{
        background: url("${pageContext.request.contextPath}/static/img/error.png") 0 0/contain no-repeat;
    }
    .book-info.reader{
        background: url("${pageContext.request.contextPath}/static/img/in-process.png") 0 50% / 18px 18px no-repeat;
    }
    .book-info.late{
        background: url("${pageContext.request.contextPath}/static/img/late.png") 0 50% / 18px 18px no-repeat;
    }
    .book-info.done{
        background: url("${pageContext.request.contextPath}/static/img/done.png") 0 50% / 18px 18px no-repeat;
    }
    .search input[type="submit"]:hover{
        background: url('${pageContext.request.contextPath}/static/img/search-hov.jpg') center / cover;
    }
</style>