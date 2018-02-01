package base.bean;

public class TokenModel {
	
	/**
	 * 登录token
	 */
	 private String access_token;
	 
	 /**
	  * 认证类型
	  */
	 private String token_type;
	 
	 /**
	  * oauth2 刷新token
	  */
	
	 private String refresh_token;
	 
	 
	 /**
	  * 失效时间
	  */
	 private Long expires_in;
	 
	 /**
	  * 操作的欲
	  */
	 
	 private String scope;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	 
	 

}
