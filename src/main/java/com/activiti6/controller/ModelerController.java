package com.activiti6.controller;

import com.activiti6.config.domparse;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * 流程控制器
 */

@Controller
public class ModelerController{

    private static final Logger logger = LoggerFactory.getLogger(ModelerController.class);

    @Autowired
    public RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Resource
    public JdbcTemplate jdbcTemplate2;
    private Object String;
    private Object List;

    @Value("${path.activitiXmlPath}")
    private String activitiXmlPath;

    @Value("${path.dagXmlPath}")
    private String dagXmlPath;

    @Value("${path.modelXmlPath}")
    private String modelXmlPath;

    @ResponseBody
    @RequestMapping("index")
    public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
        String email = new String();
        String emailcooki = new String();
        /**/
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equals("email")){emailcooki=cookie.getValue();
                System.out.println(emailcooki);}
            // else if(cookie.getName().equals("password"))password=cookie.getValue();
            // System.out.println("xxxxxxxxxx");
        }
        //System.out.println("index_email:"+emailcooki);
        email=emailcooki.replace("%40","@");
        email=email.replace("%20","");
        System.out.println("index_email:"+email);
        //  logger.info("传来email:"+email);

        //email = "635830037@qq.com";
        // password = "123456";//

        /*String id="test_1024";//更新当前用户表
        String sql1 = "update current_user_tabler set email = ? where Identi = ?";
        String sql2 = "update current_user_tabler set password = ? where Identi = ?";
        int temp1 = jdbcTemplate.update(sql1, email,id);  int temp2 = jdbcTemplate.update(sql2, password,id);
        if(temp1 > 0&&temp2 > 0) {
            // System.out.println("ACT_RE_MODEL表中账户密码增加成功");
            logger.info("current_user表中账户密码增加"+email+"成功");
        }
        else
            // System.out.println("ACT_RE_MODEL表中账户密码增加失败");
            logger.info("ACT_RE_MODEL表中账户密码增加"+email+"失败");*/

        modelAndView.setViewName("index");//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!index
        //List list = repositoryService.createModelQuery().list();//查询email
        /*获取当前用户的email和password
        String idd="test_1024";//更新当前用户表
        String sql11 = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
        Map<String, Object> map = jdbcTemplate.queryForMap(sql11,idd);
        String email1= (java.lang.String) map.get("email");
        String password1= (java.lang.String) map.get("password"); //获取当前用户的email和password
        //System.out.println(email1+"。。。"+password1);*/
        List temp=new ArrayList();
        String sql = "SELECT ID_ FROM ACT_RE_MODEL WHERE email = '"  +email+"'";
        // System.out.println(sql);
        List modelList = jdbcTemplate.queryForList(sql);

      /*   if (modelList != null) {
            for (int i = 0; i < modelList.size(); i++) {
                //System.out.println( modelList.size());
                String result = modelList.get(i).toString().substring(5);//截取{ID_=五个字符
                System.out.println(result);

            }
        }*/
        String a=new String();int o=0;  int flag=0;
        int [] arr = new int[20];
        for(int ii=0;ii<arr.length;ii++){ arr[ii]=o;}
        for( int i = 0 ; i < repositoryService.createModelQuery().list().size() ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。

            for (int j = 0; j < modelList.size(); j++) {
                String result= modelList.get(j).toString().substring(5);//截取{ID_=五个字符  页面传过来的值
                String result1=repositoryService.createModelQuery().list().get(i).getId()+"}";//数据库的值
                if(result1.equals(result)) {
                    flag++;//记录存在}
                    Model list=repositoryService.createModelQuery().list().get(i);
                    temp.add(list);
                }
                // System.out.println(result +"///"+result1+"///"+flag);
            }

        }
        logger.info(email+"  用户拥有的工作流数量为："+flag);
        modelAndView.addObject("modelList", temp);
        return modelAndView;
    }


    // @ResponseBody
    @RequestMapping("hello")
    public ModelAndView hello(ModelAndView modelAndView){
        /*获取当前用户的email和password*/
        String idd="test_1024";//更新当前用户表
        String sql11 = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
        Map<String, Object> map = jdbcTemplate.queryForMap(sql11,idd);
        String email= (java.lang.String) map.get("email");

        System.out.println("fog项目传来的email:"+email);

        modelAndView.setViewName("hello");
        List temp=new ArrayList();
        temp.add(email);
        // temp.add(password);
        System.out.println(temp);
        modelAndView.addObject("emaillList", temp);
        return modelAndView;
    }


    /**
     * 跳转编辑器页面
     * @return
     */
    @GetMapping("editor")
    public String editor(){
        return "modeler";
    }


    /**
     * 创建模型
     * @param response
     * @param name 模型名称
     * @param key 模型key
     */
    @RequestMapping("/create")//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!new
    public void create(HttpServletResponse response, String name, String key,HttpServletRequest request) throws IOException {

        String email = new String();
        String emailcooki = new String();
        /* */
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equals("email"))emailcooki=cookie.getValue();
            // else if(cookie.getName().equals("password"))password=cookie.getValue();
            // System.out.println("xxxxxxxxxx");
        }
        System.out.println("create_email:"+emailcooki);
        email=emailcooki.replace("%40","@");
        email=email.replace("%20","");
        /*获取当前用户的email和password
        String idd="test_1024";//更新当前用户表
        String sql11 = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
        Map<String, Object> map = jdbcTemplate.queryForMap(sql11,idd);
        String email1= (java.lang.String) map.get("email");
        String password1= (java.lang.String) map.get("password"); 获取当前用户的email和password*/
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random1=new Random();
        //指定字符串长度，拼接字符并toString
        String str1=new String();
        for (int i = 0; i < 4; i++) {
            //获取指定长度的字符串中任意一个字符的索引值
            int number=random1.nextInt(str.length());
            //根据索引值获取对应的字符
            char charAt = str.charAt(number);
            str1=str1+charAt;
        }

        String  fname=name+"_"+str1;
        Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, fname);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());
        String id=model.getId();
        //System.out.println("生产的工作流" +id);
        String sql1 = "update ACT_RE_MODEL set email = ? where ID_ = ?";
        // String sql2 = "update ACT_RE_MODEL set password = ? where ID_ = ?";
        int temp1 = jdbcTemplate.update(sql1, email,id);  //int temp2 = jdbcTemplate.update(sql2, password1,id);
        if(temp1 > 0) {
            // System.out.println("ACT_RE_MODEL表中账户密码增加成功");
            logger.info("ACT_RE_MODEL表中账户增加成功");
        }
        else
            // System.out.println("ACT_RE_MODEL表中账户密码增加失败");
            logger.info("ACT_RE_MODEL表中账户增加失败");
        response.sendRedirect("/editor?modelId="+ model.getId());
        // logger.info("创建模型结束，返回模型ID：{}",model.getId());
    }

    /**
     * 创建模型时完善ModelEditorSource
     * @param modelId
     */
    @SuppressWarnings("deprecation")
    private void createObjectNode(String modelId){
        logger.info("创建模型完善ModelEditorSource入参模型ID：{}",modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId,editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            logger.info("创建模型时完善ModelEditorSource服务异常：{}",e);
        }
        logger.info("创建模型完善ModelEditorSource结束");
    }

    //@Resource ：自动注入，项目启动后会实例化一个JdbcTemplate对象,省去初始化工作。
    @Resource
    public JdbcTemplate jdbcTemplate1;
    @ResponseBody
    @RequestMapping(value = "/delete/{modelId}/{email}")//{email}/{password}//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!delete
    public String delete(@PathVariable("modelId") String modelId,@PathVariable("email") String email,HttpServletRequest request) {
        //String email = new String();
        String emailcooki = new String();

        //email = "635830037@qq.com";
        /*
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equals("email"))emailcooki=cookie.getValue();
            // else if(cookie.getName().equals("password"))password=cookie.getValue();
            // System.out.println("xxxxxxxxxx");
        }
       // System.out.println("delete_email:"+email);
        email=emailcooki.replace("%40","@");
        email=email.replace("%20","");*/
        System.out.println("delete_email:"+email);
        email=email+".com";
        /*获取当前用户的email和password
        String idd="test_1024";//更新当前用户表
        String sql = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
        Map<String, Object> map = jdbcTemplate.queryForMap(sql,idd);
        String email= (java.lang.String) map.get("email");
        String password= (java.lang.String) map.get("password"); 获取当前用户的email和password*/
        String id=modelId;//获取当前model id
        String sql1 = "SELECT * FROM ACT_RE_MODEL WHERE ID_ = ?;";//通过id找到要删除的文件
        Map<String, Object> map1 = jdbcTemplate.queryForMap(sql1,id);
        System.out.println("-------------------------------------分割线--------------------------");
        //  System.out.println(map1);
        String xml_name= (java.lang.String) map1.get("xmlfile");

        if(xml_name==null){//没有上传就不需要更新数据库
        }
        else {System.out.println("删除文件名："+xml_name);
            String xml_path = dagXmlPath + xml_name;
//            String xml_path="E:/dagXML/"+xml_name;
            System.out.println("删除文件路径为："+xml_path);
            File file_de = new File(xml_path);
            System.gc();//清除io流，防止文件无法正常删除
            if (file_de.exists()) {
                //如果文件存在，则删除文件
                file_de.delete();
            }
            //更新user_info中对应工作流xml信息
            String sql11 = "SELECT * FROM userinfo WHERE email = '" +email+"'";
            List<User> userList = (List<User>) jdbcTemplate.query(sql11, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int i) throws SQLException {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setOrganization(rs.getString("organization"));
                    user.setSubscribe(rs.getString("subscribe"));
                    user.setXmlfiles(rs.getString("xmlfiles"));
                    return user;
                }
            });
            /*删除数据库中的文件目录*/
            User user= userList.get(0);
            String xmlfiles = user.getXmlfiles();


            JSONArray files= new JSONArray();
            JSONArray xmlfiles_json = JSONArray.parseArray(xmlfiles);
            System.out.println("用户在数据库的xml文件数目"+xmlfiles_json.size());
            int size=xmlfiles_json.size();
            if(size!=0){
                for (int i = 0;i< xmlfiles_json.size(); i++){
                    if(xmlfiles_json.get(i).toString().equals(xml_name)) continue;//文件名相同则不再添加
                    else files.add(xmlfiles_json.get(i).toString());//System.out.println(xmlfiles_json.get(i).toString());
                }
            }
            String xmlfiles_update = "update userinfo set xmlfiles ='" + files.toString() + "' where email='" + email +"'";
            System.out.println(xmlfiles_update);
            int insert_flag = jdbcTemplate.update(xmlfiles_update);
            System.out.println(insert_flag);}

        repositoryService.deleteModel(modelId);
        return "Delete Successfully";
    }


    //@Resource ：自动注入，项目启动后会实例化一个JdbcTemplate对象,省去初始化工作。
    @Resource
    public JdbcTemplate jdbcTemplate;
    @ResponseBody
    @RequestMapping("/download/{modelId}/{email}")//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!submit
    public String downloadBPMN(@PathVariable("modelId") String modelId, @PathVariable("email") String email,HttpServletRequest request) {
        logger.info("开始执行导出！");
        String res=new String();
        Model model = repositoryService.getModel(modelId);
        // String email = new String();
        String emailcooki = new String();
        /*
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equals("email"))emailcooki=cookie.getValue();
            // else if(cookie.getName().equals("password"))password=cookie.getValue();
            // System.out.println("xxxxxxxxxx");
        }
        System.out.println("submit_email:"+emailcooki);
        email=emailcooki.replace("%40","@");
        email=email.replace("%20","");*/
        System.out.println("submit_email:"+email);
        email=email+".com";
        // email="635830037@qq.com";
        /*获取当前用户的email和password
        String idd="test_1024";//更新当前用户表
        String sql11 = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
        Map<String, Object> map = jdbcTemplate.queryForMap(sql11,idd);
        String email= (java.lang.String) map.get("email");
        String password= (java.lang.String) map.get("password"); 获取当前用户的email和password*/
        //  System.out.println("modelID"+modelId );
        //System.out.println( );
        if (model == null) {
            return "Workflow is null !";
            // return "redirect:/index";
            //    res="model is null";
            //    return res;
        }

        try {
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
            JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(editorNode);

            // 处理异常
            if (bpmnModel.getMainProcess() == null) {
                return "Workflow is null";
                //  return "redirect:/index";
                //res="model is null";
                //   return res;
            }

            String filePath = new String(),filename, type = "";
            byte[] exportBytes;
            String mainProcessId = bpmnModel.getMainProcess().getId();
//            String mainProcessName = bpmnModel.getMainProcess().getName();
            if ("JSON".equals(type)) {
                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";
            } else {
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);
                // System.out.println(exportBytes);
                // String xmlStr=exportBytes.toString();
                //System.out.println(xmlStr);
                filename = mainProcessId + "_activiti.xml";
//                filename = mainProcessName + "_activiti.xml";
//                filePath="E:/activitiXML/"+filename;//
                filePath = activitiXmlPath + filename;
            }

            InputStream inputStream = null;
            InputStream inputStreams = null;
            inputStream = new ByteArrayInputStream(exportBytes);
            // 进行解码
            //BASE64Decoder base64Decoder = new BASE64Decoder();
            //byte[] byt = base64Decoder.decodeBuffer(inputStream);
            inputStreams = new ByteArrayInputStream(exportBytes);
            File file = new File(filePath);
            if (file.exists()) {
                //如果文件存在，则删除文件
                file.delete();
            }
            Files.copy(inputStreams, Paths.get(filePath));
            /*转为为平台可执行文件*/
            domparse xml=new domparse();
             System.out.println("上传路径为： "+filePath);
            //*String getfile="B:/Abiye/ActivitiExplorer/src/main/resources/transform/Epigenomics_24 _activiti.xml";*//*
            xml.getDocument(filePath);//加载需要导入的xml文件，并进行解析
            String number_conform;
            number_conform=xml.intitialdata();//节点信息初始化
            xml.deleteNode();//删除根节点信息进行重构，节点坐标信息仍然保留
            xml.creatNode();//创建xml任务节点
            xml.createdge();//创建xml边
            xml.edgedependency();//创建边依赖
            xml.deleteionode();//删除头尾无用依赖节点
            //
            String sql12 = "SELECT * FROM ACT_RE_MODEL WHERE ID_ = ?;";//通过id找到要删除的文件
            Map<String, Object> map1 = jdbcTemplate.queryForMap(sql12,modelId);
            System.out.println("-------------------------------------分割线--------------------------");
            // System.out.println(map1);
            String xml_name= (java.lang.String) map1.get("xmlfile");
            System.out.println("当前act——model表中对应XML文件名："+xml_name);
            String finalName=xml.returnname();
            int flag1=0;//判断生成的xml-name会不会跟别的xml——name重复
            List temp=new ArrayList();
            String sqll = "SELECT xmlfile FROM ACT_RE_MODEL WHERE email = '"  +email+"' AND ID_  != '"+modelId+"'";
            // System.out.println(sql);
            List modelList = jdbcTemplate.queryForList(sqll);
            String test=finalName+"}";
            if (modelList != null) {
                for (int i = 0; i < modelList.size(); i++) {
                    //System.out.println( modelList.size());
                    String result = modelList.get(i).toString().substring(9);//截取{ID_=五个字符
                    if(test.equals(result))flag1=1;
                    System.out.println("当前act——model表中对应其他XML文件名："+result);
                }
            }

            if(flag1==1) {
                res="The Workflow XMl Filename is repeated,correct it!";
            }
            else if(number_conform.length()!=49){//出现字母
                res=number_conform;
            }
            else{
                String finalName1 = xml.saveXml();//保存xml文件
                System.out.println("finalname:"+finalName);
                //file.delete();删除中间ACTIVITI_xML文件
                int flag=0;
                if(xml_name==null||xml_name.equals(finalName)){
                    flag=1;//为空不需要删除操作
                }
                if(flag==0){//  //更新user_info中对应工作流xml信息，将之前存的文件删除，
                    System.out.println("删除文件名："+xml_name);
//                    String xml_path="E:/dagXML/"+xml_name;
                    String xml_path = dagXmlPath + xml_name;
                    System.out.println("删除文件路径为："+xml_path);
                    File file_de = new File(xml_path);
                    System.gc();//清除io流，防止文件无法正常删除
                    if (file_de.exists()) {
                        //如果文件存在，则删除文件
                        file_de.delete();
                    }
                    String sql111 = "SELECT * FROM userinfo WHERE email = '" +email+"'";
                    List<User> userList = (List<User>) jdbcTemplate.query(sql111, new RowMapper<User>() {
                        @Override
                        public User mapRow(ResultSet rs, int i) throws SQLException {
                            User user = new User();
                            user.setUsername(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            user.setEmail(rs.getString("email"));
                            user.setOrganization(rs.getString("organization"));
                            user.setSubscribe(rs.getString("subscribe"));
                            user.setXmlfiles(rs.getString("xmlfiles"));
                            return user;
                        }
                    });
                    /*删除数据库中的文件目录*/
                    User user= userList.get(0);
                    String xmlfiles = user.getXmlfiles();
                    JSONArray files= new JSONArray();
                    JSONArray xmlfiles_json = JSONArray.parseArray(xmlfiles);
                    System.out.println("用户在数据库的xml文件数目"+xmlfiles_json.size());
                    int size=xmlfiles_json.size();
                    if(size!=0){
                        for (int i = 0;i< xmlfiles_json.size(); i++){
                            if(xmlfiles_json.get(i).toString().equals(xml_name)) continue;//文件名相同则不再添加
                            else files.add(xmlfiles_json.get(i).toString());//System.out.println(xmlfiles_json.get(i).toString());
                        }
                    }
                    String xmlfiles_update = "update userinfo set xmlfiles ='" + files.toString() + "' where email='" + email +"'";
                    System.out.println(xmlfiles_update);
                    int insert_flag = jdbcTemplate.update(xmlfiles_update);
                    System.out.println(insert_flag);
                }//if(flag==0)替换掉原本文件

                // 更新act_re_model中对应工作流新的xml信息
                String sql1 = "update ACT_RE_MODEL set xmlfile = ? where ID_ = ?";
                int temp1 = jdbcTemplate.update(sql1, finalName,modelId);
                if(temp1 > 0) {
                    // System.out.println("ACT_RE_MODEL表中账户密码增加成功");
                    logger.info("ACT_RE_MODEL表中更新"+finalName+"成功");
                }
                else
                    // System.out.println("ACT_RE_MODEL表中账户密码增加失败");
                    logger.info("act_re_model表中更新"+finalName+"失败");

                //增加user_info中对应工作流xml信息
                String sql = "SELECT * FROM userinfo WHERE email = '" +email+"'";
                System.out.println(sql);
                List<User> userList = (List<User>) jdbcTemplate.query(sql, new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        user.setOrganization(rs.getString("organization"));
                        user.setSubscribe(rs.getString("subscribe"));
                        user.setXmlfiles(rs.getString("xmlfiles"));
                        return user;
                    }
                });
//        System.out.println("userlist" + userList);
                User user= userList.get(0);
                String xmlfiles = user.getXmlfiles();
                JSONArray files= new JSONArray();
                JSONArray xmlfiles_json = JSONArray.parseArray(xmlfiles);
                System.out.println(xmlfiles_json.size());
                int size=xmlfiles_json.size();
                System.out.println("finalName"+finalName);
                String finalNmame_name = finalName.split("_")[0];
                String finalName_size = finalName.split("_")[1];
                if(size!=0){
                    for (int i = 0;i< xmlfiles_json.size(); i++){
                        String xmlfiles_item = xmlfiles_json.get(i).toString();
                        String item_name = xmlfiles_item.split("_")[0];
                        System.out.println(item_name);
                        System.out.println(finalNmame_name.equals(item_name)+"ddddddddd");
                        if(!finalNmame_name.equals(item_name)){
                            files.add(xmlfiles_item);
                        }

                    }
                }
                files.add(finalName);
                String xmlfiles_update = "update userinfo set xmlfiles ='" + files.toString() + "' where email='" + email +"'";
                System.out.println(xmlfiles_update);
                int insert_flag = jdbcTemplate.update(xmlfiles_update);
                System.out.println(insert_flag);
                //System.out.println(files);
                logger.info("执行完成！");
                res="Submit Successfully!";
            }

          /*   ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);

             IOUtils.copy(in, response.getOutputStream());
          response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();*/
        } catch (Exception e) {
            res="Submit Failure!";
            //logger.error("导出model的xml文件失败", e);
            logger.error("转换model的xml文件失败", e);
        }
        return res;
        // return "redirect:/index";
        //  res="model is null";
        //   return res;
    }



    @ResponseBody
    @RequestMapping("/getFile/{name}")//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!getmodel
    public String deployUploadedFile(@PathVariable("name") String name, HttpServletRequest request) {
      //  String filename="Epigenomics_24.xml";
         // String filename=name;
          name=name+".xml";
        System.out.println("getfilename :"+name);
        String filename=name;
        String id=new String();
//        File files1 = new File("E:/modelXML");//创建文件对象，url是目标目录
        File files1 = new File(modelXmlPath);
       // File files1 = new File(modelXmlPath);
        File[] files = files1.listFiles();// 读取文件夹下的所有文件}
        for (int i = 0; i < files.length; i++) {
            String modelxmlname=files[i].getName();
            System.out.println("modelXML文件夹中："+modelxmlname);
            if(!filename.equals(modelxmlname))continue;
            System.out.println("开始上传");
                File file = files[i];
                //转换
                byte[] buffer = null;
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int n;
                    while ((n = fis.read(b)) != -1) {
                        bos.write(b, 0, n);
                    }
                    fis.close();
                    bos.close();
                    buffer = bos.toByteArray();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //上传
                InputStreamReader in = null;
                try {
                    try {
                        boolean validFile = false;
                        String fileName = file.getName();
                        if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".xml")) {
                            validFile = true;
                            XMLInputFactory xif = XMLInputFactory.newInstance();
                            in = new InputStreamReader(new ByteArrayInputStream(buffer), "UTF-8");
                            XMLStreamReader xtr = xif.createXMLStreamReader(in);
                            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

                            if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
//                        notificationManager.showErrorNotification(Messages.MODEL_IMPORT_FAILED,
//                                i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_BPMN_EXPLANATION));
                                System.out.println("err1");
                            } else {

                                if (bpmnModel.getLocationMap().isEmpty()) {
//                            notificationManager.showErrorNotification(Messages.MODEL_IMPORT_INVALID_BPMNDI,
//                                    i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_BPMNDI_EXPLANATION));
                                    System.out.println("err2");
                                } else {

                                    String processName = null;
                                    if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
                                        processName = bpmnModel.getMainProcess().getName();
                                    } else {
                                        processName = bpmnModel.getMainProcess().getId();
                                    }
                                    Model modelData;
                                    modelData = repositoryService.newModel();
                                    ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
                                    String fname = processName + "_Model";
                                    modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, fname);
                                    modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "Workflow Model");
                                    modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
                                    modelData.setMetaInfo(modelObjectNode.toString());
                                    modelData.setName(fname);
                                    repositoryService.saveModel(modelData);

                                    BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
                                    ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);

                                    repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
                                    // System.out.println("modelData id:"+modelData.getId());
                                    String email = new String();
                                    String emailcooki = new String();
                                    //  email="635830037@qq.com";
                                    /**/
                                    Cookie[] cookies = request.getCookies();
                                    for (Cookie cookie : cookies) {
                                        if (cookie.getName().equals("email")) emailcooki = cookie.getValue();
                                        // else if(cookie.getName().equals("password"))password=cookie.getValue();
                                        // System.out.println("xxxxxxxxxx");
                                    }
                                    System.out.println("getmodel_email:" + emailcooki);
                                    email = emailcooki.replace("%40", "@");
                                    email = email.replace("%20", "");
                                    /*获取当前用户的email和password
                                    String idd="test_1024";//更新当前用户表
                                    String sql = "SELECT * FROM current_user_tabler WHERE Identi = ?;";//通过id查找一条数据库信息
                                    Map<String, Object> map = jdbcTemplate.queryForMap(sql,idd);
                                    String email= (java.lang.String) map.get("email");
                                    String password= (java.lang.String) map.get("password"); 获取当前用户的email和password*/
                                    //System.out.println("生产的工作流" +id);
                                    id = modelData.getId();

                                    String sql1 = "update ACT_RE_MODEL set email = ? where ID_ = ?";
                                    String sql2 = "update ACT_RE_MODEL set password = ? where ID_ = ?";
                                    int temp1 = jdbcTemplate.update(sql1, email, id);
                                    if (temp1 > 0) {
                                        // System.out.println("ACT_RE_MODEL表中账户密码增加成功");
                                        logger.info("ACT_RE_MODEL表中上传模板文件账户增加成功");
                                    } else
                                        // System.out.println("ACT_RE_MODEL表中账户密码增加失败");
                                        logger.info("ACT_RE_MODEL表中上传模板文件账户增加失败");
                                }
                            }
                        } else {
//                    notificationManager.showErrorNotification(Messages.MODEL_IMPORT_INVALID_FILE,
//                            i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_FILE_EXPLANATION));
                            System.out.println("err3");
                        }
                    } catch (Exception e) {
                        //String errorMsg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
                        //notificationManager.showErrorNotification(Messages.MODEL_IMPORT_FAILED, errorMsg);
                        System.out.println("err4");
                    }
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
//                    notificationManager.showErrorNotification("Server-side error", e.getMessage());
                            System.out.println("err5");
                        }
                    }
                }//上传


        }//for

        return id;//http://127.0.0.1:8089/editor?modelId=160001
    }
    @ResponseBody
    @RequestMapping("/getmodel")
public String getmodelname(){
        String modelxmlname=new String();
        modelxmlname="";
        File files1 = new File(modelXmlPath);
        // File files1 = new File(modelXmlPath);
        File[] files = files1.listFiles();// 读取文件夹下的所有文件}
        for (int i = 0; i < files.length; i++) {
            modelxmlname=modelxmlname+files[i].getName();
            modelxmlname=modelxmlname+";";
            System.out.println("modelXML文件夹中："+modelxmlname);
        }
        return modelxmlname;
    }
    /**
     * 删除流程实例
     * @param modelId 模型ID
     * @param
     * @return


     @ResponseBody
     @RequestMapping("/delete")
     public Object deleteProcessInstance(String modelId){
     logger.info("删除流程实例入参modelId：{}",modelId);
     Map<String, String> map = new HashMap<String, String>();
     Model modelData = repositoryService.getModel(modelId);
     if(null != modelData){
     try {
     ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(modelData.getKey()).singleResult();
     if(null != pi) {
     runtimeService.deleteProcessInstance(pi.getId(), "");
     historyService.deleteHistoricProcessInstance(pi.getId());
     }
     map.put("code", "SUCCESS");
     } catch (Exception e) {
     logger.error("删除流程实例服务异常：{}",e);
     map.put("code", "FAILURE");
     }
     }
     logger.info("删除流程实例出参map：{}",map);
     return map;
     }

     */
}
