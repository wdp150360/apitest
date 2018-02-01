package base.bean;

import java.io.Serializable;

public class ResultBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	private boolean success;
	
    private Object data;
    
    public ResultBean() {
    	
    }
    
    public ResultBean(Object data) {
        this.success = true;
        this.data = data;
    }
    
    public ResultBean(boolean success) {
        this.success = success;
    }

    public ResultBean(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
