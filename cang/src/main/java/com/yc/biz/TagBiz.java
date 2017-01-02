package com.yc.biz;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yc.bean.Favorite;
import com.yc.bean.Tag;

public interface TagBiz {
	
	public void addTag(Favorite f,SqlSession session,boolean updateTcount);
	
	public List<Tag> showAllTag();
	
}
