package com.yc.biz.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yc.bean.Favorite;
import com.yc.bean.Tag;
import com.yc.biz.TagBiz;
import com.yc.dao.MybatisHelper;

public class TagBizImpl implements TagBiz{

	@Override
	public void addTag(Favorite f,SqlSession session,boolean updateTcount){
		String[] ntagNames=openTag(f);
		List<Tag> update=new ArrayList<Tag>();
		
		if(ntagNames[0].equals("")){
			ntagNames[0]="未分类";
			//updateTcount=true;
		}
	
		try {
			//SqlSession session=MybatisHelper.getSession();
			List<Tag> oldtag=showAllTag();
			
			Tag t;
			int flag;
			List<Tag> updateCount=new ArrayList<Tag>();
			for(String ntag:ntagNames){
				flag=1;
				t=new Tag();
				t.setTname(ntag);
				for(Tag otag:oldtag){
					if(ntag.equals(otag.getTname())){
						flag=0;
						t.setTcount(otag.getTcount()+1);
						t.setTid(otag.getTid());
						updateCount.add(t);
						break;
					}
				}
				if(flag!=0){
					update.add(t);
				}
			}
			if( !update.isEmpty()){
				session.insert("com.yc.mapper.TagMapper.insertAllNewTag", update);
			}
			
			if(!updateCount.isEmpty() && updateTcount){
				//TODO:批量更新失败=。=
				for(Tag ta:updateCount){
					session.update("com.yc.mapper.TagMapper.updateTagCount", ta);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	
	private String[] openTag(Favorite f){
		String tempFtags=f.getFtags();
		
		String[] ftags = null;
		if(tempFtags.indexOf("，")!=-1){
			ftags=tempFtags.split("，");
		}else {//英文逗号和一个标签
			ftags=tempFtags.split(",");
		}
		
		return ftags;
	}

	@Override
	public List<Tag> showAllTag() {
		List<Tag> list=null;
		try {
			SqlSession session=MybatisHelper.getSession();
			list = session.selectList("com.yc.mapper.TagMapper.selectAllTag");
			session.commit();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}


}
