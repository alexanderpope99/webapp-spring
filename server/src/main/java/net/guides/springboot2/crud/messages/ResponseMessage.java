package net.guides.springboot2.crud.messages;

public class ResponseMessage {
	private String message;
	private int fileId;
  
	public ResponseMessage(String message,Integer fileId) {
	  this.message = message;
	  this.fileId=fileId;
	}
  
	public String getMessage() {
	  return message;
	}
  
	public void setMessage(String message) {
	  this.message = message;
	}
  
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
  }
