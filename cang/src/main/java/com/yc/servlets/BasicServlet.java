package com.yc.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


public abstract class BasicServlet<T> extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected String op;
	
	protected void outJson(Object obj,HttpServletResponse response) throws IOException{
		Gson gson=new Gson();
		String jsonString=gson.toJson(obj);
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(jsonString);
		out.close();
	}
	
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		arg0.setCharacterEncoding("utf-8");
		arg1.setCharacterEncoding("utf-8");
		op=arg0.getParameter("op");
		super.service(arg0, arg1);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}


	@SuppressWarnings("rawtypes")
	protected T parseRequest(HttpServletRequest request,Class<T> c) throws InstantiationException, IllegalAccessException{
		Map<String, String[]> map=request.getParameterMap();
		Iterator iterator=map.entrySet().iterator();
		
		Map.Entry entry;
		String value;
		T t=(T)c.newInstance();
		Method[] ms=c.getMethods();
		
		
 		while(iterator.hasNext( )) {
 			value="";
			entry=(Map.Entry) iterator.next();
			String key =(String)entry.getKey();
			Object valueObj =entry.getValue();

			if(null == valueObj){
				value = "";
			}else if(valueObj instanceof String[]){
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){
					value += values[i] + ",";
				}
				
				value = value.substring(0, value.length()-1);
			}else{
				value = valueObj.toString();
			}
			
			key="set"+key.substring(0,1).toUpperCase()+key.substring(1);
			
			try {
				for(Method method:ms){
					if(method.getName().equals(key)){
						String typeName=method.getParameterTypes()[0].getName();//获取当前方法的中参数的类型
						if("java.lang.Integer".equals(typeName) || "int".equals(typeName)){
							method.invoke(t, Integer.parseInt(value));
						}else if("java.lang.Double".equals(typeName) || "double".equals(typeName)){
							method.invoke(t, Double.parseDouble(value));
						}else if("java.lang.Float".equals(typeName) || "float".equals(typeName)){
							method.invoke(t, Float.parseFloat(value));
						}else if("java.lang.Long".equals(typeName) || "long".equals(typeName)){
							method.invoke(t, Long.parseLong(value));
						}else{
							method.invoke(t,value);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
 		}
		return t;
	}
}
