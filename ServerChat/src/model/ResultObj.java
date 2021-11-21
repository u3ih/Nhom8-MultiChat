package model;

import java.util.List;

public class ResultObj {

	private String actionType;
	private String resultCode;
	private List<Message> listMessage;
	private String content;
	private List<Room> list;
	
	public ResultObj(String actionType,String resultCode, String content) {
		this.actionType = actionType;
		this.resultCode = resultCode;
		this.content = content;
	}
	
	public ResultObj(String actionType, String resultCode,String content, List<Message> listMessage) {
		this.actionType = actionType;
		this.resultCode = resultCode;
		this.content = content;
		this.listMessage  = listMessage;
	}
	
	public ResultObj(String actionType, String resultCode, List<Room> list) {
		this.actionType = actionType;
		this.resultCode = resultCode;
		this.list = list;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Message> getListMessage() {
		return listMessage;
	}

	public List<Room> getList() {
		return list;
	}
	public void setList(List<Room> list) {
		this.list = list;
	}
}
