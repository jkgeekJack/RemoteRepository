<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.fileupload.FileUploadBase.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>


<%
 try {
	String   fileName=null;
	String  t_ext=null;
	String  save_path=null;
	final long MAX_SIZE=5*1024*1024;

	FileItemFactory factory=new DiskFileItemFactory();
	ServletFileUpload servletFileUpload =new ServletFileUpload(factory);
	servletFileUpload.setHeaderEncoding("UTF-8");
	servletFileUpload.setSizeMax(MAX_SIZE);

	List<FileItem>fileItemsList =servletFileUpload.parseRequest(request);
	if (fileItemsList!=null && fileItemsList.size()!=0) {
		Iterator fileItr =fileItemsList.iterator();
		while(fileItr.hasNext()){
			FileItem item =(FileItem)fileItr.next();
			if(item!=null && !item.isFormField()){
				fileName=item.getName();
				long size=item.getSize();
				if(!"".equals(fileName) && size!=0){
					fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
					t_ext=fileName.substring(fileName.lastIndexOf(".")+1);
					String save_name="file"+UUID.randomUUID().toString()+"."+t_ext;
					save_path="/upload/"+save_name;
					File file=new File(request.getSession().getServletContext().getRealPath(save_path));
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					item.write(file);
				}

			}
		}
		
	}
	response.setCharacterEncoding("UTF-8");
	response.setContentType("text/plain;charset=UTF-8");
	java.io.PrintWriter p=response.getWriter();
	p.write("true");
	p.close();
 } catch (FileUploadException e) {  
            e.printStackTrace();  
        }  
%>