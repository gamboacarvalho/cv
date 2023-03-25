package orm;

import java.util.Iterator;

import orm.metadata.JdbcColumnInfo;
import orm.metadata.JdbcEntityInfo;

import app.helpers.Iterables;

public class JdbcSqlGenerator {
	private final JdbcEntityInfo entityInfo;
	
	//generate first time use
	private String updateSql = null;
	
	private String insertSql = null;
	
	private String selectSql = null;

	private String deleteSql = null;
	
	
	public JdbcSqlGenerator(JdbcEntityInfo ei){
		entityInfo=ei;
	}

	public String insertSql(){
		if(insertSql==null){
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append("["+entityInfo.getTableName()+"]");
			sb.append(" (");
			sb.append(implodeColumns(entityInfo.updatableColumns(), ", "));
			sb.append(") VALUES (");
			int i = Iterables.count(entityInfo.updatableColumns());
			sb.append("?");
			for(;i>1;--i)
				sb.append(", ?");
			sb.append(")");
			insertSql = sb.toString();
		}
		return insertSql;
	}
	
	public String selectSql(){
		return selectSql(null);
	}
	public String selectSql(String where){
		if(selectSql==null){
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(implodeColumns(entityInfo.columns(), ", "));
			sb.append(" FROM ");
			sb.append("["+entityInfo.getTableName()+"]");
			selectSql = sb.toString();
		}
		return selectSql + ((where!=null && where.length() >0) ? " WHERE " +where : "");
	}
	
	public String selectByIdSql(){
		return selectByIdSql(null);
	}
	
	public String selectByIdSql(String and){
		return selectSql(null) + " WHERE " + pkQueryString() + (and == null? "":" AND "+and);
	}
	public String deleteSql(){
		if(deleteSql==null){
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ");
			sb.append("["+entityInfo.getTableName()+"]");
			sb.append(" WHERE ");
			sb.append(pkQueryString());
			deleteSql = sb.toString();
		}
		return deleteSql;
	}
	
	public String updateSql(){
		if(updateSql==null){
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE ");
			sb.append("["+entityInfo.getTableName()+"]");
			sb.append(" SET ");
			sb.append(implodeColumns(entityInfo.updatableColumns(), " = ?, "));
			sb.append(" = ?");
			sb.append(" WHERE ");
			sb.append(pkQueryString());
			updateSql = sb.toString();
		}
		return updateSql;
	}
	
	public static String implodeColumns(Iterable<JdbcColumnInfo> objAr, String glueImplode){
		if(objAr == null)
			throw new NullPointerException();
		StringBuilder sb = new StringBuilder();
		Iterator<JdbcColumnInfo> it = objAr.iterator();
		if(it.hasNext()){
			sb.append("["+it.next()+"]");
			while(it.hasNext()){
				sb.append(glueImplode);
				sb.append("[");
				sb.append(it.next().toString());
				sb.append("]");
			}
		}
		return sb.toString();
	}
	
	private String pkQueryString(){
		return implodeColumns(entityInfo.pkColumns(), " = ? AND ") + " = ? ";
	}
}
