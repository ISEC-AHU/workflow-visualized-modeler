// 转化自定义的xml文件
$("#uploadFile").click(function () {
    return $("#uploadFile-file").click();
});
$("#uploadFile-file").change(function () {
    var WorkflowData = new FormData();
    WorkflowData.append("file",$(this)[0].files[0]);
    $.ajax({
        url:"/uploadFile",
        type:"POST",
        data:WorkflowData,
        dataType:"text",
        cache: false,
        //用于对data参数进行序列化处理 这里必须false
        processData: false,
        contentType: false,
        mimeType:"multipart/form-data",
        success:function () {
         //   alert("UploadFile Success!");
        },
        error:function () {
          //  alert("UploadFile Error!");
        }
    })

});