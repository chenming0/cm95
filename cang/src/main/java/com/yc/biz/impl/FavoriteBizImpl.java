package com.yc.biz.impl;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yc.bean.Favorite;
import com.yc.biz.FavoriteBiz;

public class FavoriteBizImpl implements FavoriteBiz{

	@Override
	public void addFavorite(Favorite f,SqlSession session) {
		try {
			//SqlSession session=MybatisHelper.getSession();
			f.setFurl(f.getFurl());
			session.insert("com.yc.mapper.FavoriteMapper.insertFavorite", f);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void updateExistFavorite(Favorite f, SqlSession session) {
		try {
			session.update("com.yc.mapper.FavoriteMapper.updateExistFavorite",f);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer isExist(Favorite f, SqlSession session) {
		List<Favorite> fs=session.selectList("com.yc.mapper.FavoriteMapper.selectAllFavorite");
		session.commit();
		for(Favorite favorite:fs){
			if(  favorite.getFurl().equals(f.getFurl())  ){
				return favorite.getFid();
			}
		}
		return null;
	}

	@Override
	public List<Favorite> toList(String type,SqlSession session) {
		List<Favorite> fs=null;
		if(type.equals("-1")){
			fs=session.selectList("com.yc.mapper.FavoriteMapper.selectAllFavorite");
		}else if(type.equals("未分类")){
			fs=session.selectList("com.yc.mapper.FavoriteMapper.selectCondition","");
		}else{
			fs=session.selectList("com.yc.mapper.FavoriteMapper.selectCondition","%"+type+"%");
		}
		session.commit();
		return fs;
	}

	

}
