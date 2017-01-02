package com.yc.biz;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yc.bean.Favorite;

public interface FavoriteBiz {
	
	public void addFavorite(Favorite f,SqlSession session);
	
	public Integer isExist(Favorite f,SqlSession session);
	
	public void updateExistFavorite(Favorite f,SqlSession session);
	
	public List<Favorite> toList(String type,SqlSession session);
}
