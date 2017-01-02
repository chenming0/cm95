package com.yc.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.yc.bean.Favorite;
import com.yc.bean.Tag;
import com.yc.biz.FavoriteBiz;
import com.yc.biz.TagBiz;
import com.yc.biz.impl.FavoriteBizImpl;
import com.yc.biz.impl.TagBizImpl;
import com.yc.dao.MybatisHelper;


public class FavoriteServlet extends BasicServlet {
	private static final long serialVersionUID = 1L;
	
	private TagBiz tg=new TagBizImpl();
	private FavoriteBiz fb=new FavoriteBizImpl();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(op.equals("toAdd")){
				toAdd(request,response);
			}else if(op.equals("toList")){
				toList(request,response);
			}else if(op.equals("selectAllTag")){
				selectAllTag(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	private void selectAllTag(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Tag> tags=tg.showAllTag();
		super.outJson(tags, response);
	}

	private void toList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String type=request.getParameter("type");
		SqlSession session=MybatisHelper.getSession();
		List<Favorite> fs=fb.toList(type, session);
		super.outJson(fs, response);
	}

	private void toAdd(HttpServletRequest request, HttpServletResponse response) throws Exception, IllegalAccessException {
		Favorite f=(Favorite) super.parseRequest(request, Favorite.class);
		SqlSession session=MybatisHelper.getSession();
		
		//int status=0;
		try {
			//TODO:更新数量
			/*Integer fid=fb.isExist(f, session);
			if(fid!=null){
				f.setFid(fid);
				fb.updateExistFavorite(f, session);
				tg.addTag(f,session,false);
				status=1;
			}else{*/
			
			
			fb.addFavorite(f,session);
			tg.addTag(f, session, true);//true更新数量

			session.commit();
			super.outJson(1, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			super.outJson(0, response);
		}
	}
	
	
	

}
