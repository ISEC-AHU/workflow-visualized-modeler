<!DOCTYPE html>
<html xmlns:display="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8"/>
    <title>Workflow visual modeler</title>

</head>
<style type="text/css">
    body {
        background: rgba(204, 204, 204, 0.05);
        height: 100%;
    }
    .background {
        display: block;
        width: 100%;
        height: 100%;
        opacity: 0.4;
        filter: alpha(opacity=40);
        background:white;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 2000;
    }
    .progressBar {
        border: solid 2px #86A5AD;
        background: white url("/diagram-viewer/images/loading1.gif") no-repeat 10px 10px;
    }
    .progressBar {
        display: block;
        width: 200px;
        height: 28px;
        position: fixed;
        top: 50%;
        left: 50%;
        margin-left: -74px;
        margin-top: -14px;
        padding: 10px 10px 10px 50px;
        text-align: left;
        line-height: 27px;
        font-weight: bold;
        position: absolute;
        z-index: 2001;
    }

     select{
         border:0.1px solid gray;
         background-color: rgba(255, 255, 255, 0.05);
         color:#666;
         height:38px;
         line-height:38px;
         white-space: nowrap;
         padding:0px 18px;
         margin: 0px 0px 0px 0px;;
         font-size:14px;
         border-radius:25px;
         cursor:pointer;
         outline:none;}

</style>
<body >
<script  type="text/javascript" src="editor-app/layui/layui.all.js"></script>
<script  type="text/javascript" src="editor-app/layui/layui.js"></script>
<script  type="text/css" src="editor-app/layui/css/jquery-ui-1.10.4.min.css"></script>
<link rel="stylesheet" href="editor-app/layui/css/layui.css">
<link rel="stylesheet" href="/index.css">
<br />
<div class=" layui-col-md-offset3">
    <div >
        <h2 >
            &nbsp;&nbsp;<a href='/create?name=workflow&key=00001'width="20%" class=" layui-btn layui-btn-radius layui-btn-normal"><b>Draw a New Workflow</b></a>&nbsp;&nbsp;&nbsp;&nbsp;
            <button  class="layui-btn layui-btn-radius layui-btn-normal"  onclick=getmodel() title=""><b>Draw a New Workflow by templates:</b></button>
            <select  width="20%"id="selectID" class="type-select" name=""></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button href="/" style="display: none" class="layui-btn layui-btn-radius layui-btn-normal" title="">Test </button>
            <input  style="display: none" type="file" id="uploadFile-file"/>
		<#-- href="/getFile"   <button id="return_mainpage" class="layui-btn layui-btn-radius layui-btn-normal" onclick="window.location.href = 'http://127.0.0.1/FogWorkflowSim'"> Return </button>-->
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;<button id="return_mainpage" class="layui-btn layui-btn-radius layui-btn-normal"> <b>Return</b> </button>
            <button id="reflesh" style="display: none" class=" reflesh layui-btn layui-btn-radius layui-btn-normal"><b>Return</b></button>
        </h2>
    </div>
</div>

<div style="text-align:center;">
    <table width="80%"class="layui-table">
        <tr>
            <td width="5%" bgcolor="#F5F5F5"><b>No.</b></td>
            <td width="10%"bgcolor="#F5F5F5"> <strong>Name</strong></td>
            <td width="5%"bgcolor="#F5F5F5"><strong>Version</strong></td>
            <td width="15%"bgcolor="#F5F5F5"><strong>Creation Time</strong></td>
            <td width="30%"bgcolor="#F5F5F5"> <strong>Description</strong></td>
            <td width="35%"bgcolor="#F5F5F5"><strong>Operation</strong></td>
        </tr>
	        <#list modelList as model>
	        <tr>
                <td width="5%">${model_index+1}</td>
                <td width="10%"><#if (model.name)??>${model.name}<#else> </#if></td>
                <td width="5%">${model.version}.0</td>
                <td width="15%">${model.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                <!--  	<td width="20%"> Date</td>           <td width="20%"></td>-->
                <td width="30%" class="string_change">${model.metaInfo}</td>

                <td width="35%">
                    <button class="layui-btn-group  ">
                        <a id="edit_a" href="/editor?modelId=${model.id}"  class=" layui-btn layui-btn-radius layui-btn-normal"style="width: 90px; font-size: 15px; "title="Edit the Workflow"> Edit</a>&nbsp;&nbsp;
                        <h id="delete_a" href=""width="10%"class=" layui-btn layui-btn-radius layui-btn-normal"style="width: 90px; font-size: 15px;"  title="Delete the Workflow"onclick=deleteAsk(${(model.id!)}) > Delete</h>&nbsp;&nbsp;
                        <h  width="10%"class="attr_change layui-btn layui-btn-radius layui-btn-normal" style="width: 90px; font-size: 15px;"title="Submit the BPMN file" onclick=submitAsk(${(model.id!)})>Submit</h>
                        <!--    href="download/${(model.id!)}"    <a href="download/${(model.id!)}"  class="layui-icon layui-icon-export" style="font-size: 40px;"title="Download the BPMN file"></a>-->

                    </button>
                </td>
            </tr>
			</#list>
    </table>
</div>
<br><br><br>
<div style="width: 60%;text-align: center;margin: auto" class="layui-collapse" lay-accordion>
    <div style="" class="layui-colla-item">
        <h2 class="layui-colla-title">Note</h2>
        <div class="layui-colla-content layui-show">
            The workflow visual modeler supports sequential, parallel, and loop workflow structure. The attributes of the workflow task include workload, input and output data size. Users can also get some example workflow templates (e.g., Epigenomics ) which are provided by the system.
        </div>
    </div>
</div>
<div id="background" class="background" style="display: none; "></div>
<div id="progressBar" class="progressBar" style="display: none; ">Data loading, please wait...</div>

<div style="background-color: rgba(204, 204, 204, 0.05);
    text-align: center;
    position:fixed;bottom:0;
    width:100%;
    height:40px;
    z-index: 2;" >
    <div >Copyright ©  Intelligent Software and Edge Computing Lab, Anhui University</div>
</div>
<script src="editor-app/libs/jquery_1.11.0/jquery.min.js"></script>
<script src="editor-app/libs/jquery-ui-1.10.3.custom.min.js"></script>
<script src="editor-app/libs/jquery_1.11.0/jquery.cookie.js"></script>
<script >
    // var location_url = "http://127.0.0.1";
    // var location_url = "http://www.iseclab.org.cn";
    // var location_url = "http://47.98.222.243";

    // var location_url_8089 = "http://127.0.0.1:8089";
    // var location_url_8089 = "http://www.iseclab.org.cn";
    // var location_url_8089 = "http://47.98.222.243";

    $(document).ready(function(){
        var alist = document.getElementsByClassName("string_change");
        for(var i = 0; i < alist.length; i++){
            var string_val = alist[i].textContent;
            var json_str=JSON.parse(string_val);
            var x=json_str.description.toString();
            if(typeof x == "undefined" || x == null || x == "")x=" -- ";
            alist[i].textContent=x;

            //console.log(alist[i]);
        }

        $("#return_mainpage").click(function(){
            // window.location.href = location_url + '/EdgeWorkflowReal';

            window.location.href = 'http://127.0.0.1/EdgeWorkflowReal';
            // window.location.href = 'http://www.iseclab.org.cn/EdgeWorkflowReal';
            // window.location.href = 'http://47.98.222.243/EdgeWorkflowReal';
        });

        //click submit

        var email=$.cookie("email");
        // console.log("getmodel cookie:"+email);
        $.ajax({
            // url: location_url_8089 + "/getmodel",
            // url:"http://www.iseclab.org.cn:8089/getmodel",
            url:"http://127.0.0.1:8089/getmodel",
            // url:"http://47.98.222.243:8089/getmodel",
            async:false,
            type:'GET',
            dataType:'text',
            success:function(res)
            {
                var i;
                var name=res.split(".xml;");
                // console.log(name);
                for(i=0;i<name.length-1;i++) {
                    $('#selectID').append(new Option(name[i]));//往下拉菜单里添加元素,item.id
                }

                //location.href = "http://127.0.0.1:8089/index";
                //  document.getElementById("reflesh").click();
                // alert(res);
            }
        });


    });

    function getmodel(){
        var options = $("select option:selected");
        //alert(options.text());
        var name=options.text();
        console.log("name:"+name);
      // var  name="Epige";
        $.ajax({
            // url:"http://47.98.222.243:8089/getFile/"+name,
            // url:"http://www.iseclab.org.cn:8089/getFile/"+name,
            url:"http://127.0.0.1:8089/getFile/"+name,
            async:false,
            type:'GET',
            dataType:'text',
            success:function(res)
            {
                // location.href = "http://47.98.222.243:8089/editor?modelId="+res;
                // location.href = "http://www.iseclab.org.cn:8089/editor?modelId="+res;
                location.href = "http://127.0.0.1:8089/editor?modelId="+res;

                //  document.getElementById("reflesh").click();
              // alert(res);
            }
        });
    }

    function deleteAsk(id){
            var email=$.cookie("email");
            // console.log("delete cooki"+email);
            if(confirm("Are you sure to delete it?")) {
                $.ajax({
                    // url:"http://47.98.222.243:8089/delete/"+id+"/"+email,
                    // url:"http://www.iseclab.org.cn:8089/delete/"+id+"/"+email,
                    url:"http://127.0.0.1:8089/delete/"+id+"/"+email,
                    async:false,
                    type:'GET',
                    dataType:'text',
                    success:function(res)
                    {
                        //location.href = "http://127.0.0.1:8089/index";
                        document.getElementById("reflesh").click();
                        alert(res);
                    }
                });

            }
    }

    function submitAsk(id){
        // console.log("submit响应");
        var email=$.cookie("email");
        // console.log("submit cooki"+email);
        if(confirm("Are you sure to submit it?")) {
        $.ajax({
            // url:"http://47.98.222.243:8089/download/"+id+"/"+email,
            // url:"http://www.iseclab.org.cn:8089/download/"+id+"/"+email,
            url:"http://127.0.0.1:8089/download/"+id+"/"+email,
            async:false,
            type:'GET',
            dataType:'text',
            success:function(res)
            {
                //document.getElementById("reflesh").click();
                //location.href = "http://47.74.84.61:8089:8089/index";
                alert(res);
            }
        });}
    }

    $("#reflesh").click(function(){
        // window.location.href = location_url_8089 + '/index';

        window.location.href = 'http://127.0.0.1:8089';
        // window.location.href = 'http://www.iseclab.org.cn:8089/index';
        // window.location.href = 'http://47.98.222.243:8089/index';

    });

</script>





<script>
    //注意：折叠面板 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
        var element = layui.element;
    });
</script>

</body>

</html>